using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Ticketwise.Migrations
{
    /// <inheritdoc />
    public partial class AddServiceToVehicles : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "Service",
                table: "Vehicles",
                type: "bit",
                nullable: false,
                defaultValue: false);

        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Service",
                table: "Vehicles");

        }
    }
}
