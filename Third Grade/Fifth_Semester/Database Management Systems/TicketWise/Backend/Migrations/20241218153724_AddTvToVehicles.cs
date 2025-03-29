using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Ticketwise.Migrations
{
    /// <inheritdoc />
    public partial class AddTvToVehicles : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "Tv",
                table: "Vehicles",
                nullable: false,
                defaultValue: false);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Tv",
                table: "Vehicles");
        }
    }
}
