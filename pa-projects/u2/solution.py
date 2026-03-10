#!/usr/bin/env python3

import datetime
import itertools as it
import multiprocessing as mp
import multiprocessing.pool as mpp
import signal
import sys
import time
from argparse import ArgumentParser, ArgumentTypeError
from functools import partial
from pathlib import Path
from typing import Callable

import numpy as np
import numpy.typing as npt

type TComputationMatrix = npt.NDArray[np.uint] | npt.NDArray[np.float64]


class AP:
    """Parallel solution for finding dataset clusters using Affinity Propagation algorithm"""

    """Similarity matrix S(i, k) as source data for computation"""
    _similarity_matrix: TComputationMatrix

    """Responsibility matrix R(i, k) indicating the degree of responsibility of index k to be representative for index i"""
    _responsibility_matrix: TComputationMatrix

    """Availability matrix A(i, k) indicating the availability rate of the index k to be representative of index i"""
    _availability_matrix: TComputationMatrix

    """Criterial matrix of the final source of representatives for each of its rows as indices"""
    _criterial_matrix: TComputationMatrix

    """List of found clusters in the criterial matrix"""
    _clusters: list[tuple[int, ...]]

    """Damping factor to reduce iterations required and increase konvergence during calculation"""
    __damping_factor: float

    """Time of calculation begin in seconds"""
    begin_time_sec: float

    def __init__(self, file_path: Path, row_size: int | None, has_header: bool) -> None:
        self.__damping_factor = 0.5  # value inspired from scikitlearn library
        self._similarity_matrix = AP.load_problem(file_path, row_size, has_header)
        self._responsibility_matrix = np.zeros(
            (self._similarity_matrix.shape[0], self._similarity_matrix.shape[1])
        )
        self._availability_matrix = np.zeros(
            (self._similarity_matrix.shape[0], self._similarity_matrix.shape[1])
        )
        self._criterial_matrix = np.zeros(
            (self._similarity_matrix.shape[0], self._similarity_matrix.shape[1])
        )

    @property
    def similarity_matrix(self) -> str:
        return self.__pretty_print(self._similarity_matrix)

    @property
    def responsibility_matrix(self) -> str:
        return self.__pretty_print(self._responsibility_matrix)

    @property
    def availability_matrix(self) -> str:
        return self.__pretty_print(self._availability_matrix)

    @property
    def criterial_matrix(self) -> str:
        return self.__pretty_print(self._criterial_matrix)

    @property
    def clusters(self) -> str:
        self.__find_clusters()

        return ", ".join(map(str, self._clusters))

    def compute(
        self, process_count: int, iteration_count: int, intermediate_calc: bool
    ) -> None:
        """
        Start the parallel process of computation to find clusters in provided
        problem instance dataset using Affinity Propagation algorithm.

        :param process_count: number of available processes to use for computation
        :param iteration_count: number of iterations leading to final cluster convergence
        :param intermediate_calc: show intermediate cluster calculcation
        """

        def update_matrix(
            func: Callable[[tuple[int, int]], float], idxs: list[tuple[int, int]]
        ) -> TComputationMatrix:
            """
            Compute values for desired matrix each in separate parallel task

            :param func: callable triggered in parallel task
            :param idxs: indexes of computation matrix

            :return: updated calculation matrix
            """

            with mpp.Pool(processes=process_count) as pool:
                values = pool.map(func, idxs)

            return np.array(values).reshape(
                (self._similarity_matrix.shape[0], self._similarity_matrix.shape[1])
            )

        def damp_matrix(
            current_matrix: TComputationMatrix, prev_matrix: TComputationMatrix
        ) -> TComputationMatrix:
            """
            Calculation of the weighted average between the value of the previous
            matrix and the value of the current matrix

            :param current_matrix: current state of the calculation matrix
            :param prev_matrix: previous state of the calculation matrix

            :returns: damped current state of the calculation matrix
            """

            return (
                self.__damping_factor * prev_matrix
                + (1 - self.__damping_factor) * current_matrix
            )

        print(f"Process count: {process_count}")
        self.begin_time_sec = time.time()

        for i in range(iteration_count):
            indexes = [
                (i, k)
                for i, k in it.product(
                    range(self._similarity_matrix.shape[0]), repeat=2
                )
            ]  # generate all combination of indexes tuple, e.g.: (0,0), (0,1), etc...

            prev_responsibility_matrix = self._responsibility_matrix.copy()
            self._responsibility_matrix = update_matrix(
                self._calc_responsibility_index, indexes
            )
            prev_availability_matrix = self._availability_matrix.copy()
            self._availability_matrix = update_matrix(
                self._calc_availability_index, indexes
            )

            if i != 0:
                self._responsibility_matrix = damp_matrix(
                    self._responsibility_matrix, prev_responsibility_matrix
                )
                self._availability_matrix = damp_matrix(
                    self._availability_matrix, prev_availability_matrix
                )

            if intermediate_calc:
                self._criterial_matrix = (
                    self._responsibility_matrix + self._availability_matrix
                )

                print(
                    f"Iteration: {i + 1} | Clusters found so far: {self.clusters} | Number: {len(self._clusters)} | "
                    f"Elapsed time: {datetime.timedelta(seconds=time.time() - self.begin_time_sec)}"
                )

        self._criterial_matrix = self._responsibility_matrix + self._availability_matrix

        text = (
            f"Clusters found: {self.clusters} | Number: {len(self._clusters)} | "
            f"Elapsed time: {datetime.timedelta(seconds=time.time() - self.begin_time_sec)}"
        )
        print("-" * len(text))
        print(text)

    def _calc_responsibility_index(self, idx: tuple[int, int]) -> float:
        """
        Calculate single responsibility matrix value for given row and column index

        :param idx: tuple of row and column index

        :returns: calculated single value for given row and column index
        """

        return (
            self._similarity_matrix[idx[0], idx[1]]
            - np.array(
                [
                    self._availability_matrix[idx[0], _k]
                    + self._similarity_matrix[idx[0], _k]
                    for _k in range(self._similarity_matrix.shape[1])
                    if _k != idx[1]
                ]
            ).max()
        )

    def _calc_availability_index(self, idx: tuple[int, int]) -> float:
        """
        Calculate single availability matrix value for given row and column index

        :param idx: tuple of row and column index

        :returns: calculated single value for given row and column index
        """

        if idx[0] != idx[1]:
            sum_max = sum(
                max(0.0, float(self._responsibility_matrix[_i, idx[1]]))
                for _i in range(self._responsibility_matrix.shape[0])
                if _i != idx[0]
            )

            return min(
                0.0, float(self._responsibility_matrix[idx[1], idx[1]]) + sum_max
            )
        else:
            return sum(
                max(0.0, float(self._responsibility_matrix[_i, idx[1]]))
                for _i in range(self._responsibility_matrix.shape[0])
                if _i != idx[1]
            )

    def __find_clusters(self) -> None:
        clusters = {}
        for index, represent in [
            (i, int(np.argmax(self._criterial_matrix[i])))
            for i in range(self._criterial_matrix.shape[0])
        ]:
            clusters.setdefault(represent, []).append(index)

        self._clusters = list(map(tuple, clusters.values()))

    @classmethod
    def load_problem(
        cls, file_path: Path, row_size: int | None, has_header: bool
    ) -> TComputationMatrix:
        """
        Reading a problem instance in a CSV file format containing matrix-oriented data
        and calculating a similarity matrix from this problem

        :param file_path: Path to the problem instance file
        :param row_size: Number of rows to read in the problem instance
        :param has_header: True if the problem instance has a header row

        :returns: similarity matrix for problem instance
        """

        def gen_similarity_matrix(problem_instance: TComputationMatrix):
            matrix = np.zeros((problem_instance.shape[0], problem_instance.shape[0]))
            for i in range(problem_instance.shape[0]):
                for j in range(problem_instance.shape[0]):
                    matrix[i, j] = (
                        -np.linalg.norm(problem_instance[i] - problem_instance[j]) ** 2
                    )
            return np.array(
                [
                    [
                        np.median(matrix) if i == j else matrix[i, j]
                        for j in range(problem_instance.shape[0])
                    ]
                    for i in range(problem_instance.shape[0])
                ]
            )

        with open(file_path, "r") as file:
            row_count = sum(1 for _ in file)
            if row_size is None:
                row_size = row_count
            elif row_size > row_count:
                row_size = row_count

        chunk_size = (
            row_size + 1 if has_header is True else row_size
        ) // mp.cpu_count()
        if chunk_size < 2:
            return gen_similarity_matrix(
                cls._load_chunk(
                    1 if has_header is True else 0,
                    _file_path=file_path,
                    _chunk_size=row_size,
                )
            )

        with mpp.Pool(processes=mp.cpu_count()) as pool:
            results = pool.map(
                partial(cls._load_chunk, _file_path=file_path, _chunk_size=chunk_size),
                [i for i in range(1 if has_header else 0, row_size, chunk_size)],
            )

        return gen_similarity_matrix(np.vstack(results))

    @classmethod
    def _load_chunk(
        cls, start_row: int, _file_path: Path, _chunk_size: int
    ) -> TComputationMatrix:
        result = np.genfromtxt(
            _file_path,
            dtype=int,
            delimiter=",",
            skip_header=start_row,
            max_rows=_chunk_size,
        )

        return (
            result[:, 1:] * 0.99 / 255 + 0.01
        )  # scaling the original value to the interval <0.01, 1.00>

    @classmethod
    def __pretty_print(cls, _input) -> str:
        return "\n".join(
            ["".join(["{:8}".format(round(item, 2)) for item in row]) for row in _input]
        )


if __name__ == "__main__":
    signal.signal(
        signal.SIGINT, lambda sig, frame: sys.exit(f"Process exited gracefully")
    )

    if not (sys.version_info.major == 3 and sys.version_info.minor >= 10):
        sys.exit("Python 3.10 or above is required.")

    parser = ArgumentParser(
        prog="solution.py",
        description="Finds dataset clusters using Affinity Propagation algorithm",
    )
    parser.add_argument(
        "input_file",
        help="Path to the input csv for the Affinity Propagation problem instance",
        type=lambda x: (
            Path(x).resolve()
            if Path(x).resolve().is_file()
            else (_ for _ in ()).throw(
                ArgumentTypeError(
                    f"Provided file '{x}' not found in '{Path(__file__).parent}'"
                )
            )
        ),
    )
    parser.add_argument(
        "-c",
        "--cpu",
        action="store",
        dest="process_count",
        type=lambda x: mp.cpu_count() if int(x) > mp.cpu_count() else int(x),
        help="Number of CPU threads to use for computation",
        default=mp.cpu_count(),
    )
    parser.add_argument(
        "-i",
        "--iterations",
        action="store",
        dest="iteration_count",
        type=int,
        help="Number of iterating operations towards convergence of the algorithm",
        default=200,  # value inspired from scikitlearn library,
    )
    parser.add_argument(
        "-r",
        "--rows",
        action="store",
        dest="row_size",
        type=int,
        help="Number of rows to read in the problem instance",
    )
    parser.add_argument(
        "-hr",
        "--header_row",
        action="store_false",
        dest="has_header",
        help="Include header row from problem instance to dataset",
    )
    parser.add_argument(
        "-ic",
        "--inter_calc",
        action="store_true",
        dest="intermediate_calc",
        help="Perform and display the cluster calculation for each iteration",
    )
    args = parser.parse_args()

    ap = AP(
        file_path=args.input_file,
        row_size=args.row_size,
        has_header=args.has_header,
    )
    ap.compute(
        process_count=args.process_count,
        iteration_count=args.iteration_count,
        intermediate_calc=args.intermediate_calc,
    )
