namespace Ticketwise.Models;
public class TicketRequest
{
    public int? CustomerId { get; set; }
    public int TripId { get; set; }
    public int SeatNumber { get; set; }
    public string Status { get; set; }
    public DateTime PurchaseDate { get; set; }
    public string Gender { get; set; }
}
