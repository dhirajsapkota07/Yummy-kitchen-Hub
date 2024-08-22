import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.util.Set;
import java.util.HashSet;


public class OrderExample {
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JLabel headerLabel;
    private JLabel tableNumberLabel; // New JLabel to display the table number

    private int selectedTableNumber; // New variable to hold the selected table number
    private boolean tableNumberEntered = false;
    private int currentTableNumber = 0; // Track the current table being processed
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea orderTextArea;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    public OrderExample() {
        prepareGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderExample OrderExample = new OrderExample();
        });
    }
    private void prepareGUI() {
        mainFrame = new JFrame("Yummy Kitchen Hub");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(screenSize.width, screenSize.height);
        mainFrame.getContentPane().setBackground(new Color(0, 153, 255));
        mainFrame.setLayout(new BorderLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setBackground(new Color(0, 51, 102));

        headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setFont(new Font(null, Font.BOLD, 25));
        headerLabel.setForeground(Color.white);

        JButton searchButton = new JButton("Search");
        searchField = new JTextField(20);
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        controlPanel.add(searchPanel, BorderLayout.NORTH);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(0, 51, 102));
        refreshButton.setForeground(Color.white);
        controlPanel.add(refreshButton, BorderLayout.SOUTH);

        // Create a panel for the right-hand side
        JPanel orderPanel = new JPanel();
        orderPanel.setBackground(new Color(220, 220, 220));
        orderPanel.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setLeftComponent(controlPanel);
        splitPane.setRightComponent(orderPanel);

        mainFrame.add(splitPane, BorderLayout.CENTER);

        // Create a new JTable instance
        table = new JTable();

        JScrollPane scrollPane = new JScrollPane(table);
        controlPanel.add(scrollPane, BorderLayout.CENTER);

        // Initialize orderTextArea
        orderTextArea = new JTextArea(10, 30);
        orderTextArea.setEditable(false);
        orderTextArea.setFont(new Font(null, Font.PLAIN, 14));

        // Create components for order placement
        JLabel orderLabel = new JLabel("Order Details");
        orderLabel.setFont(new Font(null, Font.BOLD, 20));
        orderLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add components to the order panel
        orderPanel.add(orderLabel, BorderLayout.NORTH);
        orderPanel.add(orderTextArea, BorderLayout.CENTER);

        JButton tableSearchButton = new JButton("Table Status");
        JButton addToCartButton = new JButton("Place Order");
        JButton generateBillButton = new JButton("Generate Bill");
        JButton markAsPaidButton = new JButton("Mark as Paid");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(tableSearchButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(generateBillButton);
        buttonPanel.add(markAsPaidButton); // Add the button to the buttonPanel
        orderPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create a new JTable for displaying orders
        orderTable = new JTable();
        orderTableModel = new DefaultTableModel();
        orderTableModel.addColumn("Food Name");
        orderTableModel.addColumn("Quantity");
        orderTableModel.addColumn("Total Price");

        // Set the model for the orderTable
        orderTable.setModel(orderTableModel);

        // Create a JScrollPane for the orderTable
        JScrollPane orderScrollPane = new JScrollPane(orderTable);

        // Add the JScrollPane to the orderPanel
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        // Initialize the table number label
        tableNumberLabel = new JLabel("Table Number: ");
        tableNumberLabel.setFont(new Font(null, Font.BOLD, 20));
        tableNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        orderPanel.add(tableNumberLabel, BorderLayout.NORTH);

        // Set the font for the orderTable headers
        JTableHeader orderTableHeader = orderTable.getTableHeader();
        orderTableHeader.setFont(new Font(null, Font.BOLD, 18));

        // Set the font for the orderTable rows
        orderTable.setFont(new Font(null, Font.PLAIN, 16));
        orderTable.setRowHeight(30);

        // Set the renderer to center-align the content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        orderTable.setDefaultRenderer(Object.class, centerRenderer);

        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);

        JMenu aboutMenu = new JMenu("About");
        menuBar.add(aboutMenu);

        JMenu backMenu = new JMenu("Back");
        menuBar.add(backMenu);

        JMenu exitMenu = new JMenu("Exit");
        menuBar.add(exitMenu);

        // Create a menu listener



        // Create "OK" button for finishing operations
        JButton okButton = new JButton("Finish Order");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the orderTableModel
                orderTableModel.setRowCount(0);

                // Clear the orderTextArea
                orderTextArea.setText("");

                // Reset operations for the current table
                currentTableNumber = 0;

                // Clear the table number label
                tableNumberLabel.setText("Table Number: ");

                // Display a message to indicate finishing operations
                JOptionPane.showMessageDialog(mainFrame, "Operations finished for the current table.", "Finish", JOptionPane.INFORMATION_MESSAGE);
                // Change the button text to "Add to Cart"
                addToCartButton.setText("Place Order");
            }
        });

        // Add the "OK" button to the buttonPanel
        buttonPanel.add(okButton);


        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        tableSearchButton.addActionListener(e -> {
            displayTableStatusButtons();
        });


        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private Connection establishConnection() {
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/SagarDatabase";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error establishing database connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return connection;
    }

    private void displayTableStatusButtons() {
        // Create a new frame for the table status buttons
        JFrame tableStatusButtonsFrame = new JFrame("Table Status");
        tableStatusButtonsFrame.setSize(600, 500);

        JPanel tableStatusButtonsPanel = new JPanel();
        tableStatusButtonsPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Two columns for table buttons
        tableStatusButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            Connection connection = getConnection();
            String sql = "SELECT DISTINCT table_number FROM Orders"; // Use DISTINCT to get unique table numbers
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            // Create a set to store used table numbers for efficient lookup
            Set<Integer> usedTables = new HashSet<>();

            while (rs.next()) {
                int usedTableNumber = rs.getInt("table_number");
                usedTables.add(usedTableNumber);
            }

            connection.close();

            for (int tableNumber = 1; tableNumber <= 15; tableNumber++) {
                final int finalTableNumber = tableNumber; // Declare as final

                JButton tableButton = new JButton("Table " + finalTableNumber);

                boolean isUsed = usedTables.contains(finalTableNumber); // Check if the table is used

                if (isUsed) {
                    // Display the table button as occupied (red)
                    tableButton.setBackground(Color.RED);
                    tableButton.setForeground(Color.WHITE);
                    tableButton.setFont(new Font(null, Font.BOLD, 20));
                    tableButton.setToolTipText("Table Occupied");
                } else {
                    // Display the table button as available (green)
                    tableButton.setBackground(Color.GREEN);
                    tableButton.setForeground(Color.BLACK);
                    tableButton.setFont(new Font(null, Font.BOLD, 20));
                    tableButton.setToolTipText("Table Available");
                }

                // Add an action listener to the table button
                tableButton.addActionListener(e -> {
                    // Handle the table button click event
                    if (isUsed) {
                        JOptionPane.showMessageDialog(tableStatusButtonsFrame, "Table " + finalTableNumber + " is currently in use.");
                    } else {
                        JOptionPane.showMessageDialog(tableStatusButtonsFrame, "Table " + finalTableNumber + " is available.");
                    }
                });

                tableStatusButtonsPanel.add(tableButton);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving table status.");
        }

        tableStatusButtonsFrame.add(tableStatusButtonsPanel);
        tableStatusButtonsFrame.setLocationRelativeTo(mainFrame); // Display the frame near the main frame
        tableStatusButtonsFrame.setVisible(true);
    }


    private void refreshTable() {
        try {
            showButtonDemo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/SagarDatabase";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    private void showButtonDemo() throws SQLException {
        headerLabel.setText("Yummy Kitchen Hub");
        String[] columnNames = {"ID", "Food Name", "Price"};
        Object[][] data = new Object[100][3];
        Connection connection = null;
        String url = "jdbc:mysql://localhost:3306/SagarDatabase";
        String username = "root";
        String password = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM Food";
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int i = 0;
            while (rs.next()) {
                data[i][0] = rs.getInt("f_id");
                data[i][1] = rs.getString("f_name");
                data[i][2] = rs.getDouble("f_prize");
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error !");
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        tableModel = new DefaultTableModel(data, columnNames);
        table.setModel(tableModel);

        table.getTableHeader().setFont(new Font(null, Font.BOLD, 18));
        table.setFont(new Font(null, Font.PLAIN, 16));
        table.setRowHeight(30);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Set the font for the orderTable headers
        JTableHeader orderTableHeader = orderTable.getTableHeader();
        orderTableHeader.setFont(new Font(null, Font.BOLD, 18));

        // Set the font for the orderTable rows
        orderTable.setFont(new Font(null, Font.PLAIN, 16));
        orderTable.setRowHeight(30);

        // Set the renderer to center-align the content
        orderTable.setDefaultRenderer(Object.class, centerRenderer);
    }
}