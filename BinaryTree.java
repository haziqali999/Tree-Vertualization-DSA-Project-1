package tree;


class BST {
    TreeNode root;

    BST() {
        root = null;
    }

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
        }

        return node;
    }


    void delete(int value) {
        root = deleteNode(root, value);
    }

    TreeNode deleteNode(TreeNode node, int value) {
        if (node == null) return null;

        if (value < node.value) {
            node.left = deleteNode(node.left, value);
        } else if (value > node.value) {
            node.right = deleteNode(node.right, value);
        } else {
            // Found the node to delete
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Node has two children
            TreeNode temp = findMin(node.right);
            node.value = temp.value;
            node.right = deleteNode(node.right, temp.value);
        }

        return node;
    }

    TreeNode findMin(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Simple search
    boolean search(int value) {
        return searchNode(root, value);
    }

    boolean searchNode(TreeNode node, int value) {
        if (node == null) return false;
        if (node.value == value) return true;

        if (value < node.value) {
            return searchNode(node.left, value);
        } else {
            return searchNode(node.right, value);
        }
    }

    // Count nodes
    int countNodes() {
        return count(root);
    }

    int count(TreeNode node) {
        if (node == null) return 0;
        return 1 + count(node.left) + count(node.right);
    }

    // Get height
    int getHeight() {
        return height(root);
    }

    int height(TreeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }
}