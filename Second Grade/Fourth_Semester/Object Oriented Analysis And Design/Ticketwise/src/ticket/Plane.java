package ticket;

import swing.CompanyLogInFrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Plane extends Vehicle{

    public Plane(String companyName, String vehicleType, String licensePlate, int capacity, long registrationNumber) {
        super(companyName, vehicleType, licensePlate, capacity, registrationNumber);
    }

    @Override
    public void addVehicle(String companyName, String vehicleType, String licensePlate, int capacity, long registrationNumber) {
        Vehicle newPlane= new Plane(companyName, vehicleType, licensePlate, capacity, registrationNumber);
        vehicles.add(newPlane);
        writeToFile("vehicles.txt",newPlane);
    }

    @Override
    public void removeVehicle(String licensePlate) {
        readFromFile("vehicles.txt");
        for(Vehicle vehicle : vehicles)
        {
            if(vehicle.getLicensePlate().equals(licensePlate))
            {
                vehicles.remove(vehicle);
                break;
            }
        }
        writeToFileUpdated("vehicles.txt");

    }

    @Override
    public void readFromFile(String filename) {
        vehicles.clear();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Vehicle vehicle = new Plane(data[0], data[1], data[2], Integer.parseInt(data[3]), Long.parseLong(data[4]));
                vehicles.add(vehicle);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file.");
            e.printStackTrace();
        }

    }

    @Override
    public void writeToFile(String filename, Vehicle vehicle) {
        try {
            FileWriter writer = new FileWriter(filename, true); // true ile dosyanın sonuna ekleme yapılmasını sağlar
            writer.write(vehicle.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

    }



    @Override
    public String toString() {
        return getCompanyName() + "," + getVehicleType() + "," + getLicensePlate() + "," + getCapacity() + "," + getRegistrationNumber();
    }

    @Override
    public void writeToFileUpdated(String filename) {
        cleanTxt(filename);
        try {
            FileWriter writer = new FileWriter(filename);
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

    }

    @Override
    public boolean existVehicle(String licensePlate) {
        readFromFile("vehicles.txt");
        for(Vehicle vehicle : vehicles)
        {
            if(vehicle.getCompanyName().equals(CompanyLogInFrame.getTempCompanyName()) && vehicle.getLicensePlate().equals(licensePlate))
            {
                return true;
            }
        }
        return false;

    }

    @Override
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


}
