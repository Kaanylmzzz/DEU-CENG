package swing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import swing.CompanySignUpFrame;

public class AdminFrame extends JFrame {

    public AdminFrame() {

        setTitle("Admin Panel");
        setSize(600, 500);
        setLocationRelativeTo(null); // Or wherever you want it
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout); // Border layout kullan


        JLabel operationLabel = new JLabel("Which action would you like to do?");
        operationLabel.setFont(new Font("Arial", Font.BOLD, 30));
        operationLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0)); // Boşluk ekle
        operationLabel.setHorizontalAlignment(SwingConstants.CENTER); // Ortala
        add(operationLabel, BorderLayout.NORTH);


        JButton addVehicleButton = new JButton("Add Vehicle");
        JButton removeVehicleButton = new JButton("Remove Vehicle");
        JButton addTripButton = new JButton("Add Trip");
        JButton removeTripButton = new JButton("Remove Trip");
        JButton seeTripsButton = new JButton("See Trips");
        JButton logoutButton = new JButton("Log out");

        addVehicleButton.setFont(new Font("Arial", Font.PLAIN, 24));
        removeVehicleButton.setFont(new Font("Arial", Font.PLAIN, 24));
        addTripButton.setFont(new Font("Arial", Font.PLAIN, 24));
        removeTripButton.setFont(new Font("Arial", Font.PLAIN, 24));
        seeTripsButton.setFont(new Font("Arial", Font.PLAIN, 24));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 24));


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 40, 40)); // 3 satır 2 sütunlu bir grid layout
        panel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));
        panel.add(addVehicleButton);
        panel.add(removeVehicleButton);
        panel.add(addTripButton);
        panel.add(removeTripButton);
        panel.add(seeTripsButton);
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        addVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddVehicleFrame addVehicleFrame = new AddVehicleFrame();
                addVehicleFrame.setVisible(true);
            }
        });
        removeVehicleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveVehicleFrame removeVehicleFrame = new RemoveVehicleFrame();
                removeVehicleFrame.setVisible(true);
            }

        });
        addTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTripFrame addTripFrame = new AddTripFrame();
                addTripFrame.setVisible(true);
            }
        });
        removeTripButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveTripFrame removeTripFrame = new RemoveTripFrame();
                removeTripFrame.setVisible(true);
            }
        });
        seeTripsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SeeTripsFrame seeTripsFrame = new SeeTripsFrame();
                seeTripsFrame.setVisible(true);
            }
        });


        setVisible(true);
    }
}
