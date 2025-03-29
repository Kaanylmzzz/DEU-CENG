package ticket;

import swing.SeatFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Seat {
    private int tripNo;
    private int seatNumber;

    public Map<Integer, Map<Integer, String>> seats = new HashMap<>();

    public Seat(int tripNo, int seatNumber) {
        this.tripNo = tripNo;
        this.seatNumber = seatNumber;
    }
    public Seat(int tripNo) {
        this.tripNo = tripNo;
    }

    public void writeToFile() {
        try {
            FileWriter writer = new FileWriter("seats.txt");
            for (Map.Entry<Integer, Map<Integer, String>> tripEntry : seats.entrySet()) {
                writer.write(tripEntry.getKey() + ",");
                for (Map.Entry<Integer, String> seatEntry : tripEntry.getValue().entrySet()) {
                    writer.write(seatEntry.getKey() + "," + seatEntry.getValue() + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void updatedSeat(int tripNo, int seatNumber) {
        readTripsFromFile();
        if (seats.containsKey(tripNo) && seats.get(tripNo).containsKey(seatNumber)) {
            seats.get(tripNo).put(seatNumber, "false"); // Assuming "false" means the seat is taken
            writeToFile();
        }
    }

    public void whenRemovedTrip(int tripNo) {
        readTripsFromFile();
        seats.remove(tripNo);
        writeToFile();
    }

    public void whenAddTrip(int tripNo) {
        readTripsFromFile();
        Map<Integer, String> tripSeats = new HashMap<>();
        for (int i = 1; i <= 40; i++) {
            tripSeats.put(i, "true"); // Assuming "true" means the seat is available
        }
        seats.put(tripNo, tripSeats);
        writeToFile();
    }

    public void readTripsFromFile() {
        seats.clear(); // Clear existing trips before loading
        try {
            BufferedReader reader = new BufferedReader(new FileReader("seats.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int tripNo = Integer.parseInt(data[0]);
                Map<Integer, String> tripSeats = new HashMap<>();
                for (int i = 1; i < data.length; i += 2) {
                    tripSeats.put(Integer.parseInt(data[i]), data[i + 1]);
                }
                seats.put(tripNo, tripSeats);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
    }

    public boolean isAvailable(int tripNo, int chosenSeatNumber) {
        readTripsFromFile();
        if (seats.containsKey(tripNo) && seats.get(tripNo).containsKey(chosenSeatNumber)) {
            return seats.get(tripNo).get(chosenSeatNumber).equals("true");
        }
        return false;
    }

    // Getter methods
    public int getTripNo() {
        return tripNo;
    }
    public void setTripNo(int tripNo) {
        this.tripNo = tripNo;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    // Setter methods
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
