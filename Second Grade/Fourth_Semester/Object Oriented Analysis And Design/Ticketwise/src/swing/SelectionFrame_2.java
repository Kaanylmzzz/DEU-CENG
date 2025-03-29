package swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionFrame_2 extends JFrame {

    public SelectionFrame_2() {
        setTitle("COMPANY SELECTION");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // To center the frame on screen

        JPanel buttonPanel = new JPanel(); // Create a panel to hold buttons
        buttonPanel.setLayout(new GridLayout(2, 1, 20, 22)); // Grid layout with increased vertical and horizontal gaps
        buttonPanel.setBorder(new EmptyBorder(200, 200, 200, 200)); // Add padding around the panel

        // Create buttons
        JButton signUpButton = new JButton("Company Sign Up");
        JButton logInButton = new JButton("Company Log In");

        // Set custom fonts for buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 22); // Define font settings
        signUpButton.setFont(buttonFont);
        logInButton.setFont(buttonFont);

        // Customize button size by setting preferred size
        signUpButton.setPreferredSize(new Dimension(200, 50));
        logInButton.setPreferredSize(new Dimension(200, 50));

        // Add buttons to the panel
        buttonPanel.add(signUpButton);
        buttonPanel.add(logInButton);

        // Add the panel to the frame
        add(buttonPanel, BorderLayout.CENTER); // Center the panel within the frame

        // Add action listeners to buttons
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Directed to Company Sign Up");
                CompanySignUpFrame companySignUpFrame = new CompanySignUpFrame();
                companySignUpFrame.setVisible(true); // Make the new frame visible
                dispose(); // Close the current frame
            }
        });

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Directed to Company Log In");
                CompanyLogInFrame companyLogInFrame = new CompanyLogInFrame();
                companyLogInFrame.setVisible(true); // Make the new frame visible
                dispose(); // Close the current frame
            }
        });

        setVisible(true);
    }
}
