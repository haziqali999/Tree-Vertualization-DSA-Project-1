package tree;

class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;
    int height;

    TreeNode(int value) {
        this.value = value;
        this.height = 1;
        left = right = null;
    }
}