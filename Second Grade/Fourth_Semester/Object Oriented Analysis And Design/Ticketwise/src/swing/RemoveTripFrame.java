package swing;

import ticket.Bus;
import ticket.Seat;
import ticket.Trip;
import ticket.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class RemoveTripFrame extends JFrame {
    private JComboBox<String> originField, destinationField,monthComboBox,dayComboBox,hourComboBox,minuteComboBox;
    private JComboBox<Integer> yearComboBox ;
    private JTextField licensePlateField;
    private JButton removeButton;

    public RemoveTripFrame() {
        setTitle("Remove Trip");
        setSize(700, 400);  // Adjusted size for better layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        licensePlateField = new JTextField(10);
        licensePlateField.setFont(font);

        String[] cities = {"Select City", "İstanbul", "İzmir", "Aydın", "Muğla", "Eskişehir"};
        originField = new JComboBox<>(cities);
        destinationField = new JComboBox<>(cities);
        originField.setFont(font);
        destinationField.setFont(font);

        // Initialize date components
        dayComboBox = new JComboBox<>(generateDays());
        monthComboBox = new JComboBox<>(generateMonths());
        yearComboBox = new JComboBox<>(generateYears());
        hourComboBox = new JComboBox<>(generateHours());
        minuteComboBox = new JComboBox<>(generateMinutes());

        int row = 1;
        addField(formPanel, gbc, "License Plate:", licensePlateField, row++,0);
        addField(formPanel, gbc, "Origin:", originField, row++,0);
        addField(formPanel, gbc, "Destination:", destinationField, row++,1);
        addDateAndTimeFields(formPanel,gbc,"Date:" ,"Time:", row++,0);

        removeButton = new JButton("REMOVE TRIP");
        removeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        removeButton.setPreferredSize(new Dimension(250, 60)); // Adjusted size for visual consistency
        gbc.gridwidth = GridBagConstraints.REMAINDER; // This will end the row, centering the button if no other components are in the row
        gbc.gridx = 0; // Start from the first column
        gbc.gridy = row; // Position at the next available row
        gbc.fill = GridBagConstraints.NONE; // Do not stretch the button to fill the grid cell
        gbc.anchor = GridBagConstraints.CENTER; // Center the button within its grid cell
        formPanel.add(removeButton, gbc);

        removeButton.addActionListener(e -> {
            if (validateFields()) {
                String date = dayComboBox.getSelectedItem().toString() + "-" + monthComboBox.getSelectedItem().toString()+ "-" + yearComboBox.getSelectedItem();
                String time = hourComboBox.getSelectedItem().toString() + ":" + minuteComboBox.getSelectedItem().toString();
                Trip vehicleTrip = new Trip(licensePlateField.getText(), originField.getSelectedItem().toString(), destinationField.getSelectedItem().toString(), date, time);
                int removedTripNo = vehicleTrip.findTripNo(CompanyLogInFrame.getTempCompanyName(),licensePlateField.getText(), originField.getSelectedItem().toString(), destinationField.getSelectedItem().toString(), date, time);
                Seat seat = new Seat(removedTripNo);
                if (vehicleTrip.existTrip(licensePlateField.getText(), originField.getSelectedItem().toString(), destinationField.getSelectedItem().toString(), date, time)) {
                    vehicleTrip.removeTrip(licensePlateField.getText(), originField.getSelectedItem().toString(), destinationField.getSelectedItem().toString(), date, time);
                    seat.whenRemovedTrip(removedTripNo);
                    JOptionPane.showMessageDialog(this, "Trip removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No such trip found.", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void addDateAndTimeFields(JPanel panel, GridBagConstraints gbc,String labelText,String labelText1, int row, int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        gbc.gridx = 2 * (col % 2);
        gbc.gridy = row / 2;
        panel.add(label, gbc);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(dayComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(yearComboBox);

        gbc.gridx = 2 * (col % 2) + 1;
        panel.add(datePanel, gbc);

        gbc.gridx = 2 * ((col+1) % 2);
        gbc.gridy = (row + 1) / 2;
        JLabel label1 = new JLabel(labelText1);
        label1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        panel.add(label1, gbc);

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.add(hourComboBox);
        timePanel.add(minuteComboBox);

        gbc.gridx = 2 * ((col+1) % 2) + 1;
        panel.add(timePanel, gbc);
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
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hours[i] = "0" + i;
            } else {
                hours[i] = String.valueOf(i);
            }
        }
        return hours;
    }

    private String[] generateMinutes() {
        String[] minutes = new String[60];
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                minutes[i] = "0" + (i );
            } else {
                minutes[i] = String.valueOf(i );
            }
        }
        return minutes;
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
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row,int col) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        gbc.gridx = 2 * (col % 2);
        gbc.gridy = row / 2;
        panel.add(label, gbc);
        gbc.gridx = 2 * (col % 2) + 1;
        panel.add(field,gbc);
    }
}
