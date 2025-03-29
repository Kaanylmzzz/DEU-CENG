package swing;

import ticket.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

public class CompanyLogInFrame extends JFrame {
    public static String tempCompanyName;
    public static String tempCompanyType;
    public CompanyLogInFrame() {
        setTitle("LOG IN");
        setSize(600, 400);
        setLocationRelativeTo(null); // Or wherever you want it

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, -20, 50)); // 4 rows, 2 columns
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        // Add labels and text fields for user input
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 18); // Larger and bold font for labels
        JLabel nameLabel = new JLabel("Company Name:");
        nameLabel.setFont(labelFont);
        JTextField nameField = new JTextField();
        nameField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18)); // Reduced font size for text fields

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

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JPanel()); // Empty panel for spacing
        panel.add(showPasswordPanel); // Add the panel containing the checkbox

        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20)); // Larger and bold font for button

        loginButton.addActionListener((ActionEvent e) -> {
            // Handle login button click event here
            String company_name = nameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars); // Convert char array to String

            Company company = new Company(company_name,password);
            if (company.loginCompany(company_name,password)) {
                JOptionPane.showMessageDialog(this, "Log in succesfull. Welcome, " + company_name + " company");
                tempCompanyName = company_name;
                tempCompanyType = company.findCompanyType(company_name);
                AdminFrame adminFrame = new AdminFrame();
                adminFrame.setVisible(true); // Make the selection frame visible
                dispose(); // Close the current frame
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect company name or password. Please try again.");
            }

        });
        panel.add(Box.createHorizontalStrut(10));
        panel.add(loginButton);

        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static String getTempCompanyName() {
        return tempCompanyName;
    }

    public static void setTempCompanyName(String tempCompanyName) {
        CompanyLogInFrame.tempCompanyName = tempCompanyName;
    }

    public static String getTempCompanyType() {
        return tempCompanyType;
    }

    public static void setTempCompanyType(String tempCompanyType) {
        CompanyLogInFrame.tempCompanyType = tempCompanyType;
    }
}
