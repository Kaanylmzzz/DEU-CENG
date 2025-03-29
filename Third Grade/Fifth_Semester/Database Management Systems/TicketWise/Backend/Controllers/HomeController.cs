using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers;

public class HomeController : Controller
{
     private readonly ApplicationDbContext _context;

    public HomeController(ApplicationDbContext context)
    {
        _context = context;
    }

    public IActionResult Index()
    {
        
        try
        {
            // Retrieve data from SQL function
            var monthlyRevenues = _context.MonthlyRevenues
                                         .FromSqlRaw("SELECT * FROM GetMonthlyRevenues()")
                                         .ToList();

            // If no results are returned, throw an exception
            if (!monthlyRevenues.Any())
            {
                throw new Exception("No data returned from the database.");
            }

            // Create a list of 12 months with ordered data
            var revenues = Enumerable.Range(1, 12)
                                     .Select(month => monthlyRevenues
                                     .FirstOrDefault(r => r.Month == month)?.TotalRevenue ?? 0)
                                     .ToList();

            ViewBag.MonthlyRevenues = revenues;

            // Retrieve total customer number from SQL function
            var totalCustomers = _context.Database
                                        .SqlQuery<int>($"SELECT dbo.GetTotalCustomers()")
                                        .AsEnumerable()
                                        .FirstOrDefault();

            ViewBag.TotalCustomers = totalCustomers;

            var totalEmployees = _context.Database
                                      .SqlQuery<int>($"SELECT dbo.GetTotalEmployees()")
                                      .AsEnumerable()
                                      .FirstOrDefault();

            ViewBag.TotalEmployees = totalEmployees;

            var totalAdmins = _context.Database
                                      .SqlQuery<int>($"SELECT dbo.GetTotalAdmins()")
                                      .AsEnumerable()
                                      .FirstOrDefault();

            // Pass this value to the view
            ViewBag.TotalAdmins = totalAdmins;

        }
        catch (Exception ex)
        {
            ViewBag.Error = $"Error: {ex.Message}";
        }
        

        return View();
    }

    public IActionResult Privacy()
    {
        return View();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}
