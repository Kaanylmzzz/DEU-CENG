namespace Ticketwise.Models;

public class Customer
{
    public int Id {get; set;}
    public string Name {get; set;}
    public string Surname {get; set;}
    public string Email {get; set;}
    public string PhoneNumber {get; set;}
    public string Gender {get; set;}
    public string Identity {get; set;}
    public DateOnly Birthday {get; set;}
    public string Password {get; set;}
}
