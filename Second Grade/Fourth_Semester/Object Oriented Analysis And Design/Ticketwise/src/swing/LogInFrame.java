package swing;

import ticket.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class LogInFrame extends JFrame {
    public LogInFrame() {
        setTitle("LOG IN");
        setSize(600, 400);
        setLocationRelativeTo(null); // Or wherever you want it

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, -20, 50)); // 4 rows, 2 columns
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        // Add labels and text fields for user input
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 18); // Larger and bold font for labels
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        JTextField emailField = new JTextField();
        emailField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18)); // Reduced font size for text fields

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18)); // Reduced font size for password field

        JPanel showPasswordPanel = new JPanel(); // Panel for Show Password checkbox
        showPasswordPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Left align the checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        showPasswordCheckBox.addItemListener((ItemEvent e) -> {
            int eventType = e.getStateChange();
            if (eventType == ItemEvent.SELECTED) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('\u25CF'); // Hide password
            }
        });
        showPasswordPanel.add(showPasswordCheckBox);

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JPanel()); // Empty panel for spacing
        panel.add(showPasswordPanel); // Add the panel containing the checkbox

        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20)); // Larger and bold font for button

        loginButton.addActionListener((ActionEvent e) -> {
            // Handle login button click event here
            String email = emailField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars); // Convert char array to String

            // Example: Check if email and password are valid
            Customer customer = new Customer(email,password);
            if (customer.login(email,password)) {
                JOptionPane.showMessageDialog(this, "Log in succesfull. Welcome, " + customer.findCustomerName(email) + " " + customer.findCustomerSurname(email));
                SelectionFrame selectionFrame = new SelectionFrame();
                selectionFrame.setVisible(true); // Make the selection frame visible
                dispose(); // Close the current frame
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect email or password. Please try again.");
            }
        });
        panel.add(Box.createHorizontalStrut(10));
        panel.add(loginButton);

        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
