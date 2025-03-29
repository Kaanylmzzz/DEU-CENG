using Microsoft.AspNetCore.Mvc;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class LoginController : Controller
    {
        private readonly ApplicationDbContext _context;

        public LoginController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: Login
        public IActionResult Login()
        {
            return View();
        }

        // POST: Login
        [HttpPost]
        public IActionResult Login(string email, string password)
        {
            var admin = _context.Admins.FirstOrDefault(u => u.Email == email && u.Password == password);
            
            if (admin != null)
            {
                // Store user info in session or claims for later use
                String AdminName = admin.Name + " " + admin.Surname;
                HttpContext.Session.SetString("AdminName", AdminName);
                return RedirectToAction("Index" , "Home"); // Redirect to the home screen
            }

            ViewBag.Error = "Invalid email or password.";
            return View();  
        }


        // Log out
        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Login");
        }
    

    }
}
