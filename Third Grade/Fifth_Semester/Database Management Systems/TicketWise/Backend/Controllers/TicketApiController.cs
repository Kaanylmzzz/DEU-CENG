using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking.Internal;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]

    public class TicketApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        public TicketApiController(ApplicationDbContext context)
        {
            _context = context;
        }

       // API - ADD TICKET
       [HttpPost]
        public IActionResult AddTicket([FromBody] TicketRequest model)
        {
            if (ModelState.IsValid)
            {
                var customer = _context.Customers.Find(model.CustomerId);
                var trip = _context.Trips.Find(model.TripId);

                if (trip == null)
                {
                    return BadRequest("Invalid Customer or Trip.");
                }
                var pnr = GenerateUniquePNR();

                var newTicket = new Ticket
                {
                    Customer = customer,
                    Trip = trip,
                    CustomerId = model.CustomerId,
                    TripId = model.TripId,
                    SeatNumber = model.SeatNumber,
                    Status = model.Status,
                    PurchaseDate = model.PurchaseDate,
                    Gender = model.Gender,
                    Pnr = pnr
                };

                _context.Tickets.Add(newTicket);
                _context.SaveChanges();

                return Ok(newTicket);
            }

            return BadRequest(ModelState);
        }

        private string GenerateUniquePNR()
        {
            var random = new Random();
            string pnr;
            bool isUnique = false;
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 

            // Ensure the PNR is unique
            do
            {
                // Generates a random PNR-like string with 6 characters
                pnr = new string(Enumerable.Range(0, 6)
                                        .Select(_ => chars[random.Next(chars.Length)])
                                        .ToArray());
                isUnique = !_context.Tickets.Any(t => t.Pnr == pnr); // Check if the PNR already exists
            }
            while (!isUnique); // Repeat until a unique PNR is generated

            return pnr;
        }


        // API - GET TRAVELS
        [HttpPost]        
        public IActionResult GetTicketsByCustomer(int id)
        {
            var tickets = _context.Tickets.Include(t => t.Customer).Include(t => t.Trip).Where(t => t.CustomerId == id).ToList();
            
            if (tickets.Count == 0)
            {
                return NotFound("No tickets found for this customer.");
            }

            if (tickets != null)
            {
                return Ok(new 
                { 
                    success = true,
                    message = "Ticket successful", 
                    tickets = tickets.Select(t => new 
                    {
                        pnr = t.Pnr,
                        name = t.Customer.Name,
                        surname = t.Customer.Surname,
                        seat = t.SeatNumber,  
                        cost = t.Trip.Cost,
                        origin = t.Trip.Origin,
                        destination = t.Trip.Destination,
                        date = t.Trip.Date,
                        departureTime = t.Trip.DepartureTime,
                    })
                });
            }

            return BadRequest(new 
            { 
                success = false, 
                message = "An error occurred while fetching tickets." 
            });


            
        }

        // API - CANCEL TICKET
        [HttpDelete]
        public IActionResult CancelTicket(int ticketId)
        {
            // Find the ticket in the database
            var ticket = _context.Tickets.FirstOrDefault(t => t.Id == ticketId);

            if (ticket == null)
            {
                return NotFound(new 
                { 
                    success = false, 
                    message = "Ticket not found." 
                });
            }

            // Remove the ticket
            _context.Tickets.Remove(ticket);
            _context.SaveChanges();

            return Ok(new 
            { 
                success = true, 
                message = "Ticket successfully canceled." 
            });
        }




        





    }
}