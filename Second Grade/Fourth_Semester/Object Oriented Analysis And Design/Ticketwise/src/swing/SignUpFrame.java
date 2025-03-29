
package swing;
import ticket.Customer;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;


public class SignUpFrame extends JFrame {

    private String name;
    private String surname;
    private String email;
    private String phone;
    private String gender;
    private String identity;
    private String birthday;
    private String password;
    private boolean kvkkAgreement;
    public SignUpFrame() {
        setTitle("SIGN UP");
        setSize(800, 600);
        setLocationRelativeTo(null); // Or wherever you want it

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 2, 10, 10)); // 13 rows, 2 columns
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Add labels and text fields for user input
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 18); // Font for labels
        Font labelFont2 = new Font(Font.SANS_SERIF, Font.PLAIN, 16); // Font for labels


        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setFont(labelFont);
        JTextField surnameField = new JTextField();
        panel.add(surnameLabel);
        panel.add(surnameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        JTextField emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(labelFont);
        JTextField phoneField = new JTextField();
        panel.add(phoneLabel);
        panel.add(phoneField);

        // Gender selection as combo box
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Select Your Gender","Male", "Female"});
        genderComboBox.setFont(labelFont2);
        panel.add(genderLabel);
        panel.add(genderComboBox);

        JLabel identityLabel = new JLabel("Identity:");
        identityLabel.setFont(labelFont);
        JTextField identityField = new JTextField();
        panel.add(identityLabel);
        panel.add(identityField);

        // Birthday selection
        JLabel birthdayLabel = new JLabel("Birthday:");
        birthdayLabel.setFont(labelFont);

        // Day, Month, and Year selection as combo boxes
        JPanel birthdayPanel = new JPanel(new GridLayout(1, 3, 5, 5));

        // Day selection as combo box
        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
        dayComboBox.setFont(labelFont2);
        birthdayPanel.add(dayComboBox);

        // Month selection as combo box
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setFont(labelFont2);
        birthdayPanel.add(monthComboBox);

        // Year selection as combo box
        JComboBox<String> yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 100; i--) {
            yearComboBox.addItem(String.valueOf(i));
        }
        yearComboBox.setFont(labelFont2);
        birthdayPanel.add(yearComboBox);

        panel.add(birthdayLabel);
        panel.add(birthdayPanel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        JPasswordField passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        // Checkbox to toggle password visibility
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(labelFont2);
        panel.add(new JLabel()); // empty label for spacing
        panel.add(showPasswordCheckBox);

        // KVKK Agreement checkbox
        JLabel kvkkLabel = new JLabel("KVKK Agreement:");
        kvkkLabel.setFont(labelFont);
        JCheckBox kvkkCheckBox = new JCheckBox();
        kvkkCheckBox.setFont(labelFont2);
        panel.add(kvkkLabel);
        panel.add(kvkkCheckBox);

        // Add submit button
        JButton submitButton = new JButton("Sign Up");
        submitButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20)); // Larger font for the button
        submitButton.setPreferredSize(new Dimension(300, submitButton.getPreferredSize().height)); // Set button width
        submitButton.addActionListener((ActionEvent e) -> {

            //Controlls
            // Check if KVKK agreement is selected.
            if (!kvkkCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "You must agree to the KVKK Agreement before signing up.", "KVKK Agreement Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Handle form submission here
            // You can retrieve the data from the fields like this:
            name = nameField.getText();
            surname = surnameField.getText();
            email = emailField.getText();
            phone = phoneField.getText();
            gender = (String) genderComboBox.getSelectedItem();
            identity = identityField.getText();
            birthday = (String) dayComboBox.getSelectedItem() + " " + (String) monthComboBox.getSelectedItem() + " " + (String) yearComboBox.getSelectedItem();
            password = new String(passwordField.getPassword());
            kvkkAgreement = kvkkCheckBox.isSelected();

            // Name and Surname
            if (nameField.getText().isEmpty() || surnameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both name and surname.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!nameField.getText().matches("[a-zA-Z]+") || !surnameField.getText().matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(this, "Name and surname must contain only letters.", "Invalid Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Identity
            if (identity.length() != 11 || !identity.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Identity must be 11 digits long and consist only of numbers.", "Invalid Identity", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Phone Number
            if (!phone.matches("0\\d{10}")) {
                JOptionPane.showMessageDialog(this, "Phone number must be numeric, start with 0, and be 11 digits long.", "Invalid Phone Number", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gender
            if (gender.equals("Select Your Gender")) {
                JOptionPane.showMessageDialog(this, "Please select your gender type.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mail
            String[] allowedDomains = {"@hotmail.com", "@gmail.com", "@yahoo.com", "@outlook.com"};
            boolean isValidEmail = false;
            for (String domain : allowedDomains) {
                if (email.endsWith(domain)) {
                    isValidEmail = true;
                    break;
                }
            }

            if (!isValidEmail) {
                JOptionPane.showMessageDialog(this, "Email must be from a valid domain (@hotmail.com, @gmail.com, @yahoo.com, @outlook.com).", "Invalid Email", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "You have successfully signed up. Redirecting to login screen...", "Success", JOptionPane.INFORMATION_MESSAGE);

            Customer customer = new Customer(name,surname,email,phone,gender,identity,birthday,password,kvkkAgreement);
            customer.addCustomer(name,surname,email,phone,gender,identity,birthday,password,kvkkAgreement);

            dispose(); // Close the current frame

            // Open the login frame
            LogInFrame loginFrame = new LogInFrame();
            loginFrame.setVisible(true); // Make the login frame visible

        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel to center the button
        buttonPanel.add(submitButton);
        panel.add(new JPanel()); // empty panel for spacing
        panel.add(buttonPanel);

        // Toggle password visibility
        showPasswordCheckBox.addActionListener((ActionEvent e) -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\0' : '*');
        });

        add(panel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame when closed
        setVisible(true);

    }
}
