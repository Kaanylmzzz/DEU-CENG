﻿using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Ticketwise.Migrations
{
    /// <inheritdoc />
    public partial class AddPnrToTickets : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Pnr",
                table: "Tickets",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Pnr",
                table: "Tickets");
        }
    }
}
