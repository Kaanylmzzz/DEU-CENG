using Bogus;
using System;
using System.Collections.Generic;
using Ticketwise.Models;

namespace Ticketwise.Helpers
{
    public class FakeDataGenerator
    {
        public static List<Employee> GenerateEmployees(int count, List<Role> roles)
        {
            
            var faker = new Faker<Employee>()
                .RuleFor(e => e.Role, f => f.PickRandom(roles)) 
                .RuleFor(e => e.RoleId, (f, e) => e.Role.Id) 
                .RuleFor(e => e.Name, f => f.Name.FirstName()) 
                .RuleFor(e => e.Surname, f => f.Name.LastName()) 
                .RuleFor(e => e.Email, f => f.Internet.Email()) 
                .RuleFor(e => e.PhoneNumber, f => f.Phone.PhoneNumber()) 
                .RuleFor(e => e.Gender, f => f.PickRandom(new[] { "Male", "Female" })) 
                .RuleFor(e => e.Identity, f => f.Random.ReplaceNumbers("###########")) 
                .RuleFor(e => e.Birthday, f => DateOnly.FromDateTime(f.Date.Past(30, DateTime.Today.AddYears(-18)))) 
                .RuleFor(e => e.Salary, f => Math.Round(f.Random.Double(3000, 15000), 2)); 

            return faker.Generate(count);
        }

        public static List<Customer> GenerateCustomers(int count)
        {
            var faker = new Faker<Customer>()
                .RuleFor(c => c.Name, f => f.Name.FirstName()) 
                .RuleFor(c => c.Surname, f => f.Name.LastName()) 
                .RuleFor(c => c.Email, f => f.Internet.Email()) 
                .RuleFor(c => c.PhoneNumber, f => f.Phone.PhoneNumber()) 
                .RuleFor(c => c.Gender, f => f.PickRandom(new[] { "Male", "Female" })) 
                .RuleFor(c => c.Identity, f => f.Random.ReplaceNumbers("###########")) 
                .RuleFor(c => c.Birthday, f => DateOnly.FromDateTime(f.Date.Past(30, DateTime.Today.AddYears(-18)))) 
                .RuleFor(c => c.Password, f => f.Internet.Password(8, true)); 

            return faker.Generate(count);
        }

        public static List<Vehicle> GenerateVehicles(int count)
        {
            var faker = new Faker<Vehicle>()
                .RuleFor(v => v.LicensePlate, f => GenerateTurkishLicensePlate(f)) 
                .RuleFor(v => v.Capacity, 37) 
                .RuleFor(v => v.Wifi, f => f.Random.Bool(0.7f)) 
                .RuleFor(v => v.Aircondition, f => f.Random.Bool(0.9f)) 
                .RuleFor(v => v.Socket, f => f.Random.Bool(0.8f))
                .RuleFor(v => v.Tv, f => f.Random.Bool(0.5f)) 
                .RuleFor(v => v.Service, f => f.Random.Bool(0.6f));

            return faker.Generate(count);
        }

        private static string GenerateTurkishLicensePlate(Faker faker)
        {
            
            var plateCode = faker.Random.Number(1, 81).ToString("00"); 
            var letters = faker.Random.String2(3, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
            var numbers = faker.Random.Number(1, 9999).ToString("0000"); 
            return $"{plateCode} {letters} {numbers}";
        }

        public static readonly Dictionary<string, Dictionary<string, double>> CityDistances = new Dictionary<string, Dictionary<string, double>>
        {
            { "İstanbul", new Dictionary<string, double>
                {
                    { "Ankara", 450 },
                    { "İzmir", 350 },
                    { "Antalya", 720 },
                    { "Bursa", 150 },
                    { "Eskişehir", 230 }
                    
                }
            },
            { "Ankara", new Dictionary<string, double>
                {
                    { "İstanbul", 450 },
                    { "İzmir", 580 },
                    { "Antalya", 480 },
                    { "Bursa", 350 },
                    { "Eskişehir", 230 }
                    
                }
            },
            { "İzmir", new Dictionary<string, double>
                {
                    { "İstanbul", 350 },
                    { "Ankara", 580 },
                    { "Antalya", 460 },
                    { "Bursa", 220 },
                    { "Eskişehir", 420 }
                    
                }
            },
            { "Antalya", new Dictionary<string, double>
                {
                    { "İstanbul", 720 },
                    { "Ankara", 480 },
                    { "İzmir", 460 },
                    { "Bursa", 620 },
                    { "Eskişehir", 480 }
                }
            },
            { "Bursa", new Dictionary<string, double>
                {
                    { "İstanbul", 150 },
                    { "Ankara", 350 },
                    { "İzmir", 220 },
                    { "Antalya", 620 },
                    { "Eskişehir", 180 }
                }
            },
            { "Eskişehir", new Dictionary<string, double>
                {
                    { "İstanbul", 260 },
                    { "Ankara", 220 },
                    { "İzmir", 520 },
                    { "Antalya", 230 },
                    { "Bursa", 330 }
                }
            }
            
        };

        private static readonly string[] ValidCities = CityDistances.Keys.ToArray();
        public static List<Trip> GenerateTrips(int count, List<Vehicle> vehicles)
        {
            
            var faker = new Faker<Trip>()
                .RuleFor(t => t.Vehicle, f => f.PickRandom(vehicles)) 
                .RuleFor(t => t.VehicleId, (f, t) => t.Vehicle.Id) 
                .RuleFor(t => t.Origin, f => f.PickRandom(ValidCities)) 
                .RuleFor(t => t.Destination, (f, t) => f.PickRandom(ValidCities)) 
                .RuleFor(t => t.Date, f => DateOnly.FromDateTime(f.Date.Between(new DateTime(2025, 1, 13), new DateTime(2025, 1, 27)))) 
                .RuleFor(t => t.DepartureTime, f => TimeOnly.FromTimeSpan(f.Date.Recent(30).TimeOfDay)) 
                .RuleFor(t => t.Status, f => f.PickRandom(new[] { "current", "past" })) 
                .RuleFor(t => t.TravelTime, (f, t) => GenerateRealisticTravelTime(t.Origin, t.Destination)) 
                .RuleFor(t => t.Cost, (f, t) => GenerateCost(t.Origin, t.Destination)); 

            return faker.Generate(count);
        }

        private static double GenerateRealisticTravelTime(string origin, string destination)
        {
            if (CityDistances.ContainsKey(origin) && CityDistances[origin].ContainsKey(destination))
            {
                
                double distance = CityDistances[origin][destination];
                double travelTime = distance / 80.0; 
                double fractionalPart = new Faker().Random.Bool() ? 0.5 : 0; 
                double totalTime = travelTime + fractionalPart;
                return Math.Round(totalTime * 2) / 2; 
            }
            return 0; 
        }

        private static int GenerateCost(string origin, string destination)
        {
            if (CityDistances.ContainsKey(origin) && CityDistances[origin].ContainsKey(destination))
            {
                
                double distance = CityDistances[origin][destination];
                return (int)Math.Round(distance * 1,5); 
            }
            return 0; 
        }

        public static List<Ticket> GenerateTickets(int count, List<Customer> customers, List<Trip> trips)
        {
            var faker = new Faker<Ticket>()
                .RuleFor(t => t.Customer, f => f.PickRandom(customers)) // Rastgele bir müşteri seç
                .RuleFor(t => t.CustomerId, (f, t) => t.Customer.Id) // Seçilen müşterinin ID'si
                .RuleFor(t => t.Trip, f => f.PickRandom(trips)) // Rastgele bir sefer seç
                .RuleFor(t => t.TripId, (f, t) => t.Trip.Id) // Seçilen seferin ID'si
                .RuleFor(t => t.SeatNumber, f => f.Random.Number(1, 45)) // 1-45 arasında rastgele bir koltuk numarası
                .RuleFor(t => t.Status, f => f.PickRandom(new[] { "Completed", "Pending"})) // Durum: Purchased, Cancelled veya Used
                .RuleFor(t => t.PurchaseDate, f => f.Date.Recent()) // En son satın alma tarihini kullan
                .RuleFor(t => t.Gender, f => f.PickRandom(new[] { "Male", "Female" })) // Cinsiyet: Male veya Female
                .RuleFor(t => t.Pnr, f => f.Random.AlphaNumeric(6).ToUpper()); // 6 karakterli rastgele PNR oluştur

            return faker.Generate(count);
        }

        public static List<Payment> GeneratePayments(int count, List<Customer> customers, List<Ticket> tickets)
        {
            var faker = new Faker<Payment>()
                .RuleFor(p => p.Customer, f => f.PickRandom(customers)) // Rastgele bir müşteri seçimi
                .RuleFor(p => p.CustomerId, (f, p) => p.Customer?.Id) // Seçilen müşterinin ID'si
                .RuleFor(p => p.Ticket, f => f.PickRandom(tickets)) // Rastgele bir bilet seçimi
                .RuleFor(p => p.TicketId, (f, p) => p.Ticket.Id) // Seçilen biletin ID'si
                .RuleFor(p => p.TranscationId, f => f.Random.AlphaNumeric(12)) // Rastgele bir işlem ID'si
                .RuleFor(p => p.Status, f => f.PickRandom(new[] { "Completed", "Pending", "Failed" })) // Ödeme durumu
                .RuleFor(p => p.PaymentDate, f => f.Date.Between(DateTime.Now.AddMonths(-6), DateTime.Now)); // Geçmiş 6 ay içinde bir ödeme tarihi

            return faker.Generate(count);
        }

    }
}
