package swing;

import ticket.Company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CompanySignUpFrame extends JFrame {

    private String name;
    private String phone;
    private String type;
    private String password;
    public CompanySignUpFrame() {

        setTitle("COMPANY SIGN UP");
        setSize(800, 600);
        setLocationRelativeTo(null); // Or wherever you want it

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 50, 35, 50); // Padding and spacing between rows

        // Add labels and text fields for user input
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 20); // Font for labels
        Font labelFont2 = new Font(Font.SANS_SERIF, Font.PLAIN, 16); // Font for labels

        // Company Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel nameLabel = new JLabel("Company Name:");
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20); // Adjust width here
        nameField.setPreferredSize(new Dimension(200, 30)); // Set field size
        panel.add(nameField, gbc);

        // Company Phone Number
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel phoneLabel = new JLabel("Company Phone Number:");
        phoneLabel.setFont(labelFont);
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20); // Adjust width here
        phoneField.setPreferredSize(new Dimension(200, 30)); // Set field size
        panel.add(phoneField, gbc);

        // Company Vehicle Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel typeLabel = new JLabel("Company Vehicle Type:");
        typeLabel.setFont(labelFont);
        panel.add(typeLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Select Type","Bus", "Train", "Plane"});
        typeComboBox.setFont(labelFont2);
        panel.add(typeComboBox, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20); // Adjust width here
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set field size
        panel.add(passwordField, gbc);

        // Checkbox to toggle password visibility
        gbc.gridx = 1;
        gbc.gridy = 4;
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(labelFont2);
        panel.add(showPasswordCheckBox, gbc);

        // Add submit button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submitButton = new JButton("Sign Up");
        submitButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22)); // Larger font for the button
        submitButton.setPreferredSize(new Dimension(200, 50)); // Set button size
        panel.add(submitButton, gbc);

        submitButton.addActionListener((ActionEvent e) -> {

            // Handle form submission here
            // You can retrieve the data from the fields like this:
            name = nameField.getText();
            phone = phoneField.getText();
            type = (String) typeComboBox.getSelectedItem();
            password = new String(passwordField.getPassword());

            // Name and Surname
            if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Company name.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Phone Number
            if (!phone.matches("0\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone number must be numeric, start with 0, and be 11 digits long.", "Invalid Phone Number", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Type
            if (type.equals("Select Type")) {
                JOptionPane.showMessageDialog(this, "Please select a company vehicle type.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Password
            boolean hasUppercase = !password.equals(password.toLowerCase());
            boolean hasLowercase = !password.equals(password.toUpperCase());
            boolean hasDigit = password.matches(".*\\d.*");

            if (!(hasUppercase && hasLowercase && hasDigit)) {
                JOptionPane.showMessageDialog(this, "Password must contain at least one uppercase letter, one lowercase letter, and one digit.", "Invalid Password", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Company has successfully signed up. Redirecting to login screen...", "Success", JOptionPane.INFORMATION_MESSAGE);


            Company company = new Company(name,phone,type,password);
            company.addCompany(name,phone,type,password);

            dispose(); // Close the current frame

            // Open the login frame
            CompanyLogInFrame companyLogInFrame = new CompanyLogInFrame();
            companyLogInFrame.setVisible(true); // Make the login frame visible

        });

        // Toggle password visibility
        showPasswordCheckBox.addActionListener((ActionEvent e) -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\0' : '*');
        });



        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame when closed
        setVisible(true);
    }

}
