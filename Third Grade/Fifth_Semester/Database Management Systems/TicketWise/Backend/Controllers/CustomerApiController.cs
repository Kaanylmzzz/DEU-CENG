using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]

    public class CustomerApiController : ControllerBase // API Controller olarak düzenlendi
    {
        private readonly ApplicationDbContext _context;

        public CustomerApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        //API - CUSTOMER LOGIN
        [HttpPost]
        public IActionResult Login([FromBody] LoginRequest request)
        {

            if (string.IsNullOrEmpty(request.Email) || string.IsNullOrEmpty(request.Password))
            {
                return BadRequest(new { message = "Email and password are required." });
            }

            var customer = _context.Customers
                .AsEnumerable()
                .FirstOrDefault(c => c.Email.Equals(request.Email, StringComparison.Ordinal) 
                                && c.Password.Equals(request.Password, StringComparison.Ordinal));

            if (customer == null)
            {
                return NotFound(new { message = "Invalid email or password. Please try again." });
            }

            return Ok(new 
            { 
                success = true,
                message = "Login successful", 
                customer = new 
                {
                    id = customer.Id,
                    email = customer.Email,
                    name = customer.Name,
                    surname = customer.Surname
                }
            });
        }

        // API - SING UP
        [HttpPost]
        public IActionResult AddCustomer([FromBody]Customer customer)
        {
            if (ModelState.IsValid)
            {
                _context.Customers.Add(customer);
                _context.SaveChanges();
                return Ok(customer);
            }

        
            return BadRequest(ModelState);
        }

        // API - MY INFORMATION
        [HttpPost]
        public IActionResult GetCustomerDetails([FromQuery]int id)
        {
            var customer = _context.Customers.FirstOrDefault(c => c.Id == id);
            if (customer == null)
            {
                return NotFound();
            }
            return Ok(customer);
        }
        
        // API - EDIT MY INFROMATION
        [HttpPatch]
        public IActionResult EditCustomer(int id, [FromBody] JsonPatchDocument<Customer> patchDoc)
        {
            if (patchDoc == null)
            {
                return BadRequest(new { message = "Invalid patch document." });
            }

            var customer = _context.Customers.FirstOrDefault(c => c.Id == id);
            if (customer == null)
            {
                return NotFound(new { message = "Customer not found." });
            }

            patchDoc.ApplyTo(customer, ModelState);

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                _context.Entry(customer).State = EntityState.Modified;
                _context.SaveChanges();
                return Ok(new { message = "Customer updated successfully.", customer });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "An error occurred while updating the customer.", error = ex.Message });
            }
        }

        // API - DELETE MY ACCOUNT
        [HttpDelete]
        public IActionResult DeleteCustomer(int id)
        {
            var customer = _context.Customers.FirstOrDefault(c => c.Id == id);
            if (customer == null)
            {
                return NotFound(new { message = "Customer not found." });
            }

            try
            {
                _context.Customers.Remove(customer);
                _context.SaveChanges();
                return Ok(new { message = "Customer deleted successfully." });
            }
            catch (Exception ex)
            {
                return StatusCode(500, new { message = "An error occurred while deleting the customer.", error = ex.Message });
            }
        }


    }
}