namespace Ticketwise.Models;

public class Trip
{
    public int Id {get; set;}
    public Vehicle Vehicle {get; set;}
    public int VehicleId {get; set;}
    public string Origin {get; set;}
    public string Destination {get; set;}
    public DateOnly Date {get; set;}    
    public TimeOnly DepartureTime {get; set;}
    public string Status {get; set;}

    public double TravelTime {get; set;}
    public int Cost {get; set;}




}
