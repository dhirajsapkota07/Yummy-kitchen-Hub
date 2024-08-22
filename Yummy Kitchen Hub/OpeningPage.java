import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OpeningPage implements ActionListener {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel lblWelcome, lblYummy, lblKitchenHub;
    private JButton btnGetStarted, btnHome, btnAbout, btnContact;

    public OpeningPage() {
        createUI();
        addComponents();
        setLayout();
        addEventHandlers();
    }

    public void createUI() {
        frame = new JFrame("Welcome to Yummy Kitchen Hub");
        mainPanel = new JPanel();
        lblWelcome = new JLabel("WELCOME");
        lblYummy = new JLabel("Yummy");
        lblKitchenHub = new JLabel("Kitchen Hub");
        btnGetStarted = new JButton("Get Started");
        btnHome = new JButton("Home");
        btnAbout = new JButton("About");
        btnContact = new JButton("Contact");
    }

    public void setLayout() {
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //frame.setSize(screenSize.width, screenSize.height);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setSize(1000,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.black);

        lblWelcome.setBounds(145, 30, 800, 50);
        lblWelcome.setFont(new Font("Geomanist", Font.BOLD, 20));
        lblWelcome.setForeground(Color.BLUE);

        lblYummy.setBounds(140, 50, 800, 100);
        lblYummy.setFont(new Font("Geomanist", Font.BOLD, 30));
        lblYummy.setForeground(Color.BLACK);

        lblKitchenHub.setBounds(130, 100, 800, 100);
        lblKitchenHub.setFont(new Font("Geomanist", Font.BOLD, 40));
        lblKitchenHub.setForeground(Color.BLACK);

        btnGetStarted.setBounds(800, 45, 150, 40);
        btnGetStarted.setFont(new Font("Arial", Font.BOLD, 20));
        btnGetStarted.setFocusPainted(false);
        btnGetStarted.setBackground(Color.GREEN);
        btnGetStarted.setForeground(Color.BLACK);

        btnHome.setBounds(720, 10, 80, 20);
        btnHome.setFont(new Font("Arial", Font.BOLD, 10));
        btnHome.setFocusPainted(false);
        btnHome.setBackground(Color.BLUE);
        btnHome.setForeground(Color.WHITE);

        btnAbout.setBounds(800, 10, 80, 20);
        btnAbout.setFont(new Font("Arial", Font.BOLD, 10));
        btnAbout.setFocusPainted(false);
        btnAbout.setBackground(Color.BLUE);
        btnAbout.setForeground(Color.WHITE);

        btnContact.setBounds(880, 10, 80, 20);
        btnContact.setFont(new Font("Arial", Font.BOLD, 10));
        btnContact.setFocusPainted(false);
        btnContact.setBackground(Color.BLUE);
        btnContact.setForeground(Color.WHITE);

        ImageIcon backgroundImg = new ImageIcon("restaurant.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImg);
        backgroundLabel.setBounds(0, 0, 1000,500);

        mainPanel.add(lblWelcome);
        mainPanel.add(lblYummy);
        mainPanel.add(lblKitchenHub);
        mainPanel.add(btnGetStarted);
        mainPanel.add(btnHome);
        mainPanel.add(btnAbout);
        mainPanel.add(btnContact);
        mainPanel.add(backgroundLabel);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    public void addComponents() {
        mainPanel.add(lblWelcome);
        mainPanel.add(lblYummy);
        mainPanel.add(lblKitchenHub);
        mainPanel.add(btnGetStarted);
        mainPanel.add(btnHome);
        mainPanel.add(btnAbout);
        mainPanel.add(btnContact);
    }

    public void addEventHandlers() {
        btnGetStarted.addActionListener(this);

        // Add a mouse listener to change button color when pointed to it
        btnGetStarted.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGetStarted.setBackground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnGetStarted.setBackground(Color.GREEN);
            }
        });

        btnHome.addActionListener(this);
        btnAbout.addActionListener(this);
        btnContact.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGetStarted) {
           // JOptionPane.showMessageDialog(null, "Get Started Button Clicked!");
            HomePage HomePage = new HomePage();
        } else if (e.getSource() == btnHome) {
            JOptionPane.showMessageDialog(null, "Home Button Clicked!");
            // Handle the action for the Home button
        } else if (e.getSource() == btnAbout) {
            JOptionPane.showMessageDialog(null, "About Button Clicked!");
            // Handle the action for the About button
        } else if (e.getSource() == btnContact) {
            JOptionPane.showMessageDialog(null, "Contact Button Clicked!");
            // Handle the action for the Contact button
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OpeningPage());
    }
}