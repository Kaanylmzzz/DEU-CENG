using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class AdminController : Controller
    {
        private readonly ApplicationDbContext _context;

        public AdminController(ApplicationDbContext context)
        {
            _context = context;
        }

        public IActionResult Index(int page = 1, string searchString = "")
        {
            int pageSize = 10;

            // Retrieve paginated admin records using a stored procedure
            var admins = _context.Admins
                .FromSqlRaw("EXEC GetAdmins @SearchString = {0}, @Page = {1}, @PageSize = {2}", 
                            searchString, page, pageSize)
                .AsEnumerable() // Daha fazla sorgu kompozisyonunu engeller
                .ToList();

            // Retrieve total admin count using a separate query
            var totalAdmins = _context.Set<AdminCountDto>()
                .FromSqlRaw("EXEC GetAdminCount @SearchString = {0}", searchString)
                .AsEnumerable()
                .Select(a => a.TotalCount) // DTO'daki sütun adı
                .FirstOrDefault();

            // Calculate total number of pages
            var totalPages = (int)Math.Ceiling(totalAdmins / (double)pageSize);

           // Send page information to the view
            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            return View(admins);
        }

        public IActionResult AddAdmin()
        {
            return View();
        }

        [HttpPost]
        public IActionResult AddAdmin(Admin admin)
        {
            if (ModelState.IsValid)
            {
                _context.Database.ExecuteSqlRaw(
                    "EXEC AddAdmin @Name = {0}, @Surname = {1}, @Email = {2}, @Password = {3}", 
                    admin.Name, admin.Surname, admin.Email, admin.Password);

                return RedirectToAction("Index");
            }
            return View(admin);
        }

        public IActionResult Edit(int id)
        {
            var admin = _context.Admins.Find(id);

            return admin == null ? NotFound() : View(admin);
        }

        [HttpPost]
        public IActionResult Edit(Admin admin)
        {
            if (ModelState.IsValid)
            {
                _context.Database.ExecuteSqlRaw(
                    "EXEC UpdateAdmin @Id = {0}, @Name = {1}, @Surname = {2}, @Email = {3}, @Password = {4}", 
                    admin.Id, admin.Name, admin.Surname, admin.Email, admin.Password);

                return RedirectToAction("Index");
            }
            return View(admin);
        }

        public IActionResult Delete(int id)
        {
            var rowsAffected = _context.Database.ExecuteSqlRaw("EXEC DeleteAdmin @Id = {0}", id);

            if (rowsAffected == 0)
            {
                return NotFound();
            }

            return RedirectToAction("Index");
        }
    }
}
