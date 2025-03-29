package swing;
import ticket.Seat;
import ticket.Trip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SeatFrame extends JFrame {
    private JButton[] seats;
    public static int choosenSeatNumber;

    public SeatFrame() {
        setTitle("Otobüs Koltukları");
        setSize(800, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(12, 5)); // 6 satır, 4 sütun (40 koltuk + 1 boşluk)

        seats = new JButton[40]; // Toplam 40 koltuk

        JButton driverButton = new JButton("Driver");
        driverButton.setEnabled(false); // Butonun tıklanamaz olduğundan emin olun
        add(driverButton);
        int k = 0;
        while(k<4){
            add(new JPanel());
            k++;
        }
        // Koltukları ekleyelim
        for (int i = 0; i < seats.length; i++) {
            if(i % 4 == 2){
                add(new JPanel());
            }
            seats[i] = new JButton("Koltuk " + (i + 1)); // Koltuk numarasını belirtmek için
            int seatNumber = i + 1;
            add(seats[i]);
            Seat seat = new Seat(ExpeditionFrame.tripNo,seatNumber);
            if(!seat.isAvailable(ExpeditionFrame.tripNo,seatNumber))
            {
                seats[i].setEnabled(false);
            }

            int finalI = i;
            seats[i].addActionListener(e -> {
                choosenSeatNumber = finalI + 1;
                JOptionPane.showMessageDialog(null, "Koltuk " + choosenSeatNumber + " seçildi.");
                PaymentFrame paymentFrame = new PaymentFrame();
                paymentFrame.setVisible(true);
                dispose();

            });


        }

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    public int getChoosenSeatNumber() {
        return choosenSeatNumber;
    }

    public void setChoosenSeatNumber(int choosenSeatNumber) {
        this.choosenSeatNumber = choosenSeatNumber;
    }
}
