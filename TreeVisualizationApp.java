package tree;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class TreeVisualizationApp extends JFrame {
    private BST bst;
    private AVLTree avl;
    private String currentTreeType;
    private TreePanel treePanel;
    private JTextArea infoArea;
    private JTextField valueField;
    private JLabel statusLabel;
    private JLabel treeTypeLabel;

    public TreeVisualizationApp(String treeType) {
        currentTreeType = treeType;

        if (treeType.equals("BST")) {
            bst = new BST();
            avl = null;
        } else {
            avl = new AVLTree();
            bst = null;
        }

        setupGUI();
        setTitle("Tree Visualization - " + (treeType.equals("BST") ? "Binary Search Tree" : "AVL Tree"));
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setupGUI() {
        // Main panel with border layout
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(45, 45, 55));

        // Top panel - Tree type and controls
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(60, 60, 75));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tree type display
        treeTypeLabel = new JLabel("Current Tree: " +
                (currentTreeType.equals("BST") ? "Binary Search Tree" : "AVL Tree"));
        treeTypeLabel.setFont(new Font("Arial   ", Font.BOLD, 20));
        treeTypeLabel.setForeground(Color.RED);
        treeTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        controlPanel.setBackground(new Color(60, 60, 75));

        // Value input
        JLabel inputLabel = new JLabel("Enter Value:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputLabel.setForeground(Color.RED);

        valueField = new JTextField(10);
        valueField.setFont(new Font("Arial", Font.PLAIN, 14));
        valueField.setHorizontalAlignment(JTextField.CENTER);

        // Buttons
        JButton insertBtn = createButton("Insert", new Color(70, 160, 70));
        JButton deleteBtn = createButton("Delete", new Color(200, 80, 80));
        JButton searchBtn = createButton("Search", new Color(70, 130, 180));
        JButton clearBtn = createButton("Clear Tree", new Color(180, 100, 220));
        JButton randomBtn = createButton("Random Tree", new Color(255, 140, 0));
        JButton switchBtn = createButton("Switch Tree Type", new Color(100, 180, 255));

        // Add action listeners
        insertBtn.addActionListener(e -> insertValue());
        deleteBtn.addActionListener(e -> deleteValue());
        searchBtn.addActionListener(e -> searchValue());
        clearBtn.addActionListener(e -> clearTree());
        randomBtn.addActionListener(e -> generateRandomTree());
        switchBtn.addActionListener(e -> switchTreeType());

        // Enter key listener
        valueField.addActionListener(e -> insertValue());

        // Add components to control panel
        controlPanel.add(inputLabel);
        controlPanel.add(valueField);
        controlPanel.add(insertBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(searchBtn);
        controlPanel.add(randomBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(switchBtn);

        // Add to top panel
        topPanel.add(treeTypeLabel, BorderLayout.WEST);
        topPanel.add(controlPanel, BorderLayout.EAST);

        // Center panel - Tree visualization
        treePanel = new TreePanel();
        JScrollPane treeScroll = new JScrollPane(treePanel);
        treeScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 150), 2),
                "Tree Visualization",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(255, 100, 100)
        ));

        // Right panel - Information
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(50, 50, 65));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setPreferredSize(new Dimension(300, 0));

        JLabel infoTitle = new JLabel("Tree Information", SwingConstants.CENTER);
        infoTitle.setFont(new Font("Arial", Font.BOLD, 18));
        infoTitle.setForeground(new Color(150, 220, 255));
        infoTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setForeground(Color.WHITE);
        infoArea.setBackground(new Color(60, 80, 50));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        updateInfoArea();

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 120), 1));

        infoPanel.add(infoTitle, BorderLayout.NORTH);
        infoPanel.add(infoScroll, BorderLayout.CENTER);

        // Bottom panel - Status
        statusLabel = new JLabel("Ready. Enter a value and click Insert.");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setBackground(new Color(50, 50, 70));
        statusLabel.setOpaque(true);

        // Add all panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(treeScroll, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void insertValue() {
        String input = valueField.getText().trim();

        if (input.isEmpty()) {
            showStatus("Please enter a value.", Color.ORANGE);
            return;
        }

        try {
            int value = Integer.parseInt(input);

            if (currentTreeType.equals("BST")) {
                bst.insert(value);
                showStatus("Value " + value + " inserted into BST.", Color.GREEN);
            } else {
                avl.insert(value);
                showStatus("Value " + value + " inserted into AVL Tree.", Color.GREEN);
            }

            valueField.setText("");
            valueField.requestFocus();
            treePanel.repaint();
            updateInfoArea();

        } catch (NumberFormatException e) {
            showStatus("Invalid input! Please enter a number.", Color.RED);
            valueField.selectAll();
        }
    }

    private void deleteValue() {
        String input = valueField.getText().trim();

        if (input.isEmpty()) {
            showStatus("Please enter a value to delete.", Color.ORANGE);
            return;
        }

        try {
            int value = Integer.parseInt(input);

            if (currentTreeType.equals("BST")) {
                bst.delete(value);
                showStatus("Value " + value + " deleted from BST.", Color.YELLOW);
            } else {
                showStatus("Delete not implemented for AVL in this version.", Color.ORANGE);
            }

            valueField.setText("");
            treePanel.repaint();
            updateInfoArea();

        } catch (NumberFormatException e) {
            showStatus("Invalid input! Please enter a number.", Color.RED);
        }
    }

    private void searchValue() {
        String input = valueField.getText().trim();

        if (input.isEmpty()) {
            showStatus("Please enter a value to search.", Color.ORANGE);
            return;
        }

        try {
            int value = Integer.parseInt(input);
            boolean found;

            if (currentTreeType.equals("BST")) {
                found = bst.search(value);
            } else {
                // Simple search for AVL
                found = searchAVL(avl.root, value);
            }

            if (found) {
                showStatus("Value " + value + " found in the tree!", Color.GREEN);
            } else {
                showStatus("Value " + value + " not found in the tree.", Color.RED);
            }

        } catch (NumberFormatException e) {
            showStatus("Invalid input! Please enter a number.", Color.RED);
        }
    }

    private boolean searchAVL(TreeNode node, int value) {
        if (node == null) return false;
        if (node.value == value) return true;

        if (value < node.value) {
            return searchAVL(node.left, value);
        } else {
            return searchAVL(node.right, value);
        }
    }

    private void clearTree() {
        if (currentTreeType.equals("BST")) {
            bst = new BST();
        } else {
            avl = new AVLTree();
        }

        treePanel.repaint();
        updateInfoArea();
        showStatus("Tree cleared successfully.", Color.YELLOW);
    }

    private void generateRandomTree() {
        if (currentTreeType.equals("BST")) {
            bst = new BST();
            int count = 8 + (int)(Math.random() * 10); // 8-17 nodes

            for (int i = 0; i < count; i++) {
                int value = (int)(Math.random() * 100) + 1;
                bst.insert(value);
            }
            showStatus("Random BST generated with " + count + " nodes.", Color.CYAN);
        } else {
            avl = new AVLTree();
            int count = 8 + (int)(Math.random() * 10); // 8-17 nodes

            for (int i = 0; i < count; i++) {
                int value = (int)(Math.random() * 100) + 1;
                avl.insert(value);
            }
            showStatus("Random AVL Tree generated with " + count + " nodes.", Color.CYAN);
        }

        treePanel.repaint();
        updateInfoArea();
    }

    private void switchTreeType() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Switch tree type? Current tree will be lost.",
                "Switch Tree Type",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose(); // Close current window

            // Open new window with different tree type
            if (currentTreeType.equals("BST")) {
                new TreeVisualizationApp("AVL");
            } else {
                new TreeVisualizationApp("BST");
            }
        }
    }

    private void updateInfoArea() {
        StringBuilder info = new StringBuilder();

        if (currentTreeType.equals("BST")) {
            info.append("Binary Search Tree Info:\n");
            info.append("=======================\n\n");
            info.append("Total Nodes: ").append(bst.countNodes()).append("\n");
            info.append("Tree Height: ").append(bst.getHeight()).append("\n");
            info.append("Root Value: ").append(getRootValue()).append("\n\n");

            info.append("Tree Properties:\n");
            info.append("• Left child < Parent\n");
            info.append("• Right child > Parent\n");
            info.append("• No balancing required\n");

        } else {
            info.append("AVL Tree Info:\n");
            info.append("==============\n\n");
            info.append("Total Nodes: ").append(avl.countNodes()).append("\n");
            info.append("Tree Height: ").append(avl.getTreeHeight()).append("\n");
            info.append("Root Value: ").append(getRootValue()).append("\n\n");

            info.append("Tree Properties:\n");
            info.append("• Self-balancing\n");
            info.append("• Height difference ≤ 1\n");
            info.append("• Faster search times\n");
        }

        info.append("\n\nHow to Use:\n");
        info.append("1. Enter a number in the text box\n");
        info.append("2. Click Insert to add it\n");
        info.append("3. Click Delete to remove it\n");
        info.append("4. Click Search to find it\n");
        info.append("5. Click Random Tree for example\n");

        infoArea.setText(info.toString());
    }

    private String getRootValue() {
        if (currentTreeType.equals("BST") && bst.root != null) {
            return String.valueOf(bst.root.value);
        } else if (currentTreeType.equals("AVL") && avl.root != null) {
            return String.valueOf(avl.root.value);
        }
        return "None";
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    // Tree visualization panel
    class TreePanel extends JPanel {
        private final int NODE_SIZE = 40;
        private final int HORIZONTAL_GAP = 60;
        private final int VERTICAL_GAP = 80;

        TreePanel() {
            setBackground(new Color(30, 30, 40));
            setPreferredSize(new Dimension(800, 600));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Check if tree is empty
            if ((currentTreeType.equals("BST") && bst.root == null) ||
                    (currentTreeType.equals("AVL") && avl.root == null)) {
                drawEmptyMessage(g2d);
                return;
            }

            // Draw the tree
            if (currentTreeType.equals("BST")) {
                drawTree(g2d, bst.root, getWidth() / 2, 50, getWidth() / 4);
            } else {
                drawTree(g2d, avl.root, getWidth() / 2, 50, getWidth() / 4);
            }
        }

        private void drawTree(Graphics2D g2d, TreeNode node, int x, int y, int xOffset) {
            if (node == null) return;

            // Draw connections to children first
            if (node.left != null) {
                int childX = x - xOffset;
                int childY = y + VERTICAL_GAP;
                g2d.setColor(new Color(100, 150, 255, 200));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, y + NODE_SIZE/2, childX, childY - NODE_SIZE/2);
                drawTree(g2d, node.left, childX, childY, xOffset/2);
            }

            if (node.right != null) {
                int childX = x + xOffset;
                int childY = y + VERTICAL_GAP;
                g2d.setColor(new Color(100, 150, 255, 200));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, y + NODE_SIZE/2, childX, childY - NODE_SIZE/2);
                drawTree(g2d, node.right, childX, childY, xOffset/2);
            }

            // Draw  node
            Color nodeColor = currentTreeType.equals("BST") ?
                    new Color(70, 130, 180) : new Color(60, 179, 113);

            // Draw node circle
            g2d.setColor(nodeColor);
            g2d.fillOval(x - NODE_SIZE/2, y - NODE_SIZE/2, NODE_SIZE, NODE_SIZE);

            // Draw node border
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - NODE_SIZE/2, y - NODE_SIZE/2, NODE_SIZE, NODE_SIZE);

            // Draw node value
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String value = String.valueOf(node.value);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(value);
            g2d.drawString(value, x - textWidth/2, y + 5);

            // For AVL tree and show height
            if (currentTreeType.equals("AVL")) {
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.setColor(Color.YELLOW);
                g2d.drawString("h:" + node.height, x - 10, y - NODE_SIZE/2 - 5);
            }
        }

        private void drawEmptyMessage(Graphics2D g2d) {
            g2d.setColor(new Color(200, 200, 255));
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String message = "Tree is Empty";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g2d.drawString(message, x, y);

            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            String instruction = "Enter values to build your " +
                    (currentTreeType.equals("BST") ? "Binary Search Tree" : "AVL Tree");
            fm = g2d.getFontMetrics();
            x = (getWidth() - fm.stringWidth(instruction)) / 2;
            y += 40;
            g2d.drawString(instruction, x, y);
        }
    }
}
