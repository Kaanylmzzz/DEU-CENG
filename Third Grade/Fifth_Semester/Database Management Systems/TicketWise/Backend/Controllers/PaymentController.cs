using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Helpers;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class PaymentController : Controller
    {
        private readonly ApplicationDbContext _context;

        public PaymentController(ApplicationDbContext context)
        {
            _context = context;
        }

        //FAKER
        public IActionResult GenerateAndSaveFakePayments(int count = 10)
        {
            var customers = _context.Customers.ToList();

            if (customers.Count == 0)
            {
                return Content("error");
            }

            var tickets = _context.Tickets.ToList();

            if (tickets.Count == 0)
            {
                return Content("error");
            }

            var fakePayments = FakeDataGenerator.GeneratePayments(count, customers,tickets);

            _context.Payments.AddRange(fakePayments);

            _context.SaveChanges();

            return RedirectToAction("Index");
        }
        
        //MVC - LIST
        public IActionResult Index(int page = 1, string searchString = "")
        {
            //Pagination
            int pageSize = 13; 
            var payments = _context.Payments
                                .Include(e => e.Customer)
                                .Include(e => e.Ticket)
                                    .ThenInclude(t => t.Trip)
                                .Skip((page - 1) * pageSize)
                                .Take(pageSize)
                                .ToList();

            var totalPayments = _context.Payments.Count();
            var totalPages = (int)Math.Ceiling(totalPayments / (double)pageSize);

            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            //Search
            if (!string.IsNullOrEmpty(searchString))
            {
                payments = payments.Where(p => p.Ticket.Pnr.Contains(searchString) || p.TranscationId.Contains(searchString)).ToList();
            }

            return View(payments);
        }
    }
}