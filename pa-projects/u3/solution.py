#!/usr/bin/env python3

import gzip
import os
import signal
import sys
from argparse import ArgumentParser, ArgumentTypeError
from dataclasses import dataclass, field
from functools import partial
from pathlib import Path
import multiprocessing.pool as mpp
import multiprocessing.managers as mpm

type TListGraph = list[Node]
type TDictGraph = dict[int, Node]


@dataclass
class Node:
    index: int
    out_degree: int = 0
    in_nodes: list["Node"] = field(default_factory=list)
    page_rank: float = 1

    def increase_out_degree(self) -> None:
        self.out_degree += 1

    def add_in_node(self, node: "Node") -> None:
        self.in_nodes.append(node)


class PageRank:
    """Parallel solution for retrieving and ranking an oriented graph with Page Rank metric"""

    """
    Graph representation as a dictionary of vertex instances with a list of 
    input incident vertices and output degree
    """
    _graph: TDictGraph

    """Damping factor as the probability of a user following a link represented by a graph"""
    __damping_factor: float

    """User random jump constant"""
    __random_jumping: float

    def __init__(self, file_path: Path, row_size: int | None) -> None:
        self.__damping_factor = 0.85
        self._graph = PageRank.load_problem(file_path, row_size)
        self.__calc_random_jumping()

    def compute(
        self, process_count: int, iteration_count: int, intermediate_calc: bool
    ) -> None:
        """
        Run a parallel calculation process to rank graph vertices by page rank metric.

        :param process_count: number of available processes to use for computation
        :param iteration_count: number of iterations to perform in page ranking
        :param intermediate_calc: show intermediate page rank calculation
        """

        with mpm.SyncManager() as manager:
            shared_graph = manager.dict(self._graph)

            with mpp.Pool(processes=process_count) as pool:
                for i in range(iteration_count):
                    pool.map(
                        partial(self._calc_page_rank, _shared_graph=shared_graph),
                        [key for key in shared_graph.keys()],
                    )

                    if intermediate_calc:
                        self._graph = dict(shared_graph)
                        for _, node in self._graph.items():
                            print(
                                f"Iteration: {i} | Vertex: {node.index} | Page rank: {node.page_rank}"
                            )

            self._graph = dict(shared_graph)

        i = 0
        for _, node in self._graph.items():
            text = f"Vertex: {node.index} | Page rank: {node.page_rank}"
            if i == 0:
                print("-" * len(text))
            print(text)
            i += 1

    def _calc_page_rank(self, key_index: int, _shared_graph: mpm.DictProxy) -> None:
        """
        Calculate the page rank metric for the top of the graph

        :param key_index: index of the vertex in the graph
        :param _shared_graph: graph to calculate page rank for
        """

        node = _shared_graph[key_index]
        local_page_rank = self.__random_jumping * sum(
            (node.page_rank / node.out_degree)
            for node in node.in_nodes
            if node.out_degree > 0
        )
        node.page_rank = local_page_rank
        _shared_graph[key_index] = node

    def __calc_random_jumping(self) -> None:
        """
        Calculate the static part for page rank calculation
        """

        self.__random_jumping = (1 - self.__damping_factor) / len(
            self._graph
        ) + self.__damping_factor

    @classmethod
    def load_problem(cls, file_path: Path, row_size: int | None) -> TDictGraph:
        """
        Reads an instance of the problem in a text file format that contains
        individual edges on single rows oriented from left to right

        :param file_path: Path to the problem instance file
        :param row_size: Number of rows to read in the problem instance

        :returns: The loaded problem instance representing the oriented graph
        """

        def init_graph(_graph: TDictGraph) -> None:
            """
            Initializes the graph by default initial values

            :param _graph: Problem instance representing the oriented graph
            """

            init_page_rank = 1 / len(_graph)
            for _, node in _graph.items():
                node.page_rank = init_page_rank

        def merge_partial_graphs(_partial_graphs: list[TListGraph]) -> TDictGraph:
            """
            Merge partially loaded graph instances from parallel operations into one graph

            :param _partial_graphs: List of partially loaded graph instances from parallel operations

            :return: Merged graph instance representing the oriented graph
            """

            merged_nodes = {}
            for node in [x for xs in _partial_graphs for x in xs]:
                if node.index in merged_nodes:
                    existing_node = merged_nodes[node.index]
                    existing_node.out_degree += node.out_degree

                    for in_node in node.in_nodes:
                        if in_node.index not in [
                            i.index for i in existing_node.in_nodes
                        ]:
                            existing_node.in_nodes.append(
                                merged_nodes.get(in_node.index, in_node)
                            )
                else:
                    merged_nodes[node.index] = node

                node.in_nodes = [
                    (
                        merged_nodes[in_node.index]
                        if in_node.index in merged_nodes
                        else in_node
                    )
                    for in_node in node.in_nodes
                ]

            return merged_nodes

        with gzip.open(file_path, "rt", encoding="utf-8") as file:
            file_content = []
            for line in file:
                if line.startswith("#"):
                    continue
                file_content.append(line.strip("\n"))
                if row_size is not None and len(file_content) >= row_size:
                    break

        chunk_size = len(file_content) // 10

        with mpp.Pool(processes=os.cpu_count()) as pool:
            partial_graphs = pool.map(
                partial(
                    cls._process_lines,
                    _file_content=file_content,
                ),
                [
                    (start, min(start + chunk_size, len(file_content)))
                    for start in range(0, len(file_content), chunk_size)
                ],
            )

        merged_graph = merge_partial_graphs(partial_graphs)
        init_graph(merged_graph)

        return merged_graph

    @classmethod
    def _process_lines(
        cls, _line_range: tuple[int, int], _file_content: list[str]
    ) -> TListGraph:
        """
        Processes part of the input data and creates a local structure of the oriented graph

        :param _line_range: start and end of the input data
        :param _file_content: whole input data

        :return: Partial graph in list format
        """

        local_graph: TListGraph = []

        def get_node(index: int) -> Node:
            for node in local_graph:
                if node.index == index:
                    return node

            node = Node(index=index)
            local_graph.append(node)

            return node

        sub_content = _file_content[_line_range[0] : _line_range[1]]
        for sub_line in sub_content:
            from_vertex, to_vertex = sub_line.strip().split("\t")

            from_node = get_node(int(from_vertex))
            from_node.increase_out_degree()

            get_node(int(to_vertex)).add_in_node(from_node)

        return local_graph


if __name__ == "__main__":
    signal.signal(
        signal.SIGINT, lambda sig, frame: sys.exit(f"Process exited gracefully")
    )

    if not (sys.version_info.major == 3 and sys.version_info.minor >= 10):
        sys.exit("Python 3.10 or above is required.")

    parser = ArgumentParser(
        prog="solution.py",
        description="Loads and scores the vertices of the oriented graph using the PageRank metric",
    )
    parser.add_argument(
        "input_file",
        help="Path to the input gz file for the PageRank problem instance",
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
        type=lambda x: os.cpu_count() if int(x) > os.cpu_count() else int(x),
        help="Number of CPU threads to use for computation",
        default=os.cpu_count(),
    )
    parser.add_argument(
        "-i",
        "--iterations",
        action="store",
        dest="iteration_count",
        type=int,
        help="Number of iterating operations towards algorithm compute",
        default=100,
    )
    parser.add_argument(
        "-r",
        "--rows",
        action="store",
        dest="row_size",
        type=int,
        help="Number of rows to read in one parallel execution",
    )
    parser.add_argument(
        "-ic",
        "--inter_calc",
        action="store_true",
        dest="intermediate_calc",
        help="Perform and display the page rank for each iteration",
    )
    args = parser.parse_args()

    page_rank = PageRank(file_path=args.input_file, row_size=args.row_size)
    page_rank.compute(
        process_count=args.process_count,
        iteration_count=args.iteration_count,
        intermediate_calc=args.intermediate_calc,
    )
