using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Ticketwise.Contexts;
using Ticketwise.Helpers;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class VehicleController : Controller
    {
         private readonly ApplicationDbContext _context;

        public VehicleController(ApplicationDbContext context)
        {
            _context = context;
        }

        //FAKER
        public IActionResult GenerateAndSaveFakeVehicles(int count = 10)
        {
            var fakeVehicles = FakeDataGenerator.GenerateVehicles(count);

            _context.Vehicles.AddRange(fakeVehicles);

            _context.SaveChanges();

            return RedirectToAction("Index");
        }

        // MVC - LIST
        public IActionResult Index(int page = 1, string searchString = "")
        {
            //Pagination
            int pageSize = 10; 
            var vehicles = _context.Vehicles
                                .Skip((page - 1) * pageSize)
                                .Take(pageSize)
                                .ToList();

            var totalVehicles = _context.Vehicles.Count();
            var totalPages = (int)Math.Ceiling(totalVehicles / (double)pageSize);

            ViewBag.CurrentPage = page;
            ViewBag.TotalPages = totalPages;

            //Filters
            if (!string.IsNullOrEmpty(searchString))
            {
                vehicles = vehicles.Where(v => v.LicensePlate.Contains(searchString)).ToList();
            }

            return View(vehicles);
        }

        // MVC - ADD VEHICLE VIEW
        public IActionResult AddVehicle()
        {
            return View();
        }

        // MVC - ADD VEHICLE
        [HttpPost]
        public IActionResult AddVehicle(Vehicle vehicle)
        {
            if (ModelState.IsValid)
            {
                _context.Vehicles.Add(vehicle);
                _context.SaveChanges();
                return RedirectToAction("Index","Vehicle");
            }
            return View(vehicle);
        }

        // MVC - EDIT VEHICLE VIEW
        public IActionResult Edit(int id)
        {
            var vehicle = _context.Vehicles.Find(id);
            return vehicle == null ? NotFound() : View(vehicle);
        }

        // MVC - EDIT VEHICLE
        [HttpPost("/Vehicle/Edit")]
        public IActionResult Edit(Vehicle vehicle)
        {
            if (ModelState.IsValid)
            {
                _context.Vehicles.Update(vehicle);
                _context.SaveChanges();
                return RedirectToAction("Index","Vehicle");
            }
            return View(vehicle);
        }

        // MVC - DELETE VEHICLE
        public IActionResult Delete(int id)
        {
            var vehicle = _context.Vehicles.Find(id);
            if (vehicle != null)
            {
                _context.Vehicles.Remove(vehicle);
                _context.SaveChanges();
            }
            return RedirectToAction("Index","Vehicle");
        }

    }
}
