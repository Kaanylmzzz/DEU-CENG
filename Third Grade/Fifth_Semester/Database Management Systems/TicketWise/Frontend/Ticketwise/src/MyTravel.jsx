import React, { useEffect, useState } from "react";
import "./MyTravel.css";
import apiConfig from './apiConfig';


function MyTravels({ id }) {
    const [futureTickets, setFutureTickets] = useState([]);
    const [pastTickets, setPastTickets] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const apiBaseUrl = `${apiConfig.baseUrl}/api/TicketApi/GetTicketsByCustomer?id=${id}`;

    // Function to format date and time
    const formatDate = (dateString, timeString) => {
        const date = new Date(`${dateString}T${timeString}`);
        const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

        const day = days[date.getDay()];
        const dayOfMonth = date.getDate();
        const month = months[date.getMonth()];
        const year = date.getFullYear();
        const time = date.toTimeString().slice(0, 5);

        return `${dayOfMonth} ${month} ${year} ${day} - ${time}`;
    };

    // Function to sort and group tickets into future and past tickets
    const sortAndGroupTickets = (tickets) => {
        const now = new Date();
        const futureTickets = tickets.filter(ticket => new Date(`${ticket.date}T${ticket.departureTime}`) >= now);
        const pastTickets = tickets.filter(ticket => new Date(`${ticket.date}T${ticket.departureTime}`) < now);

        futureTickets.sort((a, b) => new Date(`${a.date}T${a.departureTime}`) - new Date(`${b.date}T${b.departureTime}`));
        pastTickets.sort((a, b) => new Date(`${b.date}T${b.departureTime}`) - new Date(`${a.date}T${a.departureTime}`));

        setFutureTickets(futureTickets);
        setPastTickets(pastTickets);
    };

    useEffect(() => {
        if (!id) {
            setError("User ID is not provided");
            setLoading(false);
            return;
        }
    
        const fetchTickets = async () => {
            try {
                const response = await fetch(apiBaseUrl, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ userId: id }), // Include user ID in the POST request
                });
    
                if (!response.ok) {
                    throw new Error("Failed to fetch tickets");
                }
    
                const data = await response.json();
    
                if (data.success) {
                    sortAndGroupTickets(data.tickets);
                } else {
                    setError(data.message || "No purchased tickets found.");
                }
            } catch (error) {
                setError(error.message || "An unexpected error occurred.");
            } finally {
                setLoading(false);
            }
        };
    
        fetchTickets();
    }, [id]);
    
    if (loading) {
        return (
            <div style={{ textAlign: "center", marginTop: "20px" }}>
                <h2>Loading...</h2>
                <p>Please wait while we fetch your tickets.</p>
            </div>
        );
    }
    
    if (error) {
        return (
            <div style={{ textAlign: "center", marginTop: "50px", color: "#555" }}>
                <h2 style={{ color: "#FF5733", fontSize: "28px", marginBottom: "20px" }}>
                    Oops!
                </h2>
                <p style={{ fontSize: "22px", lineHeight: "1.6" }}>
                    It seems like you don't have any purchased tickets yet.
                </p>
                <p style={{ fontSize: "20px", color: "#777", marginTop: "10px" }}>
                    You can explore available expeditions and book your tickets anytime.
                </p>
            </div>
        );
    }

    return (
        <div className="my-travels-container">
            {futureTickets.length > 0 && (
                <>
                    <h2 className="section-header">My Future Date Tickets</h2>
                    {futureTickets.map((ticket, index) => (
                        <div key={index} className="my-travel">
                            <div className="travel-info">
                                <div className="travel-date">
                                    {formatDate(ticket.date, ticket.departureTime)}
                                </div>
                                <div className="travel-route">
                                    {ticket.origin}
                                    <span className="arrow"> → </span>
                                    {ticket.destination}
                                </div>
                            </div>

                            <div className="my-travels-details">
                                <div>
                                    <span className="highlight">Name:</span> {ticket.name} {ticket.surname}
                                </div>
                                <div>
                                    <span className="highlight">PNR Number:</span> {ticket.pnr}
                                </div>
                                <div>
                                    <span className="highlight">Seat Number:</span> {ticket.seat}
                                </div>
                                <div>
                                    <span className="highlight">Amount:</span> {ticket.cost} TL
                                </div>
                            </div>

                            <div className="my-travels-footer">
                                <div className="total-price">Total Amount: {ticket.cost} TL</div>
                                <div className="support-message">Please contact Live Support to process your ticket.</div>
                            </div>
                        </div>
                    ))}
                </>
            )}

            {pastTickets.length > 0 && (
                <>
                    <h2 className="section-header">My Past Date Tickets</h2>
                    {pastTickets.map((ticket, index) => (
                        <div key={index} className="my-travel past-ticket">
                            <div className="travel-info">
                                <div className="travel-date">
                                    {formatDate(ticket.date, ticket.departureTime)}
                                </div>
                                <div className="travel-route">
                                    {ticket.origin}
                                    <span className="arrow"> → </span>
                                    {ticket.destination}
                                </div>
                            </div>

                            <div className="my-travels-details">
                                <div>
                                    <span className="highlight">Name:</span> {ticket.name} {ticket.surname}
                                </div>
                                <div>
                                    <span className="highlight">PNR Number:</span> {ticket.pnr}
                                </div>
                                <div>
                                    <span className="highlight">Seat Number:</span> {ticket.seat}
                                </div>
                                <div>
                                    <span className="highlight">Amount:</span> {ticket.cost} TL
                                </div>
                            </div>

                            <div className="my-travels-footer">
                                <div className="total-price">Total Amount: {ticket.cost} TL</div>
                                <div className="support-message">Please contact Live Support to process your ticket.</div>
                            </div>
                        </div>
                    ))}
                </>
            )}
        </div>
    );
}

export default MyTravels;
