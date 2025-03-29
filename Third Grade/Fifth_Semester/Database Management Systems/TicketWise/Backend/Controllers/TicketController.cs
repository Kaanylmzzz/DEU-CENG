using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Helpers;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class TicketController : Controller
    {
         private readonly ApplicationDbContext _context;

        public TicketController(ApplicationDbContext context)
        {
            _context = context;
        }

        //FAKER
        public IActionResult GenerateAndSaveFakeTickets(int count = 10)
        {
     
            var customers = _context.Customers.ToList();

            if (customers.Count == 0)
            {
                return Content("error");
            }

            var trips = _context.Trips.ToList();

            if (trips.Count == 0)
            {
                return Content("error");
            }

            var fakeTickets = FakeDataGenerator.GenerateTickets(count, customers,trips);

            _context.Tickets.AddRange(fakeTickets);

            _context.SaveChanges();

            return RedirectToAction("Index");
        }

    
        // MVC - LIST
        public IActionResult Index(int page = 1, string searchString = "")
        {   
            //Pagination
            int pageSize = 8; 
            var tickets = _context.TicketsWithDetails
                                .Skip((page - 1) * pageSize)
                                .Take(pageSize)
                                .ToList();

            var totalTickets = _context.TicketsWithDetails.Count();
            var totalPages = (int)Math.Ceiling(totalTickets / (double)pageSize);

            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            //Search
            if (!string.IsNullOrEmpty(searchString))
            {
                tickets = tickets.Where(t => t.Pnr.Contains(searchString) || t.CustomerName.Contains(searchString) || t.CustomerSurname.Contains(searchString) || t.LicensePlate.Contains(searchString)).ToList();
            }
            return View(tickets);
        }


        // MVC - DELETE TICKET
        public IActionResult Delete(int id)
        {
            var ticket = _context.Tickets.Find(id);
            if (ticket != null)
            {
                _context.Tickets.Remove(ticket);
                _context.SaveChanges();
            }
            return RedirectToAction("Index","Ticket");
        }

    }
}
