using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Identity.Client;
using Ticketwise.Contexts;
using Ticketwise.Helpers;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    public class TripController : Controller
    {
        private readonly ApplicationDbContext _context;

        public TripController(ApplicationDbContext context)
        {
            _context = context;
        }

        //FAKER
        public IActionResult GenerateAndSaveFakeTrips(int count = 200)
        {
            var vehicles = _context.Vehicles.ToList();

            if (vehicles.Count == 0)
            {
                return Content("error");
            }

            var fakeTrips = FakeDataGenerator.GenerateTrips(count, vehicles);

            _context.Trips.AddRange(fakeTrips);

            _context.SaveChanges();

            return RedirectToAction("Index");
        }

        // MVC - LIST
        public IActionResult Index(string origin = "", string destination = "", DateOnly? tripDate = null)
        {

            var currentDate = DateTime.Now.Date;
            var currentTime = DateTime.Now.TimeOfDay;

            var tripsQuery = _context.Trips.AsQueryable();

            // Filter trips based on origin, destination, and trip date
            if (!string.IsNullOrEmpty(origin) && !string.IsNullOrEmpty(destination) && tripDate != null)
            {
                tripsQuery = tripsQuery.Where(t => t.Origin == origin && t.Destination == destination && t.Date == tripDate.Value);
            }

            var filteredTrips = tripsQuery
                .Include(t => t.Vehicle)
                .OrderBy(t => t.Date)
                .ThenBy(t => t.DepartureTime)
                .ToList();

            // Passing the filtered trips to the view
            ViewBag.FilteredTrips = filteredTrips;

            // Update past trips status to "past"
            var pastTrips = filteredTrips
                .Where(t => t.Status != "past" && 
                            (t.Date < DateOnly.FromDateTime(currentDate) || 
                            (t.Date == DateOnly.FromDateTime(currentDate) && t.DepartureTime < TimeOnly.FromTimeSpan(currentTime))))
                .ToList();

            foreach (var trip in pastTrips)
            {
                trip.Status = "past";
            }

            // Update past trips status to "current"
            var currentTrips = filteredTrips
                .Where(t => t.Status != "current" && 
                            (t.Date > DateOnly.FromDateTime(currentDate) || 
                            (t.Date == DateOnly.FromDateTime(currentDate) && t.DepartureTime >= TimeOnly.FromTimeSpan(currentTime))))
                .ToList();

            foreach (var trip in currentTrips)
            {
                trip.Status = "current";
            }

            if (pastTrips.Any() || currentTrips.Any())
            {
                _context.SaveChanges();
            }

            // Retrive current trips    
            var currentTripsQuery = filteredTrips
                .Where(t => t.Status == "current")
                .OrderBy(t => t.Date)
                .ThenBy(t => t.DepartureTime);

            var currentTripsTotal = currentTripsQuery.Count();
            var currentTripsList = currentTripsQuery.ToList();

            // Retrieve past trips
            var pastTripsQuery = filteredTrips
                .Where(t => t.Status == "past")
                .OrderByDescending(t => t.Date)
                .ThenByDescending(t => t.DepartureTime);

            var pastTripsTotal = pastTripsQuery.Count();
            var pastTripsList = pastTripsQuery.ToList();

            // Gender status of the seats
            var seatGenders = new Dictionary<int, Dictionary<int, string>>();
            var allTrips = currentTripsList.Concat(pastTripsList).AsQueryable().ToList(); // Yalnızca gösterilen seferler için işlem yapıyoruz.

            foreach (var trip in allTrips)
            {
                var tripSeats = _context.Tickets
                    .Include(t => t.Customer)
                    .Where(t => t.TripId == trip.Id)
                    .ToDictionary(
                        t => t.SeatNumber,
                        t => t.Gender
                    );

                seatGenders[trip.Id] = tripSeats;
            }

            ViewBag.CurrentTrips = currentTripsList;
            ViewBag.PastTrips = pastTripsList;
            ViewBag.SeatGenders = seatGenders;

            var cities = new List<string>
            {
                "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray",
                "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
                "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt",
                "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
                "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
                "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan",
                "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
                "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul",
                "İzmir", "Kahramanmaraş", "Karabük", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir",
                "Kilis", "Kocaeli", "Konya", "Kütahya", "Malatya",
                "Manisa", "Mardin", "Mersin", "Muğla", "Muş",
                "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize",
                "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas",
                "Şanlıurfa", "Şırnak", "Tekirdağ", "Tokat", "Trabzon",
                "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"
            };

            ViewBag.Cities = cities;
            
            return View();

        }


        // MVC - ADD TRIP VIEW
        public IActionResult AddTrip()
        {

            var cities = new List<string>
            {
                "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray",
                "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
                "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt",
                "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
                "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
                "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan",
                "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
                "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul",
                "İzmir", "Kahramanmaraş", "Karabük", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir",
                "Kilis", "Kocaeli", "Konya", "Kütahya", "Malatya",
                "Manisa", "Mardin", "Mersin", "Muğla", "Muş",
                "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize",
                "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas",
                "Şanlıurfa", "Şırnak", "Tekirdağ", "Tokat", "Trabzon",
                "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"
            };

            ViewBag.Cities = cities.Select(city => new SelectListItem
            {
                Text = city,
                Value = city
            }).ToList();

            var vehicles = _context.Vehicles.ToList(); 
            ViewBag.VehicleList = new SelectList(vehicles, "Id", "LicensePlate");
            return View();
        }

        // MVC - ADD TRIP POST
        [HttpPost]
        public IActionResult AddTrip(Trip trip)
        {
            if (!ModelState.IsValid)
            {
                try
                {
                    // Current date and time
                    var currentDate = DateTime.Now.Date;
                    var currentTime = DateTime.Now.TimeOfDay;


                    if (trip.Date > DateOnly.FromDateTime(currentDate) ||
                        (trip.Date == DateOnly.FromDateTime(currentDate) && trip.DepartureTime >= TimeOnly.FromTimeSpan(currentTime)))
                    {
                        trip.Status = "current";
                    }
                    else
                    {
                        trip.Status = "past";
                    }

                    _context.Trips.Add(trip);
                    _context.SaveChanges();

                    return RedirectToAction("Index", "Trip");
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error while adding trip: {ex.Message}");
                    ViewBag.Error = "An error occurred while saving the trip.";
                }
            }


            var cities = new List<string>
            {
                "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray",
                "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
                "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt",
                "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
                "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
                "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan",
                "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
                "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul",
                "İzmir", "Kahramanmaraş", "Karabük", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir",
                "Kilis", "Kocaeli", "Konya", "Kütahya", "Malatya",
                "Manisa", "Mardin", "Mersin", "Muğla", "Muş",
                "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize",
                "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas",
                "Şanlıurfa", "Şırnak", "Tekirdağ", "Tokat", "Trabzon",
                "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"
            };

            ViewBag.Cities = cities.Select(city => new SelectListItem
            {
                Text = city,
                Value = city
            }).ToList();

            var vehicles = _context.Vehicles.ToList();
            ViewBag.VehicleList = new SelectList(vehicles, "Id", "LicensePlate");

            return View(trip);
        }

        // EDIT TRIP VIEW
        public IActionResult Edit(int id)
        {
            var trip = _context.Trips.Find(id);
            var cities = new List<string>
            {
                "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray",
                "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
                "Aydın", "Balıkesir", "Bartın", "Batman", "Bayburt",
                "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
                "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
                "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan",
                "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane",
                "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul",
                "İzmir", "Kahramanmaraş", "Karabük", "Karaman", "Kars",
                "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir",
                "Kilis", "Kocaeli", "Konya", "Kütahya", "Malatya",
                "Manisa", "Mardin", "Mersin", "Muğla", "Muş",
                "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize",
                "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas",
                "Şanlıurfa", "Şırnak", "Tekirdağ", "Tokat", "Trabzon",
                "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"
            };

            ViewBag.Cities = cities.Select(city => new SelectListItem
            {
                Text = city,
                Value = city
            }).ToList();

            var vehicles = _context.Vehicles.ToList(); 
            ViewBag.VehicleList = new SelectList(vehicles, "Id", "LicensePlate");
            return trip == null ? NotFound() : View(trip);
        }


        // EDIT TRIP POST
        [HttpPost("/Trip/Edit")]
        public IActionResult Edit(Trip trip)
        {
            if (!ModelState.IsValid)
            {
                try
                {
                    var currentDate = DateTime.Now.Date;
                    var currentTime = DateTime.Now.TimeOfDay;

                    if (trip.Date > DateOnly.FromDateTime(currentDate) ||
                        (trip.Date == DateOnly.FromDateTime(currentDate) && trip.DepartureTime >= TimeOnly.FromTimeSpan(currentTime)))
                    {
                        trip.Status = "current";
                    }
                    else
                    {
                        trip.Status = "past";
                    }

                    _context.Trips.Update(trip);
                    _context.SaveChanges();

                    return RedirectToAction("Index", "Trip");
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error while editing trip: {ex.Message}");
                    ViewBag.Error = "An error occurred while updating the trip.";
                }
            }

            return View(trip);
        }

        // DELETE TRIP
        public IActionResult Delete(int id)
        {
            var trip = _context.Trips.Find(id);
            if (trip != null)
            {
                _context.Trips.Remove(trip);
                _context.SaveChanges();
            }
            return RedirectToAction("Index","Trip");
        }




    }
}
