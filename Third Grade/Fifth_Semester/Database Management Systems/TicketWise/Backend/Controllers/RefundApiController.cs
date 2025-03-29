using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]

    public class RefundApiController : ControllerBase // API Controller olarak düzenlendi
    {
        private readonly ApplicationDbContext _context;

        public RefundApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        //API - REFUND
        [HttpPost]
        public IActionResult AddRefund(Refund refund)
        {
            if (ModelState.IsValid)
            {
                _context.Refunds.Add(refund);
                _context.SaveChanges();
                return Ok(new { success = true, message = "Refund added successfully." });
            }
            return BadRequest(new { success = false, message = "Refund could not be added." });

        }

    }
}