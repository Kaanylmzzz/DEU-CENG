using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Ticketwise.Contexts;
using Ticketwise.Models;

namespace Ticketwise.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]

    public class TripApiController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public TripApiController(ApplicationDbContext context)
        {
            _context = context;
        }

        // API - LIST TRIPS
        [HttpPost]
        public async Task<IActionResult> GetTrips([FromQuery] string from, [FromQuery] string to, [FromQuery] DateOnly date)
        {

            try{
                var trips = await _context.Trips
                    .Include(t => t.Vehicle)
                    .Where(t => t.Origin == from && t.Destination == to && t.Date == date)
                    .OrderBy(t => t.DepartureTime)
                    .ToListAsync();

                if (!trips.Any())
                {
                    throw new KeyNotFoundException("Seçilen güzergah için bilet bulunamadı.");
                }

                return Ok(new
                {
                    Success = true,
                    Data = trips.Select(t => new
                    {
                        t.Id,
                        t.Origin,
                        t.Destination,
                        t.Date,
                        t.DepartureTime,
                        t.TravelTime,
                        t.Cost,
                        Vehicle = new
                        {
                            t.Vehicle.LicensePlate,
                            t.Vehicle.Wifi,
                            t.Vehicle.Aircondition,
                            t.Vehicle.Socket,
                            t.Vehicle.Tv,
                            t.Vehicle.Service
                        }
                    })
                });
            }
            catch (ArgumentException ex)
            {
                return BadRequest(new { Success = false, Message = ex.Message });
            }
            catch (KeyNotFoundException ex)
            {
                return NotFound(new { Success = false, Message = ex.Message });

            }
            
        }

        // API - GET SEATS
        [HttpPost]
        public IActionResult GetSeats(int tripId)
        {

            var seatList = _context.Tickets
            .Where(t => t.TripId == tripId)
            .Include(t => t.Customer)
            .Select(t => new 
            {
                SeatNumber = t.SeatNumber,
                Gender = t.Gender,
            })
            .ToList();

            if (!seatList.Any())
            {
                return NotFound();
            }

            return Ok(seatList);
        }

    }
}