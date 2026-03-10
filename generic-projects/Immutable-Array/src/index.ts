class Triple<T> {
    readonly #data: Array<T> = new Array(3);

    constructor(private left: T, private middle: T, private right: T) {
        this.#data[0] = left;
        this.#data[1] = middle;
        this.#data[2] = right;
    }

    data(index: Triple.Index | number): T {
        if (index >= 0 && index <= 2) {
            return this.#data[index];
        } else {
            throw "Index out of range!";
        }
    }

    isBranch() {
        return typeof this.#data[0] === "object";
    }
}

module Triple {
    export enum Index {
        Left = 0,
        Middle = 1,
        Right = 2,
    }
}

class PerstArray<T> {
    #root: Triple<any> = null;

    constructor(size: number) {
        const listsCount = Math.ceil(size / 3);
        const lists: Array<Triple<T>> = [];

        let iterSize = size;
        for (let i = 0; i < listsCount; i++) {
            if (iterSize / 3 >= 1) {
                lists.push(this.#createList(3));
            } else {
                lists.push(this.#createList(iterSize));
            }
            iterSize -= 3;
        }

        this.#root = this.#harvestNodes(lists, [])[0];
    }

    toString() {
        const output: Array<string> = [];
        this.#printNode(this.#root, output);
        console.log(output.toString());
    }

    #printNode(node: Triple<any>, output: Array<string>): void {
        if (node.data(Triple.Index.Left).isBranch()) {
            this.#printNode(node.data(Triple.Index.Left), output);
        } else {
            output.push(`${node.data(Triple.Index.Left)?.data(Triple.Index.Left)}`);
            output.push(`${node.data(Triple.Index.Left)?.data(Triple.Index.Middle)}`);
            output.push(`${node.data(Triple.Index.Left)?.data(Triple.Index.Right)}`);
        }
        if (
            node.data(Triple.Index.Middle) &&
            node.data(Triple.Index.Middle).isBranch()
        ) {
            this.#printNode(node.data(Triple.Index.Middle), output);
        } else {
            output.push(`${node.data(Triple.Index.Middle)?.data(Triple.Index.Left)}`);
            output.push(`${node.data(Triple.Index.Middle)?.data(Triple.Index.Middle)}`);
            output.push(`${node.data(Triple.Index.Middle)?.data(Triple.Index.Right)}`);
        }
        if (node.data(Triple.Index.Right) && node.data(Triple.Index.Right).isBranch()) {
            this.#printNode(node.data(Triple.Index.Right), output);
        } else {
            output.push(`${node.data(Triple.Index.Right)?.data(Triple.Index.Left)}`);
            output.push(`${node.data(Triple.Index.Right)?.data(Triple.Index.Middle)}`);
            output.push(`${node.data(Triple.Index.Right)?.data(Triple.Index.Right)}`);
        }
    }

    #harvestNodes(
        nodes: Array<Triple<any>>,
        upperNodes: Array<Triple<any>>
    ): Array<Triple<any>> {
        // if upperNodes from last iteration is already root
        if (nodes.length <= 1) {
            return nodes;
        }
        // harvest triples from nodes to upper nodes by 3
        let [left, middle, right] = [null, null, null];
        nodes.forEach((node, index) => {
            if (index % 3 === 0) {
                left = node;
            }
            if (index % 3 === 1) {
                middle = node;
            }
            if (index % 3 === 2) {
                right = node;
            }
            if (left && middle && right) {
                upperNodes.push(new Triple<any>(left, middle, right));
                [left, middle, right] = [null, null, null];
            }
        });
        // if there is additional last (left) or (left, middle) after loop
        if (middle) {
            upperNodes.push(new Triple<any>(left, middle, null));
        } else if (left) {
            upperNodes.push(new Triple<any>(left, null, null));
        }
        // harvest next level nodes
        return this.#harvestNodes(upperNodes, []);
    }

    #createList(size: number): Triple<any> {
        switch (size) {
            case 1:
                return new Triple<T>(undefined, null, null);
            case 2:
                return new Triple<T>(undefined, undefined, null);
            case 3:
                return new Triple<T>(undefined, undefined, undefined);
        }
    }
}

const array = new PerstArray<number>(1);
array.toString();
