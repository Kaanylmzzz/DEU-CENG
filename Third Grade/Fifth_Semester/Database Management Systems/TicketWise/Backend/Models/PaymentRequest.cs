namespace Ticketwise.Models;
public class PaymentRequest
{
    public int? CustomerId { get; set; }
    public string Pnr { get; set; }
    public string Status { get; set; }
    public DateTime PaymentDate { get; set; }
}
