using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Helpers;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class EmployeeController : Controller
    {
         private readonly ApplicationDbContext _context;

        public EmployeeController(ApplicationDbContext context)
        {
            _context = context;
        }

        //FAKER
        public IActionResult GenerateAndSaveFakeEmployees(int count = 10)
        {
            var roles = _context.Roles.ToList();
            if (roles.Count == 0)
            {
                return Content("error: there are no roles in the database");
            }

            var fakeEmployees = FakeDataGenerator.GenerateEmployees(count, roles);

            _context.Employees.AddRange(fakeEmployees);

            _context.SaveChanges();

            return RedirectToAction("Index");
        }

        // MVC - LIST
        public IActionResult Index(int page = 1, string searchString = "", int? roleFilter = null, string genderFilter = "")    
        {
            //Pagination
            int pageSize = 10; 
            var employees = _context.Employees
                                .Include(e => e.Role)
                                .Skip((page - 1) * pageSize)
                                .Take(pageSize)
                                .AsQueryable()
                                .ToList();

            var totalEmployees = _context.Employees.Count();
            var totalPages = (int)Math.Ceiling(totalEmployees / (double)pageSize);

            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            //Filters
            if (!string.IsNullOrEmpty(searchString))
            {
                employees = employees.Where(a => a.Name.Contains(searchString) || a.Surname.Contains(searchString) || a.Email.Contains(searchString) || a.PhoneNumber.Contains(searchString) || a.Gender.Contains(searchString) || a.Identity.Contains(searchString) || a.Salary.ToString().Contains(searchString)).ToList();
            }

            if (roleFilter.HasValue)
            {
                employees = employees.Where(e => e.RoleId == roleFilter.Value).ToList();
            }

            if (!string.IsNullOrEmpty(genderFilter))
            {
                employees = employees.Where(e => e.Gender == genderFilter).ToList();
            }
            
            ViewBag.RoleList = _context.Roles.Select(role => new SelectListItem
            {
                Value = role.Id.ToString(), 
                Text = role.Name            
            }).ToList();   

            ViewBag.GenderList = new List<SelectListItem>
            {
                new SelectListItem { Text = "Female", Value = "Female" },
                new SelectListItem { Text = "Male", Value = "Male" },
            };
            
            
            return View(employees);
        }

        // MVC - ADD EMPLOYEE VIEW
        public IActionResult AddEmployee()
        {
            var roles = _context.Roles.ToList(); 
            ViewBag.RoleList = new SelectList(roles, "Id", "Name"); 
            return View();
        }

        // MVC - ADD EMPLOYEE
        [HttpPost]
        public IActionResult AddEmployee(Employee employee)
        {

            if (!ModelState.IsValid)
            {
                _context.Employees.Add(employee);
                _context.SaveChanges();
                return RedirectToAction("Index","Employee");
            }

            ViewBag.RoleList = new SelectList(_context.Roles, "Id", "Name", employee.RoleId);            
            return View(employee);
        }

        // MVC - EDIT EMPLOYEE VIEW
        public IActionResult Edit(int id)
        {
            var roles = _context.Roles.ToList(); 
            ViewBag.RoleList = new SelectList(roles, "Id", "Name"); 
            var employee = _context.Employees.Find(id);
            return employee == null ? NotFound() : View(employee);
        }

        // MVC - EDIT EMPLOYEE
        [HttpPost("/Employee/Edit")]
        public IActionResult Edit(Employee employee)
        {
            if (!ModelState.IsValid)
            {
                _context.Employees.Update(employee);
                _context.SaveChanges();
                return RedirectToAction("Index","Employee");
            }

            ViewBag.RoleList = new SelectList(_context.Roles, "Id", "Name", employee.RoleId);            
            return View(employee);
        }

        // MVC - DELETE EMPLOYEE
        public IActionResult Delete(int id)
        {
            var employee = _context.Employees.Find(id);
            if (employee == null)
            {
                return NotFound();
            }

            _context.Employees.Remove(employee);
            _context.SaveChanges();
            return RedirectToAction("Index","Employee");
        }

        // MVC - VIEW MODAL
        [HttpGet("/Employee/Details/{id}")]
        public IActionResult Details(int id)
        {
            var employee = _context.Employees
                .Include(e => e.Role)
                .FirstOrDefault(e => e.Id == id);
            if (employee == null)
            {
                return NotFound();
            }

            var employeeDetails = new 
            {
                employee.Name,
                employee.Surname,
                Role = employee.Role?.Name,
                employee.Email,
                employee.PhoneNumber,
                employee.Gender,
                employee.Identity,
                Birthday = employee.Birthday.ToString("yyyy-MM-dd"),
                employee.Salary
            };

            return Json(employeeDetails);
        }
    }
}
