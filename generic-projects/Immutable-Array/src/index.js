var __classPrivateFieldGet = (this && this.__classPrivateFieldGet) || function (receiver, state, kind, f) {
    if (kind === "a" && !f) throw new TypeError("Private accessor was defined without a getter");
    if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver)) throw new TypeError("Cannot read private member from an object whose class did not declare it");
    return kind === "m" ? f : kind === "a" ? f.call(receiver) : f ? f.value : state.get(receiver);
};
var __classPrivateFieldSet = (this && this.__classPrivateFieldSet) || function (receiver, state, value, kind, f) {
    if (kind === "m") throw new TypeError("Private method is not writable");
    if (kind === "a" && !f) throw new TypeError("Private accessor was defined without a setter");
    if (typeof state === "function" ? receiver !== state || !f : !state.has(receiver)) throw new TypeError("Cannot write private member to an object whose class did not declare it");
    return (kind === "a" ? f.call(receiver, value) : f ? f.value = value : state.set(receiver, value)), value;
};
var _Triple_data, _PerstArray_root;
var Triple = /** @class */ (function () {
    function Triple(left, middle, right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
        _Triple_data.set(this, new Array(3));
        __classPrivateFieldGet(this, _Triple_data, "f")[0] = left;
        __classPrivateFieldGet(this, _Triple_data, "f")[1] = middle;
        __classPrivateFieldGet(this, _Triple_data, "f")[2] = right;
    }
    Triple.prototype.data = function (index) {
        if (index >= 0 && index <= 2) {
            return __classPrivateFieldGet(this, _Triple_data, "f")[index];
        }
        else {
            throw "Index out of range!";
        }
    };
    Triple.prototype.isBranch = function () {
        return typeof __classPrivateFieldGet(this, _Triple_data, "f")[0] === "object";
    };
    return Triple;
}());
_Triple_data = new WeakMap();
(function (Triple) {
    var Index;
    (function (Index) {
        Index[Index["Left"] = 0] = "Left";
        Index[Index["Middle"] = 1] = "Middle";
        Index[Index["Right"] = 2] = "Right";
    })(Index = Triple.Index || (Triple.Index = {}));
})(Triple || (Triple = {}));
var PerstArray = /** @class */ (function () {
    function PerstArray(size) {
        _PerstArray_root.set(this, null);
        var listsCount = Math.ceil(size / 3);
        var lists = [];
        var iterSize = size;
        for (var i = 0; i < listsCount; i++) {
            if (iterSize / 3 >= 1) {
                lists.push(this.createList(3));
            }
            else {
                lists.push(this.createList(iterSize));
            }
            iterSize -= 3;
        }
        __classPrivateFieldSet(this, _PerstArray_root, this.harvestNodes(lists, [])[0], "f");
        console.log(__classPrivateFieldGet(this, _PerstArray_root, "f"));
    }
    PerstArray.prototype.printTreeStruct = function (node) {
        if (node.isBranch()) {
            var left = node.data(Triple.Index.Left);
            var middle = node.data(Triple.Index.Middle);
            var right = node.data(Triple.Index.Right);
        }
    };
    PerstArray.prototype.harvestNodes = function (nodes, upperNodes) {
        // if upperNodes from last iteration is already root
        if (nodes.length <= 1) {
            return nodes;
        }
        // harvest triples from nodes to upper nodes by 3
        var _a = [null, null, null], left = _a[0], middle = _a[1], right = _a[2];
        nodes.forEach(function (node, index) {
            var _a;
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
                upperNodes.push(new Triple(left, middle, right));
                _a = [null, null, null], left = _a[0], middle = _a[1], right = _a[2];
            }
        });
        // if there is additional last (left) or (left, middle) after loop
        if (middle) {
            upperNodes.push(new Triple(left, middle, null));
        }
        else if (left) {
            upperNodes.push(new Triple(left, null, null));
        }
        // harvest next level nodes
        return this.harvestNodes(upperNodes, []);
    };
    PerstArray.prototype.createList = function (size) {
        switch (size) {
            case 1:
                return new Triple(undefined, null, null);
            case 2:
                return new Triple(undefined, undefined, null);
            case 3:
                return new Triple(undefined, undefined, undefined);
        }
    };
    return PerstArray;
}());
_PerstArray_root = new WeakMap();
var array = new PerstArray(58);
