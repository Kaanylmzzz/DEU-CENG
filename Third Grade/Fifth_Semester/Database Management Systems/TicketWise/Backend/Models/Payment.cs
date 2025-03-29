namespace Ticketwise.Models
{
    public class Payment
    {
        public int Id {get; set;}
        public Customer Customer {get; set;}
        public int? CustomerId {get; set;}
        public Ticket Ticket {get; set;}
        public int TicketId {get; set;}
        public string TranscationId {get; set;}
        public string Status {get; set;}
       
        public DateTime PaymentDate {get; set;}
    }
}