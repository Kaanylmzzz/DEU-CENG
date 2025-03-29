import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useLocation } from 'react-router-dom';
import "./Payment.css";
import visaLogo from './components/visa.png';
import mastercardLogo from './components/mastercard.png';
import amexLogo from './components/americanexpress.jpeg';
import troyLogo from './components/troy.png';
import cvcHelpImage from './components/cvc_help.png';
import PhoneInput from 'react-phone-input-2';
import logo from './components/Logo-Blue.jpg';
import 'react-phone-input-2/lib/style.css';
import apiConfig from './apiConfig';

const Payment = ({ customerId }) => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [name, setName] = useState("");
    const [identity, setId] = useState("");
    const [cardNumber, setCardNumber] = useState("");
    const [expiryDate, setExpiryDate] = useState("");
    const [cvc, setCvc] = useState("");
    const [errors, setErrors] = useState({});
    const [showCvcHelp, setShowCvcHelp] = useState(false);
    const location = useLocation();
    const { trip_id, from, to, departureTime, date, seatIds, genders, busId, totalCost } = location.state;

    // Function to validate form inputs
    const validateForm = () => {
        let formErrors = {};

        if (!email) {
            formErrors.email = "Cannot be left empty.";
        } else if (!/\S+@\S+\.\S+/.test(email)) {
            formErrors.email = "Please enter a valid email address.";
        }

        if (!phone) {
            formErrors.phone = "Cannot be left empty.";
        } else if (phone.length < 10) {
            formErrors.phone = "Phone number must have at least 10 digits.";
        }

        if (!identity) {
            formErrors.id = "Cannot be left empty.";
        } else if (!/^\d{11}$/.test(identity)) {
            formErrors.id = "ID number must be 11 digits.";
        }

        if (!name) {
            formErrors.name = "Cannot be left empty.";
        }

        if (!cardNumber) {
            formErrors.cardNumber = "Cannot be left empty.";
        } else if (!/^\d{16}$/.test(cardNumber.replace(/\s+/g, ''))) {
            formErrors.cardNumber = "Card number must be 16 digits.";
        }

        if (!cvc) {
            formErrors.cvc = "Cannot be left empty.";
        } else if (!/^\d{3}$/.test(cvc)) {
            formErrors.cvc = "CVC must be 3 digits.";
        }

        setErrors(formErrors);
        return Object.keys(formErrors).length === 0;
    };

    // Function to handle card number input change
    const handleCardNumberChange = (e) => {
        const value = e.target.value.replace(/[^0-9]/g, ""); // Remove non-numeric characters
        const formattedValue = value.replace(/(\d{4})(?=\d)/g, "$1 "); // Add space after every 4 digits
        setCardNumber(formattedValue);
    };

    // Function to handle expiry date input change
    const handleExpiryDateChange = (e) => {
        const value = e.target.value.replace(/[^0-9]/g, ""); // Remove non-numeric characters
        const formattedValue = value.replace(/(\d{2})(\d{2})/, "$1 / $2"); // Format as MM / YY
        setExpiryDate(formattedValue);
    };

    // Function to handle CVC input change
    const handleCvcChange = (e) => {
        const value = e.target.value.replace(/[^0-9]/g, ""); // Remove non-numeric characters
        setCvc(value);
    };

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        const now = new Date();
        const currentDate = now.toISOString().split('T')[0]; // Format date as YYYY-MM-DD
        const currentTime = now.toTimeString().split(' ')[0]; // Format time as HH:mm:ss
        const combinedDateTime = `${currentDate} ${currentTime}`;
    
        if (validateForm()) {
            const tickets = seatIds.map((seatId, index) => ({
                customerId: customerId || null,
                tripId: trip_id,
                from: from,
                to: to,
                seatNumber: seatId,
                departureTime: formatDateTime(date, departureTime),
                price: totalCost,
                status: "Confirmed",
                purchaseDate: combinedDateTime,
                gender: genders[index]?.charAt(0).toUpperCase() + genders[index]?.slice(1),
            }));
    
            try {
                // Send API requests for each ticket
                const responses = await Promise.all(
                    tickets.map((ticketData) =>
                        fetch(`${apiConfig.baseUrl}/api/TicketApi/AddTicket`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(ticketData),
                        })
                    )
                );
    
                // Check successful responses
                const successfulResponses = responses.filter((response) => response.ok);
    
                if (successfulResponses.length > 0) {
                    const ticketData = await Promise.all(successfulResponses.map((res) => res.json()));
    
                    // Create payment records for each ticket
                    const paymentResponses = await Promise.all(
                        ticketData.map((ticket) =>
                            fetch(`${apiConfig.baseUrl}/api/PaymentApi/AddPayment`, {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify({
                                    customerId: customerId || null,
                                    pnr: ticket.pnr, // Use PNR of each ticket
                                    status: "Completed",
                                    paymentDate: now.toISOString(), // ISO format payment date
                                    totalCost: ticket.price, // Use price of each ticket
                                }),
                            })
                        )
                    );
    
                    // Check successful payment responses
                    const successfulPayments = paymentResponses.filter((response) => response.ok);
    
                    if (successfulPayments.length === ticketData.length) {
                        const paymentData = await Promise.all(successfulPayments.map((res) => res.json()));
                        console.log('Payments created successfully:', paymentData);
    
                        navigate('/PaymentSuccess', {
                            state: { ticketInfo: ticketData, paymentInfo: paymentData },
                        });
                    } else {
                        console.error('Failed to create one or more payments');
                    }
                } else {
                    console.error('Failed to create one or more tickets');
                }
            } catch (error) {
                console.error('Error:', error);
            }
        }
    };

    // Function to format date and time
    const formatDateTime = (dateString, timeString) => {
        const dateObj = new Date(dateString);
    
        const day = dateObj.getDate(); 
        const month = dateObj.toLocaleString('en-US', { month: 'long' }); // Month name (January, February, ...)
        const weekday = dateObj.toLocaleString('en-US', { weekday: 'long' }); // Day name (Monday, ...)
    
        return `${day} ${month} ${weekday}  ${timeString}`;
    };

    return (
        <div className="payment-container">
            <form onSubmit={handleSubmit}>
                <div className="form-columns">
                    {/* Leftmost Column for Ticket Information */}
                    <div className="expanded-section">
                        <div className="form-section">
                            <div className="trip-header">
                                <img src={logo} alt="Logo" className="logo" />
                                <h2>TicketWise</h2>
                            </div>
                            <div className="trip-details">
                                <div className="trip-details-row">
                                    <div>
                                        <strong>From</strong>
                                    </div>
                                    <div>
                                        <strong>To</strong>
                                    </div>
                                </div>
                                <div className="trip-details-row">
                                    <div>{from}</div>
                                    <div>{to}</div>
                                </div>
                                <div className="trip-details-row">
                                    <div>
                                        <strong>Seat</strong>
                                    </div>
                                    <div>
                                        <strong>Departure</strong>
                                    </div>
                                </div>
                                <div className="trip-details-row departure-seat-row">
                                    <div>{Array.isArray(seatIds) ? seatIds.sort((a, b) => a - b).join(", ") : seatIds}</div>
                                    <div className="departure-time-container">
                                        <span className="departure-time">{formatDateTime(date, departureTime)}</span>
                                    </div>
                                </div>
                            </div>
                            <div className="trip-footer">
                                <p>
                                    You can turn the ticket to an open ticket, change or cancel your ticket up to 1 hour before the departure.
                                </p>
                            </div>
                        </div>
                    </div>

                    {/* Left Column */}
                    <div className="expanded-section">
                        <div className="form-section">
                            <div className="section-header">
                                <h2 className="section-title">Contact Information</h2>
                            </div>
                            <div className="form-group">
                                <label>Email:</label>
                                <input
                                    type="email"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    placeholder="Your email address"
                                />
                                {errors.email && <p className="error">{errors.email}</p>}
                            </div>
                            <div className="form-group">
                                <label>Phone Number:</label>
                                <PhoneInput
                                    country={'tr'}
                                    value={phone}
                                    onChange={setPhone}
                                    inputStyle={{ width: '100%' }}
                                />
                                {errors.phone && <p className="error">{errors.phone}</p>}
                                <div className="checkbox-container">
                                    <input
                                        type="checkbox"
                                        id="phoneConsent"
                                        style={{ marginRight: "8px" }}
                                    />
                                    <label htmlFor="phoneConsent" className="checkbox-label">
                                        To receive my ticket by FREE SMS, receive campaigns and announcements as commercial electronic messages, and that my personal data will be processed for marketing purposes.I consent .
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div className="form-section">
                            <div className="section-header">
                                <h2 className="section-title">Passenger Information</h2>
                            </div>
                            <div className="form-group">
                                <label>Full Name</label>
                                <input
                                    type="text"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    placeholder="Name and Surname"
                                />
                                {errors.name && <p className="error">{errors.name}</p>}
                            </div>
                            <div className="form-group">
                                <label>Identity</label>
                                <input
                                    type="text"
                                    value={identity}
                                    onChange={(e) => setId(e.target.value)}
                                    placeholder="Required by regulations."
                                />
                                {errors.id && <p className="error">{errors.id}</p>}
                            </div>
                        </div>
                    </div>

                    {/* Right Column */}
                    <div className="expanded-section">
                        <div className="form-section">
                            <div className="section-header">
                                <h2 className="section-title">Payment Information</h2>
                            </div>
                            <label className="debit-credit-label">Debit/Credit Card</label>
                            <div className="form-group">
                                <label>Card Number:</label>
                                <input
                                    type="text"
                                    value={cardNumber}
                                    onChange={handleCardNumberChange}
                                    placeholder="**** **** **** ****"
                                    maxLength={19} // Limit input to 19 characters including spaces
                                />
                                {errors.id && <p className="error">{errors.id}</p>}
                            </div>
                            <div className="form-group">
                                <label>Expiration Date:</label>
                                <input
                                    type="text"
                                    value={expiryDate}
                                    onChange={handleExpiryDateChange}
                                    placeholder="MM / YY"
                                    maxLength={7} // Limit input to 7 characters including slash
                                />
                                {errors.id && <p className="error">{errors.id}</p>}
                            </div>
                            <div className="form-group">
                                <label>
                                    CVC:
                                    <span
                                        onClick={() => setShowCvcHelp(!showCvcHelp)}
                                        style={{ marginLeft: "250px", color: "#007bff", cursor: "pointer" }}
                                    >
                                        CVC?
                                    </span>
                                </label>
                                <input
                                    type="text"
                                    value={cvc}
                                    onChange={handleCvcChange}
                                    placeholder="***"
                                    maxLength={3} // Limit input to 3 characters
                                />
                                {errors.id && <p className="error">{errors.id}</p>}
                                {showCvcHelp && (
                                    <img
                                        src={cvcHelpImage}
                                        alt="CVC Help"
                                        style={{
                                            marginLeft: "5px",
                                            height: "200px",
                                            verticalAlign: "middle",
                                        }}
                                    />
                                )}
                            </div>
                            <div className="payment-icons">
                                <img src={visaLogo} alt="Visa" />
                                <img src={mastercardLogo} alt="MasterCard" />
                                <img src={amexLogo} alt="American Express" />
                                <img src={troyLogo} alt="Troy" />
                            </div>
                        </div>
                        <button type="submit" className="submit-button"
                            onClick={handleSubmit}>
                            <div>{totalCost}</div>
                            Make Secure Payment
                        </button>
                    </div>
                </div>
            </form>
        </div>
    );
};

export default Payment;