using Microsoft.EntityFrameworkCore;
using Ticketwise;
using Ticketwise.Models;

namespace Ticketwise.Contexts;

public class ApplicationDbContext : DbContext

{
    public DbSet<Admin> Admins { get; set;}
    public DbSet<Role> Roles { get; set;}
    public DbSet<Employee> Employees { get; set;}
    public DbSet<Customer> Customers { get; set;}
    public DbSet<Vehicle> Vehicles { get; set;}
    public DbSet<Trip> Trips{ get; set;}
    public DbSet<Ticket> Tickets{ get; set;}
    public DbSet<Payment> Payments{ get; set;}
    public DbSet<Refund> Refunds{ get; set;}
    public DbSet<AdminCountDto> AdminCountDtos { get; set; }
    public DbSet<TicketDetail> TicketsWithDetails { get; set; }
    public DbSet<MonthlyRevenue> MonthlyRevenues { get; set; }


    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        // Define keyless entities
        modelBuilder.Entity<AdminCountDto>().HasNoKey();
        modelBuilder.Entity<TicketDetail>().HasNoKey(); // Indicates that the view has no primary key
        modelBuilder.Entity<Trip>()
        .ToTable("Trips", t => t.HasTrigger("trgUpdateTripStatus")); // Map to table with trigger
        modelBuilder.Entity<MonthlyRevenue>().HasNoKey();
    }

    [DbFunction]
    public static decimal GetMonthlyRevenue()
    {
        throw new NotSupportedException("This method is for SQL translation only.");
    }

    public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
    {
    }
    


}
