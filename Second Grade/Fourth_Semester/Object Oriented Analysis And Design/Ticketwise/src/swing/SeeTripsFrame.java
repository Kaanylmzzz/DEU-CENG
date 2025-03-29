package swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class SeeTripsFrame extends JFrame {

    public SeeTripsFrame() {

        setTitle("See Trips Frame");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // To center the frame on screen
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(new EmptyBorder(23, 0, 0, 0)); // 23 pixel top margin
        Font headerFont = new Font("Arial", Font.BOLD, 24);
        JLabel headerLabel = new JLabel(CompanyLogInFrame.getTempCompanyName());
        headerLabel.setFont(headerFont);
        topPanel.add(headerLabel);
        add(topPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Column names for the table
        String[] columnNames = {"License Plate", "Capacity", "Origin", "Destination", "Date", "Departure Time", "Travel Time", "Catering", "Wi-Fi", "Cost"};
        Font columnFont = new Font("Arial", Font.BOLD, 16);

        // Using DefaultTableModel to handle data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader("trips.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values[1].trim().equals(CompanyLogInFrame.getTempCompanyName())) {
                    if (values[9].trim().equalsIgnoreCase("true"))
                        values[9] = "Yes";
                    else if (values[9].trim().equalsIgnoreCase("false"))
                        values[9] = "No";

                    if (values[10].trim().equalsIgnoreCase("true"))
                        values[10] = "Yes";
                    else if (values[10].trim().equalsIgnoreCase("false"))
                        values[10] = "No";

                    values[11] += " TL";

                    model.addRow(new Object[]{values[3], "40", values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.getTableHeader().setFont(columnFont);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }
}
