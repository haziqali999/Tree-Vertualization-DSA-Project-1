package tree;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ask user which tree they want
        String[] treeTypes = {"Binary Search Tree", "AVL Tree"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose a tree type to visualize:",
                "Tree Visualizer",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                treeTypes,
                treeTypes[0]
        );

        // Start the appropriate tree app
        if (choice == 0) {
            new TreeVisualizationApp("BST");  // Binary Search Tree
        } else if (choice == 1) {
            new TreeVisualizationApp("AVL");  // AVL Tree
        }
    }
}