namespace Ticketwise.Models;
public class TicketDetail
{
    public int TicketId { get; set; }
    public int SeatNumber { get; set; }
    public string Gender {get; set;}
    public string Pnr {get; set;}
    public string Status{get; set;}
    public string CustomerName { get; set; }
    public string CustomerSurname { get; set; }
    public string CustomerEmail { get; set; }
    public string Origin { get; set; }
    public string Destination { get; set; }
    public TimeOnly DepartureTime { get; set; }
    public DateOnly Date { get; set; }
    public double TravelTime { get; set; }
    public int Cost { get; set; }
    public string LicensePlate { get; set; }
}