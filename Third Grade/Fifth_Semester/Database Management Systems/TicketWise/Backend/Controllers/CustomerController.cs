using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Helpers;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{   

    // MVC Controller 
    public class CustomerController : Controller
    {
        private readonly ApplicationDbContext _context;

        public CustomerController(ApplicationDbContext context)
        {
            _context = context;
        }

        //FAKER
        public IActionResult GenerateAndSaveFakeCustomers(int count = 10)
        {
            
            var fakeCustomers = FakeDataGenerator.GenerateCustomers(count);
            _context.Customers.AddRange(fakeCustomers);
            _context.SaveChanges();

            return RedirectToAction("Index");
        }

        // MVC - LIST
        public IActionResult Index(int page = 1, string searchString = "", string genderFilter = "")
        {
            //Pagination
            int pageSize = 10; 
            var customers = _context.Customers
                                .Skip((page - 1) * pageSize)
                                .Take(pageSize)
                                .ToList();

            var totalCustomers = _context.Customers.Count();
            var totalPages = (int)Math.Ceiling(totalCustomers / (double)pageSize);

            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            //Search
            if (!string.IsNullOrEmpty(searchString))
            {
                customers = customers.Where(a => a.Name.Contains(searchString) || a.Surname.Contains(searchString) || a.Email.Contains(searchString) || a.PhoneNumber.Contains(searchString) || a.Gender.Contains(searchString) || a.Identity.Contains(searchString)).ToList();
            }

            if (!string.IsNullOrEmpty(genderFilter))
            {
                customers = customers.Where(e => e.Gender == genderFilter).ToList();
            }

            ViewBag.GenderList = new List<SelectListItem>
            {
                new SelectListItem { Text = "Female", Value = "Female" },
                new SelectListItem { Text = "Male", Value = "Male" },
            };
            
            return View(customers);
        }


        // MVC - Add Customer View
        public IActionResult AddCustomer()
        {
            return View();
        }

        // MVC - Add Customer
        [HttpPost]
        public IActionResult AddCustomer( Customer customer)
        {
            if (ModelState.IsValid)
            {
                _context.Customers.Add(customer);
                _context.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(customer);
        }

        // MVC - Edit Customer View

        public IActionResult Edit(int id)
        {
            var customer = _context.Customers.Find(id);
            if (customer == null)
            {
                return NotFound(); // Müşteri bulunamazsa 404 döner.
            }
            return View(customer); // Edit.cshtml görünümü döner.
        }

        // MVC - Edit Customer
        [HttpPost("/Customer/Edit")]
        public IActionResult Edit(Customer customer)
        {
            if (ModelState.IsValid)
            {
                try
                {
                    _context.Customers.Attach(customer);
                    _context.Entry(customer).State = EntityState.Modified;
                    _context.SaveChanges();

                    return RedirectToAction("Index","Customer"); 
                }
                catch (Exception ex)
                {
                    ModelState.AddModelError(string.Empty, "Bir hata oluştu: " + ex.Message);
                }
            }

            return View(customer);
        }


        // MVC - Delete Customer
        public IActionResult Delete(int id)
        {
            var customer = _context.Customers.Find(id);
            if (customer != null)
            {
                _context.Customers.Remove(customer);
                _context.SaveChanges();
            }
            return RedirectToAction("Index");
        }

        // MVC - View Modal
       
       [HttpGet("/Customer/Details/{id}")]
        public IActionResult Details(int id)
        {
            var customer = _context.Customers
                .FirstOrDefault(c => c.Id == id);
            if (customer == null)
            {
                return NotFound();
            }

            return Json(customer);
        }
    }
}
