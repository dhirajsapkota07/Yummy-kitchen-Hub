import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLogin implements ActionListener {
    private JFrame frame;
    private JPanel panel,loginPanel;
    private JLabel lblUsername, lblPassword, lblHeader;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public AdminLogin() {
        createUI();
        addEventHandlers();
        addComponents();
        setLayout();
    }

    public void createUI() {
        frame = new JFrame("Yummy Kitchen Hub");
        panel = new JPanel();
        loginPanel = new JPanel();
        lblUsername = new JLabel("Username: ");
        txtUsername = new JTextField(15);
        lblPassword = new JLabel("Password: ");
        lblHeader = new JLabel("ADMIN LOGIN PAGE");
        txtPassword = new JPasswordField(15);
        btnLogin = new JButton("Login");
    }

    public void setLayout() {
        panel.setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        panel.setBackground(new Color(0, 51, 102));

        // Set layout for loginPanel
        loginPanel.setLayout(null);
        loginPanel.setBounds((screenSize.width - 400) / 2, (screenSize.height - 300) / 2, 400, 350); // Centered panel
        loginPanel.setBackground(Color.WHITE);

        // Styling for header label
        lblHeader.setBounds((loginPanel.getWidth() - 300) / 2, 20, 300, 50); // Centered in loginPanel
        lblHeader.setFont(new Font("Arial", Font.BOLD, 28));
        lblHeader.setForeground(new Color(44, 62, 80)); // Dark blue-gray text color

        // Position components in the loginPanel
        int x = 50, y = 80, width = 300, height = 30, spacing = 40;
        lblUsername.setBounds(x, y, width, height);
        txtUsername.setBounds(x, y + spacing, width, height);
        lblPassword.setBounds(x, y + 2 * spacing, width, height);
        txtPassword.setBounds(x, y + 3 * spacing, width, height);
        btnLogin.setBounds(150, y + 5 * spacing, 100, 30);

        // Styling adjustments for login panel components
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);

        // Adding components to panels and frame
        loginPanel.add(lblHeader); // Add the header to the login panel
        loginPanel.add(lblUsername);
        loginPanel.add(txtUsername);
        loginPanel.add(lblPassword);
        loginPanel.add(txtPassword);
        loginPanel.add(btnLogin);
        panel.add(loginPanel);
        frame.add(panel);


        JLabel headerLabel = new JLabel();
        headerLabel = new JLabel("ADMIN LOGIN PAGE", JLabel.CENTER);
        headerLabel.setFont(new Font(null, Font.BOLD, 30));
        headerLabel.setForeground(Color.white);
        frame.getContentPane().setBackground(new Color(0, 153, 255));
        frame.add(headerLabel, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

    }

    public void addComponents() {
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        frame.add(panel);
        panel.add(lblHeader);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu aboutMenu = new JMenu("About");
        menuBar.add(aboutMenu);

        JMenu backMenu = new JMenu("Back");
        menuBar.add(backMenu);

        JMenu exitMenu = new JMenu("Exit");
        menuBar.add(exitMenu);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setActionCommand("About");
        aboutMenuItem.addActionListener(this);
        aboutMenu.add(aboutMenuItem);

        JMenuItem backMenuItem = new JMenuItem("Back");
        backMenuItem.setActionCommand("Back");
        backMenuItem.addActionListener(this);
        backMenu.add(backMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setActionCommand("Exit");
        exitMenuItem.addActionListener(this);
        exitMenu.add(exitMenuItem);
    }

    public void addEventHandlers() {
        btnLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "About":
                JOptionPane.showMessageDialog(frame, "About Button clicked!");
                break;
            case "Back":
                //JOptionPane.showMessageDialog(frame, "Back Button clicked!");
                // Handle the back action here
                frame.dispose();
                HomePage homePage = new HomePage();
                break;
            case "Exit":
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
            default:
                performLogin();
                break;
        }
    }

    private void performLogin() {
        String url = "jdbc:mysql://localhost:3306/SagarDatabase";
        String username = "root";
        String password = "";

        String inputUsername = txtUsername.getText();
        String inputPassword = new String(txtPassword.getPassword());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM Admin WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, inputUsername);
            preparedStatement.setString(2, inputPassword);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                AdminPage AdminPage = new AdminPage();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password!");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Connection Error!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLogin());
    }
}