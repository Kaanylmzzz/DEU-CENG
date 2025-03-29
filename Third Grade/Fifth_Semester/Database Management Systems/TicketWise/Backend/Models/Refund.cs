namespace Ticketwise.Models
{
    public class Refund
    {   
        public int Id { get; set; }
        public Payment Payment { get; set; }
        public int PaymentId { get; set; }
        public Customer Customer { get; set; }
        public int CustomerId { get; set; }
        public DateTime RefundDate { get; set; }
        public double Amount { get; set; }
        public string Status { get; set; }
    }   
}

