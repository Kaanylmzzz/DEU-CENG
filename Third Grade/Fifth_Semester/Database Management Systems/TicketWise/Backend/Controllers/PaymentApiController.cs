using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]

    public class PaymentApiController : ControllerBase // API Controller olarak düzenlendi
    {
        private readonly ApplicationDbContext _context;

        public PaymentApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        //API - PAYMENT
        [HttpPost]
        public IActionResult AddPayment([FromBody] PaymentRequest model)
        {
            Console.WriteLine("Customer ID: " + model.CustomerId);
            if (ModelState.IsValid)
            {
                var customer = _context.Customers.Find(model.CustomerId);
                var ticket = _context.Tickets.FirstOrDefault(t => t.Pnr == model.Pnr);

                if (ticket == null)
                {
                    return BadRequest("Invalid Customer or Trip.");
                }

                string transcationId = GenerateUniqueTransactionId();

                var newPayment = new Payment
                {
                    Customer = customer,
                    Ticket = ticket,
                    CustomerId = model.CustomerId,
                    TicketId = ticket.Id,
                    TranscationId = transcationId,
                    Status = model.Status,
                    PaymentDate = model.PaymentDate
                };

                _context.Payments.Add(newPayment);
                _context.SaveChanges();

                return Ok(newPayment);
            }

            return BadRequest(ModelState);
        }

        private string GenerateUniqueTransactionId()
        {
            Random rand = new Random();
            string transactionId;
            bool isUnique;

            do
            {
                // Generate a random 10-digit number
                transactionId = rand.Next(10000, 99999).ToString() + rand.Next(10000, 99999).ToString();

                // Check for uniqueness
                isUnique = !_context.Payments.Any(p => p.TranscationId == transactionId);
            } while (!isUnique);  // Keep generating until unique

            return transactionId;
        }
        



        


    }
}