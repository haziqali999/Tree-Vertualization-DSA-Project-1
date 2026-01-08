package tree;

class AVLTree {
    TreeNode root;

    AVLTree() {
        root = null;
    }

    // Get height of node
    int getHeight(TreeNode node) {
        if (node == null) return 0;
        return node.height;
    }

    // Get balance factor
    int getBalance(TreeNode node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    // Right rotate
    TreeNode rightRotate(TreeNode y) {
        TreeNode x = y.left;
        TreeNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    // Left rotate
    TreeNode leftRotate(TreeNode x) {
        TreeNode y = x.right;
        TreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }

    // Insert node
    void insert(int value) {
        root = insertNode(root, value);
    }

    TreeNode insertNode(TreeNode node, int value) {
        if (node == null) {
            return new TreeNode(value);
        }

        if (value < node.value) {
            node.left = insertNode(node.left, value);
        } else if (value > node.value) {
            node.right = insertNode(node.right, value);
        } else {
            return node; // No duplicates
        }

        // Update height
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        // Get balance factor
        int balance = getBalance(node);

        // 4 cases for rotation
        // Left Left Case
        if (balance > 1 && value < node.left.value) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && value > node.right.value) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && value > node.left.value) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && value < node.right.value) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Count nodes
    int countNodes() {
        return count(root);
    }

    int count(TreeNode node) {
        if (node == null) return 0;
        return 1 + count(node.left) + count(node.right);
    }

    // Get height of tree
    int getTreeHeight() {
        return height(root);
    }

    int height(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }
}