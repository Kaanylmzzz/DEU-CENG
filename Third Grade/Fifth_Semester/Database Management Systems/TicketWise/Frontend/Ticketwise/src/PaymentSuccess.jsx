import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./PaymentSuccess.css";

const PaymentSuccess = () => {
    const location = useLocation();
    const { ticketInfo } = location.state || {};
    const navigate = useNavigate(); // Used for navigation

    return (
        <div className="success-container">
            <div className="success-box">
                <h1>Payment Successful!</h1>
                <p>Thank you for your purchase. Your ticket details are below:</p>
                {Array.isArray(ticketInfo) && ticketInfo.length > 0 ? (
                    <div className="ticket-details">
                        <div className="ticket-row">
                            <strong>From:</strong> <span>{ticketInfo[0].trip.origin}</span>
                        </div>
                        <div className="ticket-row">
                            <strong>To:</strong> <span>{ticketInfo[0].trip.destination}</span>
                        </div>
                        <div className="ticket-row">
                            <strong>Departure:</strong> <span>{ticketInfo[0].trip.departureTime.substring(0, 5)}</span>
                        </div>
                        <div className="ticket-row">
                            <strong>Seat:</strong>{" "}
                            <span>
                                {ticketInfo.map((ticket) => ticket.seatNumber).join(", ")}
                            </span>
                        </div>
                        <div className="ticket-row">
                            <strong>PNR:</strong>{" "}
                            <span>{ticketInfo.map((ticket) => ticket.pnr).join(", ")}</span>
                        </div>
                        <div className="ticket-row">
                            <strong>Total Price:</strong>{" "}
                            <span>
                                {ticketInfo.reduce(
                                    (total, ticket) =>
                                        total + (ticket.trip.cost),
                                    0
                                )}{" "}
                                TL
                            </span>
                        </div>
                    </div>
                ) : (
                    <p>No ticket information available.</p>
                )}
                <button
                    className="return-button"
                    onClick={() => setTimeout(() => {
                        navigate('/'); // Redirect to home page after 1 second
                      }, 1000)}
                >
                    Return to Home
                </button>
            </div>
        </div>
    );
};

export default PaymentSuccess;