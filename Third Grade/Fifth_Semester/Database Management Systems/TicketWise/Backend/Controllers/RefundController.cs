using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Helpers;

namespace Ticketwise.Controllers
{
    public class RefundController : Controller
    {
        private readonly ApplicationDbContext _context;

        public RefundController(ApplicationDbContext context)
        {
            _context = context;
        }
        
        // MVC - LIST
        public IActionResult Index(int page = 1, string searchString = "")
        {
            //Pagination
            int pageSize = 11; 
            var refunds = _context.Refunds
                                .Include(e => e.Customer)
                                .Include(e => e.Payment)
                                .ThenInclude(e => e.Ticket)
                                .ThenInclude(e => e.Trip)
                                .Skip((page - 1) * pageSize)
                                .Take(pageSize)
                                .ToList();

            var totalRefunds = _context.Refunds.Count();
            var totalPages = (int)Math.Ceiling(totalRefunds / (double)pageSize);

            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            //Search
            if (!string.IsNullOrEmpty(searchString))
            {
                refunds = refunds.Where(r => r.Customer.Name.Contains(searchString) || r.Customer.Surname.Contains(searchString) || r.Customer.Email.Contains(searchString)).ToList();
            }

            return View(refunds);
        }
    }
}