package ticket;

import swing.CompanyLogInFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Trip {
    public static int tripNo;
    private String companyName;
    private String vehicleType;
    private String licensePlate;
    private String origin;
    private String destination;
    private String date;
    private String departureTime;
    private String travelTime;
    private boolean wifiStatus;
    private boolean cateringStatus;
    private int cost;
    public Map<Integer, Trip> trips = new HashMap<>();


    //for add
    public Trip(String companyName, String vehicleType, String licensePlate, String origin, String destination, String date, String departureTime, String travelTime, boolean wifiStatus, boolean cateringStatus, int cost) {
        this.companyName = companyName;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.departureTime = departureTime;
        this.travelTime = travelTime;
        this.wifiStatus = wifiStatus;
        this.cateringStatus = cateringStatus;
        this.cost = cost;
    }

    //for remove
    public Trip(String licensePlate,String origin,String destination, String date,String departureTime) {
        this.licensePlate=licensePlate;
        this.origin=origin;
        this.destination=destination;
        this.date=date;
        this.departureTime=departureTime;
    }

    public void addTrip(String companyName, String vehicleType, String licensePlate, String origin, String destination, String date, String departureTime, String travelTime, boolean wifiStatus, boolean cateringStatus, int cost) {
        Trip newTrip = new Trip(companyName, vehicleType, licensePlate, origin, destination, date, departureTime, travelTime, wifiStatus, cateringStatus, cost);
        trips.put(getTripNo(),newTrip);
        writeTripToFile("trips.txt", newTrip);
    }

    public boolean existTrip(String licensePlate, String origin, String destination, String date, String departureTime) {
        readTripsFromFile("trips.txt");
        for (Map.Entry<Integer, Trip> entry : trips.entrySet()) {
            Trip trip = entry.getValue();
            if (trip.getCompanyName().equals(CompanyLogInFrame.getTempCompanyName()) && trip.getLicensePlate().equals(licensePlate) && trip.getOrigin().equals(origin) && trip.getDestination().equals(destination) && trip.getDate().equals(date) && trip.getDepartureTime().equals(departureTime)) {
                return true;
            }
        }
        return false;
    }


    public boolean existVehicle(String companyName,String licensePlate)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if(data[0].equals(companyName) && data[2].equals(licensePlate))
                {
                    return true;
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
        return false;
    }

    public int findTripNo(String companyName, String licensePlate, String origin, String destination, String date, String departureTime)
    {
        readTripsFromFile("trips.txt");
        for (Map.Entry<Integer, Trip> entry : trips.entrySet()) {
            Trip trip = entry.getValue();
            int tripId = entry.getKey();
            if (trip.getCompanyName().equals(companyName) && trip.getLicensePlate().equals(licensePlate) && trip.getOrigin().equals(origin) && trip.getDestination().equals(destination) && trip.getDate().equals(date) && trip.getDepartureTime().equals(departureTime)) {
                return tripId;
            }
        }
        return 0;
    }

    public void writeTripToFile(String filename, Trip trip) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write((getTripNo() + 1) + "," + trip.getCompanyName() + "," + trip.getVehicleType() + "," + trip.getLicensePlate() + "," + trip.getOrigin() + "," + trip.getDestination() + "," + trip.getDate() + "," + trip.getDepartureTime() + "," + trip.getTravelTime() + "," + trip.isWifiStatus() + "," + trip.isCateringStatus() + "," + trip.getCost() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void removeTrip(String licensePlate,String origin,String destination, String date,String departureTime) {
        readTripsFromFile("trips.txt");
        for (Map.Entry<Integer, Trip> entry : trips.entrySet()) {
            Trip trip = entry.getValue();
            int tripId = entry.getKey(); // HashMap'teki anahtarın değerini al
            if (trip.getLicensePlate().equals(licensePlate) && trip.getOrigin().equals(origin) && trip.getDestination().equals(destination) && trip.getDate().equals(date) && trip.getDepartureTime().equals(departureTime)) {
                trips.remove(tripId);
                break;
            }
        }
        writeTripsToFileUpdated("trips.txt");

    }

    public void readTripsFromFile(String filename) {
        trips.clear(); // Clear existing trips before loading
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                tripNo = Integer.parseInt(data[0]);
                Trip trip = new Trip(data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], Boolean.parseBoolean(data[9]), Boolean.parseBoolean(data[10]), Integer.parseInt(data[11]));
                trips.put(tripNo,trip);
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }
    }
    public void writeTripsToFileUpdated(String filename) {
        cleanTxt(filename);
        try {
            FileWriter writer = new FileWriter(filename);
            for (Map.Entry<Integer, Trip> entry : trips.entrySet()) {
                Trip trip = entry.getValue();
                int tripId = entry.getKey(); // HashMap'teki anahtarın değerini al
                writer.write(tripId + "," + trip.getCompanyName() + "," + trip.getVehicleType() + "," + trip.getLicensePlate() + "," + trip.getOrigin() + "," + trip.getDestination() + "," + trip.getDate() + "," + trip.getDepartureTime() + "," + trip.getTravelTime() + "," + trip.isWifiStatus() + "," + trip.isCateringStatus() + "," + trip.getCost() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    public void cleanTxt(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    // Getters and Setters

    public int getTripNo() {
        return tripNo;
    }

    public void setTripNo(int tripNo) {
        Trip.tripNo = tripNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public boolean isWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(boolean wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public boolean isCateringStatus() {
        return cateringStatus;
    }

    public void setCateringStatus(boolean cateringStatus) {
        this.cateringStatus = cateringStatus;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    
}
