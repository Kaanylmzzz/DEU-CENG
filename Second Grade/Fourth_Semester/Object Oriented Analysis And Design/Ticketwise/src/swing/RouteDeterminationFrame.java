package swing;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class RouteDeterminationFrame extends JFrame {
    private JComboBox<String> fromComboBox;
    private JComboBox<String> toComboBox;

    // Day, Month, Year Combo Box Initialization and Selection
    private JComboBox<String> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private String date;

    public RouteDeterminationFrame() {
        setTitle("Route Determination");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, -90, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 30, 60, 30));
        Font labelFont = new Font("Arial", Font.BOLD, 24);

        // From Location Combo Box
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(labelFont);
        panel.add(fromLabel);
        fromComboBox = new JComboBox<>(new String[]{"İstanbul", "İzmir", "Aydın", "Muğla", "Eskişehir"});
        panel.add(fromComboBox);

        // To Location Combo Box
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(labelFont);
        panel.add(toLabel);
        toComboBox = new JComboBox<>(new String[]{"İstanbul", "İzmir", "Aydın", "Muğla", "Eskişehir"});
        panel.add(toComboBox);

        // Date selection
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(labelFont);
        panel.add(dateLabel);
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new FlowLayout());

        dayComboBox = new JComboBox<>(generateDays());
        monthComboBox = new JComboBox<>(generateMonths());
        yearComboBox = new JComboBox<>(generateYears());
        datePanel.add(dayComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(yearComboBox);
        panel.add(datePanel);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 26));
        searchButton.addActionListener(e -> {
            String from = (String) fromComboBox.getSelectedItem();
            String to = (String) toComboBox.getSelectedItem();
            int day = Integer.parseInt((String) dayComboBox.getSelectedItem());
            int month = Integer.parseInt((String) monthComboBox.getSelectedItem()) - 1;
            int year = (int) yearComboBox.getSelectedItem();
            date = String.format("%02d-%02d-%d", day, month + 1, year);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day, 0, 0, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);

            if (selectedDate.before(today)) {
                JOptionPane.showMessageDialog(this, "Please select a current or future date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (from.equals(to)) {
                JOptionPane.showMessageDialog(this, "The 'From' and 'To' locations cannot be the same.", "Invalid Route", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ExpeditionFrame expeditionFrame = new ExpeditionFrame(from, to, date);
            expeditionFrame.setVisible(true);
        });
        panel.add(searchButton);

        // Add panel to frame
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private String[] generateDays() {
        String[] days = new String[31];
        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                days[i - 1] = "0" + i;
            } else {
                days[i - 1] = String.valueOf(i);
            }
        }
        return days;
    }

    private String[] generateMonths() {
        String[] months = new String[12];
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                months[i - 1] = "0" + i;
            } else {
                months[i - 1] = String.valueOf(i);
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
