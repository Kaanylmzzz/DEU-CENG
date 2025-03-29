package swing;
import ticket.Bus;
import ticket.Plane;
import ticket.Train;
import ticket.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class RemoveVehicleFrame extends JFrame {

    private JTextField licensePlateField;
    private JButton removeVehicleButton;

    public RemoveVehicleFrame() {
        setTitle("Remove Vehicle");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        // Set the layout manager for the frame
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60)); // Padding around the panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0); // Margin between components

        // Create text fields
        licensePlateField = new JTextField();
        licensePlateField.setPreferredSize(new Dimension(60, 40)); // More likely to be respected in GridBagLayout

        // Font settings
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);

        // Create button
        removeVehicleButton = new JButton("REMOVE VEHICLE");
        removeVehicleButton.setFont(labelFont);
        removeVehicleButton.setPreferredSize(new Dimension(10, 80)); // Set the preferred size of the button

        // Adding components to the form panel
        JLabel labelLicense = new JLabel("Type the license plate to remove the vehicle");
        labelLicense.setFont(labelFont);
        formPanel.add(labelLicense, gbc);
        formPanel.add(licensePlateField, gbc);

        formPanel.add(new JPanel(), gbc); // Spacer panel
        formPanel.add(removeVehicleButton, gbc);


        // Add the form panel to the frame
        add(formPanel, BorderLayout.CENTER);


        removeVehicleButton.addActionListener(e -> {
            if (validateFields()) {

                int capacity = AddVehicleFrame.getCapacity();
                long registrationNumber = AddVehicleFrame.getRegistrationNumber();
                switch (CompanyLogInFrame.tempCompanyType) {
                    case "Bus":
                        Vehicle bus = new Bus(CompanyLogInFrame.getTempCompanyName(),CompanyLogInFrame.getTempCompanyType(),licensePlateField.getText(),capacity, registrationNumber);
                        if(bus.existVehicle(licensePlateField.getText())) {
                            bus.removeVehicle(licensePlateField.getText());
                            JOptionPane.showMessageDialog(this, "Vehicle removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "There is no vehicle belonging to this company with this license plate. Please enter a valid license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Train":
                        Vehicle train = new Train(CompanyLogInFrame.getTempCompanyName(),CompanyLogInFrame.getTempCompanyType(),licensePlateField.getText(),capacity, registrationNumber);
                        if(train.existVehicle(licensePlateField.getText()))
                        {
                            train.removeVehicle(licensePlateField.getText());
                            JOptionPane.showMessageDialog(this, "Vehicle removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "There is no vehicle belonging to this company with this license plate. Please enter a valid license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Plane": // Note the spelling should be "Plane" unless intentionally spelled differently
                        Vehicle plane = new Plane(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(),capacity,registrationNumber);
                        if(plane.existVehicle(licensePlateField.getText()))
                        {
                            plane.removeVehicle(licensePlateField.getText());
                            JOptionPane.showMessageDialog(this, "Vehicle removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this, "There is no vehicle belonging to this company with this license plate. Please enter a valid license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                        }

            }
        });

    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        // Validate license plate
        if (licensePlateField.getText().trim().isEmpty()) {
            errors.append("License plate must not be empty.\n");
        }

        // Show error messages if validation fails
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;

    }
}
