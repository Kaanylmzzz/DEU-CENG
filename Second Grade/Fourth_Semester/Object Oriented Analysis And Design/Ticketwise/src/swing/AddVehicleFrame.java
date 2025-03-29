package swing;
import ticket.Bus;
import ticket.Plane;
import ticket.Train;
import ticket.Vehicle;

import javax.swing.*;
import java.awt.*;

public class AddVehicleFrame extends JFrame {

    private JTextField licensePlateField;

    private static JTextField registrationNumberField, capacityField;
    private JButton addVehicleButton;

    public static int capacity;
    public static long registrationNumber;


    public AddVehicleFrame() {
        setTitle("Add Vehicle");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        // Set the layout manager for the frame
        setLayout(new BorderLayout());

        // Create a panel to hold form elements
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 50)); // Adjusted for additional row
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60)); // top, left, bottom, right padding

        // Create text fields
        licensePlateField = new JTextField();
        registrationNumberField = new JTextField();
        capacityField = new JTextField();

        // Font settings
        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 18); // Larger and bold font for labels

        // Create button
        addVehicleButton = new JButton("ADD VEHICLE");
        addVehicleButton.setFont(labelFont);

        // Adding components to the form panel
        JLabel labelLicense = new JLabel("License Plate:");
        labelLicense.setFont(labelFont);
        formPanel.add(labelLicense);
        formPanel.add(licensePlateField);

        JLabel labelRegistration = new JLabel("Registration Number:");
        labelRegistration.setFont(labelFont);
        formPanel.add(labelRegistration);
        formPanel.add(registrationNumberField);

        JLabel labelCapacity = new JLabel("Capacity:");
        labelCapacity.setFont(labelFont);
        formPanel.add(labelCapacity);
        formPanel.add(capacityField);

        formPanel.add(new JPanel()); // This spacer panel might be removed or adjusted
        formPanel.add(addVehicleButton);

        // Add the form panel to the frame
        add(formPanel, BorderLayout.CENTER);

        addVehicleButton.addActionListener(e -> {
            if (validateFields()) {
                capacity = Integer.parseInt(capacityField.getText().trim());
                registrationNumber = Long.parseLong(registrationNumberField.getText().trim());

                switch (CompanyLogInFrame.tempCompanyType) {
                    case "Bus":
                        Vehicle bus = new Bus(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), capacity, registrationNumber);
                        if (bus.existVehicle(licensePlateField.getText())) {
                            JOptionPane.showMessageDialog(this, "The vehicle with this license plate is already registered. Please enter a vehicle with a different license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            bus.addVehicle(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), capacity, registrationNumber);
                            JOptionPane.showMessageDialog(this, "Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        break;
                    case "Train":
                        Vehicle train = new Train(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), capacity, registrationNumber);
                        if (train.existVehicle(licensePlateField.getText())) {
                            JOptionPane.showMessageDialog(this, "The vehicle with this license plate is already registered. Please enter a vehicle with a different license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            train.addVehicle(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), capacity, registrationNumber);
                            JOptionPane.showMessageDialog(this, "Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        break;
                    case "Plane": // Note the spelling should be "Plane" unless intentionally spelled differently
                        Vehicle plane = new Plane(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), capacity, registrationNumber);
                        if (plane.existVehicle(licensePlateField.getText())) {
                            JOptionPane.showMessageDialog(this, "The vehicle with this license plate is already registered. Please enter a vehicle with a different license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            plane.addVehicle(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), capacity, registrationNumber);
                            JOptionPane.showMessageDialog(this, "Vehicle added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
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

        // Validate registration number
        String registrationNumberText = registrationNumberField.getText().trim();
        if (registrationNumberText.isEmpty()) {
            errors.append("Registration number must not be empty.\n");
        } else if (registrationNumberText.length() != 10) {
            errors.append("Registration number must be exactly 10 digits.\n");
        } else {
            for (char c : registrationNumberText.toCharArray()) {
                if (!Character.isDigit(c)) {
                    errors.append("Registration number must contain only numeric values.\n");
                    break;
                }
            }
        }

        // Validate capacity
        String capacityText = capacityField.getText().trim();
        if (capacityText.isEmpty()) {
            errors.append("Capacity must not be empty.\n");
        } else {
            try {
                int capacity = Integer.parseInt(capacityText);
                if (capacity < 12 || capacity > 99) {
                    errors.append("Capacity must be between 12 and 99.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Capacity must be an integer.\n");
            }
        }

        // Show error messages if validation fails
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static long getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

}
