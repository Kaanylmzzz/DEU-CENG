package swing;
import ticket.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTripFrame extends JFrame {

    private JComboBox<String> originField, destinationField, monthComboBox,dayComboBox,hourComboBox,minuteComboBox;
    private JComboBox<Integer> yearComboBox ;
    private JTextField licensePlateField, travelHoursField, costField;
    private JRadioButton wifiYesButton, wifiNoButton, cateringYesButton, cateringNoButton;
    private ButtonGroup wifiGroup, cateringGroup;
    private JButton addButton;

    public AddTripFrame() {
        setTitle("Add Trip");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 1, 20, 1);
        gbc.anchor = GridBagConstraints.WEST;

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);  // Font for all text components
        Font font1 = new Font(Font.SANS_SERIF, Font.BOLD, 22);

        // Set up text fields and spinners
        licensePlateField = new JTextField(11);
        licensePlateField.setFont(font);
        travelHoursField = new JTextField(11);
        travelHoursField.setFont(font);
        costField = new JTextField(11);
        costField.setFont(font);

        // Setup JComboBox for cities
        String[] cities = {"Select City", "İstanbul", "İzmir", "Aydın", "Muğla", "Eskişehir"};
        originField = new JComboBox<>(cities);
        originField.setFont(font);
        destinationField = new JComboBox<>(cities);
        destinationField.setFont(font);

        // Setup for Wi-Fi and Catering radio buttons
        wifiYesButton = new JRadioButton("Yes");
        wifiYesButton.setFont(font);
        wifiNoButton = new JRadioButton("No");
        wifiNoButton.setFont(font);
        wifiGroup = new ButtonGroup();
        wifiGroup.add(wifiYesButton);
        wifiGroup.add(wifiNoButton);

        // Setup date components
        dayComboBox = new JComboBox<>(generateDays());
        monthComboBox = new JComboBox<>(generateMonths());
        yearComboBox = new JComboBox<>(generateYears());

        // Setup time components
        hourComboBox = new JComboBox<>(generateHours());
        minuteComboBox = new JComboBox<>(generateMinutes());

        cateringYesButton = new JRadioButton("Yes");
        cateringYesButton.setFont(font);
        cateringNoButton = new JRadioButton("No");
        cateringNoButton.setFont(font);
        cateringGroup = new ButtonGroup();
        cateringGroup.add(cateringYesButton);
        cateringGroup.add(cateringNoButton);


        int row = 1;
        addField(formPanel, gbc, "License Plate:", licensePlateField, row++, 0);
        addField(formPanel, gbc, "Origin:", originField, row++, 0);
        addField(formPanel, gbc, "Destination:", destinationField, row++, 1);
        addField(formPanel, gbc, "Travel Time:", travelHoursField, row++, 0);
        addField(formPanel, gbc, "Cost:", costField, row++, 1);
        addDateField(formPanel, gbc, "Date:", dayComboBox, monthComboBox, yearComboBox, row++,0);
        addTimeField(formPanel, gbc, "Departure Time:", hourComboBox, minuteComboBox, row++,1);
        addRadioField(formPanel, gbc, "Wi-Fi:", wifiYesButton, wifiNoButton, row++, 0);
        addRadioField(formPanel, gbc, "Catering:", cateringYesButton, cateringNoButton, row++, 1);

        // Add button
        addButton = new JButton("ADD TRIP");
        addButton.setFont(font1);
        addButton.setPreferredSize(new Dimension(250, 60)); // Set size as requested
        gbc.gridwidth = 20;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER; // Center the button
        formPanel.add(addButton, gbc);


        addButton.addActionListener(e -> {
            if (validateFields()) {
                String date = dayComboBox.getSelectedItem().toString() + "-" + monthComboBox.getSelectedItem().toString()+ "-" + yearComboBox.getSelectedItem();
                String time = hourComboBox.getSelectedItem().toString() + ":" + minuteComboBox.getSelectedItem().toString();
                Trip vehicleTrip = new Trip(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(), originField.getSelectedItem().toString(),destinationField.getSelectedItem().toString(),date,time,travelHoursField.getText(),wifiGroup.getSelection().isSelected(),cateringGroup.getSelection().isSelected(),Integer.parseInt(costField.getText()));
                if(vehicleTrip.existVehicle(CompanyLogInFrame.getTempCompanyName(),licensePlateField.getText()))
                {
                    if (vehicleTrip.existTrip(licensePlateField.getText(),originField.getSelectedItem().toString(),destinationField.getSelectedItem().toString(),date,time)) {
                        JOptionPane.showMessageDialog(this, "The vehicle with this license plate is already registered. Please enter a vehicle with a different license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        vehicleTrip.addTrip(CompanyLogInFrame.getTempCompanyName(), CompanyLogInFrame.getTempCompanyType(), licensePlateField.getText(),originField.getSelectedItem().toString(),destinationField.getSelectedItem().toString(),date,time,travelHoursField.getText(),wifiGroup.getSelection().isSelected(),cateringGroup.getSelection().isSelected(),Integer.parseInt(costField.getText()));
                        int addedTripNo = vehicleTrip.findTripNo(CompanyLogInFrame.getTempCompanyName(),licensePlateField.getText(), originField.getSelectedItem().toString(), destinationField.getSelectedItem().toString(), date, time);
                        Seat seat = new Seat(addedTripNo);
                        seat.whenAddTrip(addedTripNo);
                        JOptionPane.showMessageDialog(this, "Trip added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "This company does not have a vehicle with this license plate.", "Error", JOptionPane.INFORMATION_MESSAGE);

                }

            }
        });

        add(formPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame when closed
        setVisible(true);
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();
        if (licensePlateField.getText().trim().isEmpty()) {
            errors.append("License plate must not be empty.\n");
        }
        if (originField.getSelectedItem().equals("Select City")) {
            errors.append("Please select an origin city.\n");
        }
        if (destinationField.getSelectedItem().equals("Select City")) {
            errors.append("Please select a destination city.\n");
        }
        if (originField.getSelectedItem().equals(destinationField.getSelectedItem())) {
            errors.append("Please choose different cities.\n");
        }
        if (!wifiYesButton.isSelected() && !wifiNoButton.isSelected()) {
            errors.append("Please select a Wi-Fi option.\n");
        }
        if (!cateringYesButton.isSelected() && !cateringNoButton.isSelected()) {
            errors.append("Please select a catering option.\n");
        }
        try {
            int cost = Integer.parseInt(costField.getText().trim());
            if (cost < 100) {
                errors.append("Cost must be greater than 99.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Cost must be a valid integer.\n");
        }
        try {
            String[] parts = travelHoursField.getText().split("\\.");
            if (parts.length != 2 || Integer.parseInt(parts[0]) < 0 || Integer.parseInt(parts[1]) < 0 || Integer.parseInt(parts[1]) >= 60) {
                errors.append("Travel time must be in the format (HH.mm), with minutes less than 60.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Travel time must be numeric.\n");
        }
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row, int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 2 * (col % 2);
        gbc.gridy = row / 2;
        panel.add(label, gbc);
        gbc.gridx = 2 * (col % 2) + 1;
        panel.add(field, gbc);
    }

    private void addRadioField(JPanel panel, GridBagConstraints gbc, String labelText, JRadioButton yesButton, JRadioButton noButton, int row, int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 2 * (col % 2);
        gbc.gridy = row / 2;
        panel.add(label, gbc);
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(yesButton);
        radioPanel.add(noButton);
        gbc.gridx = 2 * (col % 2) + 1;
        panel.add(radioPanel, gbc);
    }

    private String[] generateDays() {
        String[] days = new String[31];
        for (int i = 1 ;i <= 31; i++) {
            if (i < 10) {
                days[i-1] = "0" + (i);
            } else {
                days[i-1] = String.valueOf(i);
            }
        }
        return days;
    }

    private String[] generateMonths() {
        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                months[i-1] = "0" + (i );
            } else {
                months[i-1] = String.valueOf(i );
            }
        }
        return months;
    }

    private Integer[] generateYears() {
        Integer[] years = new Integer[25];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 25; i++) {
            years[i] = currentYear + i;
        }
        return years;
    }

    private String[] generateHours() {
        String[] hours = new String[24];
        for (int i = 1; i <= 24; i++) {
            if (i < 10) {
                hours[i-1] = "0" + i;
            } else {
                hours[i-1] = String.valueOf(i);
            }
        }
        return hours;
    }

    private String[] generateMinutes() {
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes[i] = "0" + (i);
            } else {
                minutes[i] = String.valueOf(i);
            }
        }
        return minutes;
    }
    private void addDateField(JPanel panel, GridBagConstraints gbc, String labelText, JComboBox day, JComboBox month, JComboBox year, int row , int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 2 * (col % 2);
        gbc.gridy = row / 2;
        panel.add(label, gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(day);
        datePanel.add(month);
        datePanel.add(year);
        gbc.gridx = 2 * (col % 2) + 1;
        panel.add(datePanel, gbc);
    }

    private void addTimeField(JPanel panel, GridBagConstraints gbc, String labelText, JComboBox hour, JComboBox minute, int row , int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 2 * (col % 2);
        gbc.gridy = row / 2;
        panel.add(label, gbc);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.add(hour);
        timePanel.add(minute);
        gbc.gridx = 2 * (col % 2) + 1;
        panel.add(timePanel, gbc);
    }

}
