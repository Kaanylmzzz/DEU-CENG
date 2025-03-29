package ticket;

public interface VehicleInterface {
    // Method signatures
    String getVehicleType();
    void setVehicleType(String vehicleType);
    void addVehicle(String companyName, String vehicleType, String licensePlate, int capacity, long registrationNumber);

    void removeVehicle(String licensePlate);
    void readFromFile(String filename);
    void writeToFile(String filename, Vehicle vehicle);
}