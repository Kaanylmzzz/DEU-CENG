package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Swing {

    public static void createAndShowGUI() {
        // Create the frame
        JFrame jf = new JFrame();
        jf.setTitle("TICKETWISE");
        jf.setSize(1000, 600);
        jf.setLocationRelativeTo(null); // Center the frame

        // Create and configure the welcome label
        JLabel welcomeLabel = createWelcomeLabel();

        // Create panel for the welcome label
        JPanel welcomePanel = new JPanel();
        welcomePanel.add(welcomeLabel);


        // Create panel for the menu options
        JPanel menuPanel = createMenuPanel();

        // Create panel for the login button
        JPanel loginPanel = createLoginPanel();

        // Add panels to the frame
        jf.add(welcomePanel, BorderLayout.NORTH);
        jf.add(menuPanel, BorderLayout.CENTER);
        jf.add(loginPanel, BorderLayout.SOUTH);

        jf.setResizable(false);
        jf.setVisible(true);
    }

    private static JLabel createWelcomeLabel() {
        JLabel welcomeLabel = new JLabel("<html><br><font color='black'>WELCOME</font> <font color='#8B0000'>TICKETWISE</font></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
        return welcomeLabel;
    }

    private static JPanel createMenuPanel() {
        // Create panel for the menu options
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 70)); // Using FlowLayout

        // Create buttons for menu options with increased font size
        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size
        signUpButton.setPreferredSize(new Dimension(250, 60)); // Increase button size

        signUpButton.addActionListener((ActionEvent e) -> {
            // When signUpButton is clicked, create and show the SignUpFrame
            new SignUpFrame(); // Pass parentFrame to SignUpFrame constructor
        });

        JButton continueButton = new JButton("CONTINUE WITHOUT SIGN UP");
        continueButton.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size
        continueButton.setPreferredSize(new Dimension(350, 60)); // Increase button size

        continueButton.addActionListener((ActionEvent e) -> {
            // When signUpButton is clicked, create and show the SignUpFrame
            new SelectionFrame(); // Pass parentFrame to SignUpFrame constructor
        });

        JButton companyButton = new JButton("COMPANY");
        companyButton.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size
        companyButton.setPreferredSize(new Dimension(250, 60)); // Increase button size

        companyButton.addActionListener((ActionEvent e) -> {
            // When signUpButton is clicked, create and show the SignUpFrame
            new SelectionFrame_2(); // Pass parentFrame to SignUpFrame constructor
        });

        // Add buttons to the menu panel
        menuPanel.add(signUpButton);
        menuPanel.add(continueButton);
        menuPanel.add(companyButton);


        return menuPanel;
    }

    private static JPanel createLoginPanel() {
        // Create panel for the login button
        JPanel loginPanel = new JPanel();
        JButton loginButton = new JButton("LOG IN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 22)); // Increase font size
        loginButton.setPreferredSize(new Dimension(350, 60)); // Increase button size
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Centering the button horizontally
        loginPanel.add(loginButton);

        loginPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 150, 0));

        loginButton.addActionListener((ActionEvent e) -> {
            // When signUpButton is clicked, create and show the SignUpFrame
            new LogInFrame(); // Pass parentFrame to SignUpFrame constructor
        });
        return loginPanel;
    }
}