package swing;

import ticket.Trip;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class ExpeditionFrame extends JFrame {

    private String from;
    private String to;
    private String date;
    private String companyName;
    private String licensePlate;
    public static String origin;
    public static String destination;
    public static String tempDate;
    public static String time;
    public static String cost;
    public Trip trip;
    public static int tripNo;

    public ExpeditionFrame(String from, String to, String date) {
        this.from = from;
        this.to = to;
        this.date = date;

        setTitle("Expedition Information");
        setSize(1400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(new EmptyBorder(23, 0, 0, 0));
        Font headerFont = new Font("Arial", Font.BOLD, 24);
        JLabel headerLabel = new JLabel(from + " ---> " + to);
        headerLabel.setFont(headerFont);
        topPanel.add(headerLabel);
        add(topPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Table data
        String[] columnNames = {"Company Name", "License Plate", "Capacity", "Origin", "Destination", "Date", "Departure Time", "Travel Time", "Catering", "Wi-Fi", "Availability", "Cost"};
        Font columnFont = new Font("Arial", Font.BOLD, 16);
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);


        try (BufferedReader reader = new BufferedReader(new FileReader("trips.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if(values[2].equals(SelectionFrame.getRouteType())) {
                    if (values[4].trim().equals(from) && values[5].trim().equals(to) && values[6].trim().equals(date)) {
                        if (values[9].trim().equalsIgnoreCase("true"))
                            values[9] = "Yes";
                        else if (values[9].trim().equalsIgnoreCase("false"))
                            values[9] = "No";

                        if (values[10].trim().equalsIgnoreCase("true"))
                            values[10] = "Yes";
                        else if (values[10].trim().equalsIgnoreCase("false"))
                            values[10] = "No";
                        values[11] += " TL";
                        model.addRow(new Object[]{values[1], values[3], "40", values[4], values[5], values[6], values[7], values[8], values[9], values[10], "Available", values[11]});
                    }
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

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        JButton purchaseButton = new JButton("Purchase Ticket");
        purchaseButton.setPreferredSize(new Dimension(200, 50));
        purchaseButton.setFont(new Font("Arial", Font.BOLD, 22));
        buttonPanel.add(purchaseButton);
        add(buttonPanel, BorderLayout.SOUTH);

        purchaseButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            companyName = (String) table.getValueAt(selectedRow,0);
            licensePlate = (String) table.getValueAt(selectedRow, 1);
            origin = (String) table.getValueAt(selectedRow, 3);
            destination = (String) table.getValueAt(selectedRow, 4);
            tempDate = (String) table.getValueAt(selectedRow, 5);
            time = (String) table.getValueAt(selectedRow, 6);
            cost = (String) table.getValueAt(selectedRow,11);
            trip = new Trip(licensePlate,origin,destination,tempDate,time);
            tripNo = trip.findTripNo(companyName,licensePlate,origin,destination,tempDate,time);

            SeatFrame seatFrame = new SeatFrame();
            seatFrame.setVisible(true);


        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }



    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTempDate() {
        return tempDate;
    }

    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
