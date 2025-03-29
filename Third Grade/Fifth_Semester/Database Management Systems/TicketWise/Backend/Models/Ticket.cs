namespace Ticketwise.Models;

public class Ticket
{
    public int Id {get; set;}
    public Customer Customer {get; set;}
    public int? CustomerId {get; set;}
    public Trip Trip {get; set;}
    public int TripId {get; set;}
    public int SeatNumber {get; set;}
    public string Status{get; set;}
    public DateTime PurchaseDate {get; set;}
    public string Gender {get; set;}
    public string Pnr {get; set;}

}
