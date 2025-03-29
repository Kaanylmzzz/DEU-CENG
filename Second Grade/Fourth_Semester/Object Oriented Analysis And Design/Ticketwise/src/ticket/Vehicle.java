package ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Vehicle implements VehicleInterface{
    private String vehicleType;
    private String companyName;
    private String licensePlate;
    private long registrationNumber;
    private int capacity;
    public ArrayList<Vehicle> vehicles = new ArrayList<>();


    // Constructor
    public Vehicle(String companyName, String vehicleType, String licensePlate, int capacity, long registrationNumber) {
        this.companyName = companyName;
        this.vehicleType = vehicleType;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.registrationNumber = registrationNumber;
    }



    public abstract void addVehicle(String companyName, String vehicleType, String licensePlate, int capacity, long registrationNumber);

    public abstract void removeVehicle(String licensePlate);
    public abstract void readFromFile(String filename);
    public abstract void writeToFile(String filename, Vehicle vehicle);
    public abstract String toString();
    public abstract void writeToFileUpdated(String filename);
    public abstract boolean existVehicle(String licensePlate);
    public abstract void cleanTxt(String filename);

    // Getters and Setters
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public long getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(int registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

}