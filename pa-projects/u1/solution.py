#!/usr/bin/env python3

import ctypes
import datetime
import itertools as it
import multiprocessing as mp
import multiprocessing.managers as mpm
import multiprocessing.pool as mpp
import signal
import sys
import threading as th
import time
from argparse import ArgumentParser, ArgumentTypeError
from pathlib import Path

type TFacsWidths = list[int]
type TFacsWeightsMatrix = list[TFacsWidths]


class SRFLP:
    """Parallel solution of SRFLP problem using branch and bound method"""

    """Adjacency matrix containing the distance weights of the facilities"""
    weight_matrix: TFacsWeightsMatrix

    """List of facilities representing their widths"""
    facilities_widths: TFacsWidths

    """Time of calculation begin in seconds"""
    begin_time_sec: float

    def __init__(self, file_path: Path) -> None:
        self.weight_matrix, self.facilities_widths = SRFLP.load_problem(file_path)
        self.facs_size = len(self.facilities_widths)

    def compute(self, process_count: int, skip_symmetric: bool) -> None:
        """
        Start the parallel process of finding the permutation of the
        arrangement with the smallest SRFL distance metric
        on defined procesess and print final results

        :param process_count: number of available processes to use for computation
        :param skip_symmetric: skip symmetric facilities permutation (drastically speed up solution in cost of fewer finds)

        :return: None
        """

        print(f"Process count: {process_count} | Skip symmetric: {skip_symmetric}")
        self.begin_time_sec = time.time()

        with mpm.SyncManager() as manager:
            min_cost = manager.Value(
                ctypes.c_float, float("inf")
            )  # shared value as minimum SRFLP cost
            best_perms = (
                manager.list()
            )  # shared list of tuples as all permutations for minimum SRFLP cost
            lock = manager.Lock()  # lock for safe share resources access

            facs = list(
                range(len(self.weight_matrix[0]))
            )  # linear facilities index list

            with mpp.Pool(processes=process_count) as pool:
                pool.starmap(
                    self._gen_perm,
                    zip(
                        facs,
                        it.repeat(skip_symmetric),
                        it.repeat(min_cost),
                        it.repeat(best_perms),
                        it.repeat(lock),
                    ),
                )

            text = (
                f"Best cost: {min_cost.value} | Best permutations: {", ".join(map(str, best_perms))} | "
                f"Elapsed time: {datetime.timedelta(seconds=time.time() - self.begin_time_sec)}"
            )
            print("-" * len(text))
            print(text)

    def _gen_perm(
        self,
        pref_fac: int,
        skip_symmetric: bool,
        min_cost: mpm.ValueProxy,
        best_perms: mpm.ListProxy,
        lock: th.Lock,
    ) -> None:
        """
        Generate all permutations of facilities with static 'prec_fac' facility
        and find out the permutations with the lowest cost metric according to SRFLP

        :param pref_fac: static facility index
        :param skip_symmetric: skip symmetric facilities permutation (drastically speed up solution in cost of fewer finds)
        :param min_cost: shared multiprocessing proxy value as the lowest cost
        :param best_perms: shared multiprocessing proxy value as the permutations with 'min_cost'
        :param lock: multiprocessing lock for safe value set of shared proxy

        :return: None
        """

        def calc_cost(
            _perm: list[int],
        ) -> tuple[bool, None, int] | tuple[bool, int, None]:
            """
            Calculate cost of actual permutation by SRFLP distance
            metric using Branch and bound method.

            :param _perm: facilities permutation

            :returns: tuple of succesfully founded better cost, value of
                      that better cost and cutout branch index if better cost wasn't found
                      e.g.: (True, 5000, None), (False, None, 3)
            """
            cost = 0
            for prim in range(self.facs_size - 1):
                for sec in range(prim + 1, self.facs_size):
                    length = (
                        sum(
                            [
                                self.facilities_widths[_perm[i]]
                                for i in range(prim + 1, sec)
                            ]
                        )
                        + (
                            self.facilities_widths[_perm[prim]]
                            + self.facilities_widths[_perm[sec]]
                        )
                        / 2
                    )
                    cost += self.weight_matrix[_perm[prim]][_perm[sec]] * length
                    if cost > min_cost.value:
                        return False, None, prim

            return True, cost, None

        cutout_fac = (
            None  # facility in permutation list which direct to worse branch cost
        )
        cutout_branch_idx = None  # index of 'cutout_fac' in permutation list
        skip_branch = (
            False  # toggle flag for potential skip in next permutation iteration
        )

        perm = [i for i in range(self.facs_size) if i != pref_fac]
        for p in it.permutations(perm):
            if skip_symmetric and perm[-1] < pref_fac:
                continue

            perm = [pref_fac] + list(p)

            if skip_branch and perm[cutout_branch_idx] == cutout_fac:
                continue
            else:
                skip_branch = False

            better_perm, better_cost, cutout_branch_idx = calc_cost(perm)

            if better_perm:
                with lock:
                    if better_cost == min_cost.value:
                        best_perms.append(tuple(perm))
                    elif better_cost < min_cost.value:
                        min_cost.value = better_cost
                        best_perms[:] = [tuple(perm)]

                    print(
                        f"Cost: {better_cost} | Permutations: {", ".join(map(str, best_perms))} | "
                        f"Elapsed time: {datetime.timedelta(seconds=time.time() - self.begin_time_sec)}"
                    )

            else:
                cutout_fac = perm[cutout_branch_idx]
                skip_branch = True

    @staticmethod
    def load_problem(file_path: Path) -> tuple[TFacsWeightsMatrix, TFacsWidths]:
        """
        Read input file in desired format as SRFLP problem instance.

        :param file_path: path to input file with SRFLP problem instance

        :returns: tuple of adjacency matrix of facilities weights & facilities widths
                  e.g.: ([[0, 30, 17], [30, 0, 21], [17, 21, 0]], [1, 3, 2])
        """
        try:
            with open(file_path, "r") as file:
                data = file.readlines()
                data_widths = [list(map(int, i.strip().split())) for i in data[1:2]][0]
                data_original = [list(map(int, i.strip().split())) for i in data[2:]]
                data_transpose = [list(i) for i in zip(*data_original)]
                if len(data_widths) is not len(data_original[0]):
                    raise ValueError

                return [
                    [
                        max(data_original[i][j], data_transpose[i][j])
                        for j in range(len(data_original[i]))
                    ]
                    for i in range(len(data_original))
                ], data_widths
        except (IndexError, ValueError):
            sys.exit("Input is not SRFLP problem instance")


if __name__ == "__main__":
    signal.signal(
        signal.SIGINT, lambda sig, frame: sys.exit(f"Process exited gracefully")
    )

    if not (sys.version_info.major == 3 and sys.version_info.minor >= 10):
        sys.exit("Python 3.10 or above is required.")

    parser = ArgumentParser(
        prog="solution.py", description="Compute solution for SRFLP problem instance"
    )
    parser.add_argument(
        "input_file",
        help="Path to the input txt file for the SRFLP problem instance",
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
        "-s",
        "--symmetric",
        action="store_true",
        dest="skip_symmetric",
        help="Skip symmetric facilities (drastically speed up solution in cost of fewer permutations finds)",
    )
    args = parser.parse_args()

    srflp = SRFLP(file_path=args.input_file)
    srflp.compute(process_count=args.process_count, skip_symmetric=args.skip_symmetric)
