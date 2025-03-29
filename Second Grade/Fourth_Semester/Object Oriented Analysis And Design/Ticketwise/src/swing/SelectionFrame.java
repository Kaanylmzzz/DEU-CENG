package swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionFrame extends JFrame {

    public static String routeType;

    public SelectionFrame() {
        setTitle("Transportation Selection");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // To center the frame on screen

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Panel to hold the question label with padding
        JPanel questionPanel = new JPanel();
        questionPanel.setBorder(new EmptyBorder(140, 0, 70, 0)); // Top: 10px, Bottom: 25px
        JLabel questionLabel = new JLabel("Which transportation method would you like to travel with?");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionPanel.add(questionLabel);
        mainPanel.add(questionPanel, BorderLayout.NORTH);

        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout());
        JButton busButton = new JButton("Bus");
        JButton trainButton = new JButton("Train");
        JButton planeButton = new JButton("Plane");

        // Setting button size
        Dimension buttonSize = new Dimension(180, 60);
        busButton.setPreferredSize(buttonSize);
        trainButton.setPreferredSize(buttonSize);
        planeButton.setPreferredSize(buttonSize);

        // Increasing font size of buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        busButton.setFont(buttonFont);
        trainButton.setFont(buttonFont);
        planeButton.setFont(buttonFont);

        // Adding click events to buttons
        busButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Bus selected.");
                RouteDeterminationFrame routeDeterminationFrame = new RouteDeterminationFrame();
                routeDeterminationFrame.setVisible(true); // Make the selection frame visible
                setRouteType("Bus");
                dispose(); // Close the current frame
            }
        });

        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Train selected.");
                RouteDeterminationFrame routeDeterminationFrame = new RouteDeterminationFrame();
                routeDeterminationFrame.setVisible(true); // Make the selection frame visible
                setRouteType("Train");
                dispose(); // Close the current frame
            }
        });

        planeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Plane selected.");
                RouteDeterminationFrame routeDeterminationFrame = new RouteDeterminationFrame();
                routeDeterminationFrame.setVisible(true); // Make the selection frame visible
                setRouteType("Plane");
                dispose(); // Close the current frame
            }
        });

        // Adding buttons to the panel
        optionsPanel.add(busButton);
        optionsPanel.add(trainButton);
        optionsPanel.add(planeButton);

        // Adding options panel to the main panel
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        // Adding main panel to the frame
        add(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static String getRouteType() {
        return routeType;
    }

    public static void setRouteType(String routeType) {
        SelectionFrame.routeType = routeType;
    }

}