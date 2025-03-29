package swing;

import ticket.Payment;
import ticket.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class PaymentFrame extends JFrame {
    // Declare components
    private JLabel ticketInfoLabel;
    private JTextField cardOwnerField, cardNumberField, cvvField;
    private JButton purchaseButton;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox ;

    public PaymentFrame() {
        setTitle("Payment");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        // Initialize date components
        monthComboBox = new JComboBox<>(generateMonths());
        yearComboBox = new JComboBox<>(generateYears());
        monthComboBox.setFont(font);
        yearComboBox.setFont(font);

        // Ticket information
        String format = String.format("<html>Nereden: %s<br/>Nereye: %s<br/>Gün: %s<br/>Sefer Saati: %s<br/>Koltuk Numarası: %d<br/>Ücret: %s</html>",ExpeditionFrame.origin,ExpeditionFrame.destination,ExpeditionFrame.tempDate,ExpeditionFrame.time,SeatFrame.choosenSeatNumber,ExpeditionFrame.cost);
        ticketInfoLabel = new JLabel(format);
        ticketInfoLabel.setBorder(BorderFactory.createTitledBorder("Ticket Details"));
        ticketInfoLabel.setFont(font);
        panel.add(ticketInfoLabel, gbc);

        gbc.gridy++;
        // Card owner name field
        cardOwnerField = new JTextField(20);
        JLabel cardOwnerLabel = new JLabel("Cardholder Name:");
        cardOwnerLabel.setFont(font);
        panel.add(cardOwnerLabel, gbc);
        gbc.gridx = 1;
        panel.add(cardOwnerField, gbc);

        // Card number field
        gbc.gridy++;
        gbc.gridx = 0;
        cardNumberField = new JTextField(20);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(font);
        panel.add(cardNumberLabel, gbc);
        gbc.gridx = 1;
        panel.add(cardNumberField, gbc);

        // Card expiry date
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1; // Set gridwidth back to 1 to properly align next components
        addDateFields(panel, gbc, "Expiry Date (MM/YY):");
        String date = monthComboBox.getSelectedItem().toString()+ "-" + yearComboBox.getSelectedItem();


        // CVV field
        gbc.gridy++;
        gbc.gridx = 0; // Reset column index for new row
        cvvField = new JTextField(20);
        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(font);
        panel.add(cvvLabel, gbc);
        gbc.gridx = 1;
        panel.add(cvvField, gbc);

        // Purchase button
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        purchaseButton = new JButton("PURCHASE");
        purchaseButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        purchaseButton.setPreferredSize(new Dimension(200, 50)); // Set preferred size to make it bigger
        purchaseButton.addActionListener(e -> {
            if(validateFields())
            {
                Seat seat = new Seat(ExpeditionFrame.tripNo,SeatFrame.choosenSeatNumber);
                seat.updatedSeat(ExpeditionFrame.tripNo,SeatFrame.choosenSeatNumber);
                JOptionPane.showMessageDialog(this, "The ticket has been purchased successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }

        });


        panel.add(purchaseButton, gbc);

        add(panel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();
        String date = monthComboBox.getSelectedItem().toString()+ "-" + yearComboBox.getSelectedItem();
        Payment payment = new Payment(cardOwnerField.getText().toString(),cardNumberField.getText().toString(),date,cvvField.getText().toString());
        if (cardOwnerField.getText().trim().isEmpty()) {
            errors.append("Cardholder name must not be empty.\n");
        }
        else if(!payment.validateCardholderName(cardOwnerField.getText().toString()))
        {
            errors.append("Please enter your name and surname correctly.\n");
        }
        if (cardNumberField.getText().trim().isEmpty()) {
            errors.append("Card number must not be empty.\n");
        }
        else if(!payment.validateCardNumber(cardNumberField.getText().toString()))
        {
            errors.append("The card number must be in the format XXXXXXXXXXXXXXXX.\n");
        }
        if (cvvField.getText().trim().isEmpty()) {
            errors.append("CVV must not be empty.\n");
        }
        else if(!payment.validateCVV(cvvField.getText().toString()))
        {
            errors.append("CVV must be a 3 digit number.\n");
        }
        if(Integer.parseInt(monthComboBox.getSelectedItem().toString()) < 5 && Integer.parseInt(yearComboBox.getSelectedItem().toString()) == 2024)
        {
            errors.append("You can not choose past time.\n");
        }
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void addDateFields(JPanel panel, GridBagConstraints gbc, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        panel.add(label, gbc);

        gbc.gridx = 1; // Move to the next column for the combo boxes
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(monthComboBox);
        datePanel.add(yearComboBox);
        panel.add(datePanel, gbc);

        gbc.gridx = 0; // Reset column index for next components
        gbc.gridy++; // Move to the next row after the combo boxes
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
}
