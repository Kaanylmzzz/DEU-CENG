import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import clockIcon from './components/Clock.png';
import seatIcon from './components/Seat.png';
import busFrame from './components/Bus.png';
import wifiIcon from './components/Wifi.png';
import electricityIcon from './components/Electricity.png';
import tvIcon from './components/TV.png';
import cateringIcon from './components/Catering.png';
import Logo from './components/Logo-White.jpg';
import male_Icon from './components/Male.png';
import female_Icon from './components/Female.png';
import './Expedition.css';
import apiConfig from './apiConfig';

function Expedition() {
  const [searchParams] = useSearchParams();
  const [from, setFrom] = useState(searchParams.get('from') || "");
  const [to, setTo] = useState(searchParams.get('to') || "");
  const [date, setDate] = useState(() => {
    const paramDate = searchParams.get('date');
    return paramDate ? new Date(paramDate) : new Date(); // Use URL date if available, otherwise today
  });
  const [tempFrom, setTempFrom] = useState(from);
  const [tempTo, setTempTo] = useState(to);
  const [filteredBuses, setFilteredBuses] = useState([]);
  const [isSearchClicked, setIsSearchClicked] = useState(false);
  const [selectedBus, setSelectedBus] = useState(null);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [selectedSeatsWithGender, setSelectedSeatsWithGender] = useState([]);
  const [trips, setTrips] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [genderSelection, setGenderSelection] = useState({ seatId: null, gender: null });
  const navigate = useNavigate();

  const cities = [
    "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydın",
    "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale",
    "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir",
    "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul", "İzmir", "Kahramanmaraş",
    "Karabük", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir", "Kilis", "Kocaeli", "Konya",
    "Kütahya", "Malatya", "Manisa", "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize", "Sakarya",
    "Samsun", "Siirt", "Sinop", "Sivas", "Şanlıurfa", "Şırnak", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"
  ];
  
  const fetchAndCreateSeats = async (totalColumns, tripId) => {
    if (!tripId || (typeof tripId !== 'string' && typeof tripId !== 'number')) {
      throw new Error(`Invalid tripId: ${tripId}`);
    }
  
    try {
      const response = await axios.post(`${apiConfig.baseUrl}/api/TripApi/GetSeats?tripId=${tripId}`);
      const filledSeats = response?.data;
  
      if (!Array.isArray(filledSeats)) {
        throw new Error("Invalid seats data format from API");
      }
  
      const seats = [];
      const seatLayout = [
        { row: 0, start: 4, step: 4 },
        { row: 1, start: 3, step: 4 },
        { row: 2, start: null }, // Corridor row
        { row: 3, start: 1, step: 4 },
      ];
  
      seatLayout.forEach((layout) => {
        if (layout.start !== null) {
          for (let col = 0; col < totalColumns; col++) {
            const seatId = layout.start + col * layout.step;
            const filledSeat = filledSeats.find((seat) => seat.seatNumber === seatId);
  
            if (filledSeat) {
              seats.push({
                id: seatId,
                status: 'filled',
                gender: filledSeat.gender.toLowerCase(),
              });
            } else {
              const status = (seatId === 27 || seatId === 28) ? 'corridor' : 'empty';
              seats.push({ id: seatId, status });
            }
          }
        } else {
          for (let col = 0; col < totalColumns; col++) {
            seats.push({ id: `corridor-${col}`, status: 'corridor' });
          }
        }
      });
  
      return seats;
    } catch (err) {
      console.error("Error fetching or creating seats:", err.message);
  
      // If no seat data is available, create all seats as empty
      const emptySeats = [];
      const seatLayout = [
        { row: 0, start: 4, step: 4 },
        { row: 1, start: 3, step: 4 },
        { row: 2, start: null }, // Corridor row
        { row: 3, start: 1, step: 4 },
      ];
  
      seatLayout.forEach((layout) => {
        if (layout.start !== null) {
          for (let col = 0; col < totalColumns; col++) {
            const seatId = layout.start + col * layout.step;
            const status = (seatId === 27 || seatId === 28) ? 'corridor' : 'empty';
            emptySeats.push({ id: seatId, status });
          }
        } else {
          for (let col = 0; col < totalColumns; col++) {
            emptySeats.push({ id: `corridor-${col}`, status: 'corridor' });
          }
        }
      });
  
      return emptySeats; // Return empty seat layout
    }
  };
  
  const fetchTrips = async () => {
    if (!from || !to || !date) {
      throw new Error("Please enter a valid 'from', 'to', and 'date' value.");
    }
  
    setLoading(true);
    setError(null);
  
    try {
      const formattedDate = date.toISOString().split('T')[0];
  
      const response = await axios.post(
        `${apiConfig.baseUrl}/api/TripApi/GetTrips?from=${from}&to=${to}&date=${formattedDate}`
      );
  
      const tripsData = response?.data?.data;
      if (!Array.isArray(tripsData)) {
        console.error("API Response Data is invalid or not an array:", tripsData);
        throw new Error("Invalid data format from API");
      }
  
      if (tripsData.length === 0) {
        setError("Sorry, there is no suitable trip for you on the date you are looking for.");
        setFilteredBuses([]); // Clear the list
        return;
      }
  
      // Map the received data
      const mappedBuses = await Promise.all(
        tripsData.map(async (trip, index) => {
          const vehicle = trip.vehicle || {};
  
          // Fetch seats with fetchAndCreateSeats
          const seats = await fetchAndCreateSeats(13, trip.id);

          // Format departure time
          const departureTime = trip.departureTime.split(":").slice(0, 2).join(":");
  
          // Format travel time
          const hours = Math.floor(trip.travelTime);
          const minutes = Math.round((trip.travelTime - hours) * 60);
          const formattedTravelTime = minutes
            ? `${hours} hours ${minutes} minutes`
            : `${hours} hours`;
  
          return {
            id: index + 1,
            trip_id: trip.id,
            companyLogo: Logo,
            companyName: 'TicketWise',
            departureTime: departureTime,
            duration: formattedTravelTime,
            type: '2+1',
            from: trip.origin,
            to: trip.destination,
            price: `${trip.cost} TL`,
            seats,
            amenities: {
              wifi: vehicle.wifi,
              electricity: vehicle.socket,
              tv: vehicle.tv,
              catering: vehicle.service,
            },
          };
        })
      );
  
      setFilteredBuses(mappedBuses);
    } catch (err) {
      if (err.response && err.response.status === 404) {
        setError("Sorry, there is no suitable trip for you on the date you are looking for.");
      } else {
        console.error("Error fetching trips:", err);
        setError("An error occurred. Please try again.");
      }
      setFilteredBuses([]); // Clear the list
    } finally {
      setLoading(false);
    }
  };  
  
  // Perform search if from, to, and date are filled on initial load
  // Effect to handle potential updates in URL parameters
  useEffect(() => {
    const paramFrom = searchParams.get('from');
    const paramTo = searchParams.get('to');
    const paramDate = searchParams.get('date');

    if (paramFrom) setFrom(paramFrom);
    if (paramTo) setTo(paramTo);
    if (paramDate) setDate(new Date(paramDate));
    setIsSearchClicked(true);
  }, [searchParams]);
  
  // Triggered when the user performs a search
  useEffect(() => {
    if (isSearchClicked) {
      fetchTrips();
      setIsSearchClicked(false); // Reset isSearchClicked
    }
  }, [isSearchClicked]); // Only runs when isSearchClicked changes

  const switchPlaces = () => {
    setTempFrom(tempTo);
    setTempTo(tempFrom);
  };

  const handleDateChange = (e) => {
    setDate(new Date(e.target.value));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setFrom(tempFrom);
    setTo(tempTo);

    // Save to localStorage
    localStorage.setItem('from', tempFrom);
    localStorage.setItem('to', tempTo);
    localStorage.setItem('date', date.toISOString());

    // Update URL
    navigate(`?from=${tempFrom}&to=${tempTo}&date=${date.toISOString().split('T')[0]}`);

    setIsSearchClicked(true);
  };

  const handleSeatClick = (seatId) => {
    const busWithSeat = filteredBuses.find((bus) =>
      bus.seats.some((seat) => seat.id === seatId && seat.status === 'corridor')
    );
  
    if (busWithSeat) {
      console.log('Seat is in corridor, skipping.');
      return;
    }
  
    const seat = selectedSeatsWithGender.find((s) => s.seatId === seatId);
  
    if (seat) {
      console.log('Seat already selected, unselecting.');
      setSelectedSeatsWithGender((prev) =>
        prev.filter((s) => s.seatId !== seatId)
      );
      setFilteredBuses((prevBuses) =>
        prevBuses.map((bus) => ({
          ...bus,
          seats: bus.seats.map((seat) =>
            seat.id === seatId ? { ...seat, status: 'empty', gender: null } : seat
          ),
        }))
      );
    } else {
      setGenderSelection({ seatId, gender: null });
    }
  };
  
  const handleGenderSelection = (seatId, gender) => {
    // Mark the seat with the selected gender and update the status
    setSelectedSeatsWithGender((prev) => [...prev, { seatId, gender }]);
    setFilteredBuses((prevBuses) =>
      prevBuses.map((bus) => ({
        ...bus,
        seats: bus.seats.map((seat) =>
          seat.id === seatId ? { ...seat, status: 'selected', gender } : seat
        ),
      }))
    );
  
    // Close the gender selection form in the next render cycle
    setTimeout(() => {
      setGenderSelection({ seatId: null, gender: null });
    }, 0);
  };
  
  // Close gender selection form
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (genderSelection.seatId && !event.target.closest(".seat")) {
        // If gender selection is open and the clicked element is not a seat, close it
        setGenderSelection({ seatId: null, gender: null });
      }
    };
  
    document.addEventListener("click", handleClickOutside);
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, [genderSelection]);
  
  const handleBusSelection = (busId) => {
    if (selectedBus === busId) {
      setSelectedBus(null);
    } else {
      setSelectedSeatsWithGender([]);
      setGenderSelection({ seatId: null, gender: null });
  
      setFilteredBuses((prevBuses) =>
        prevBuses.map((bus) => {
          const updatedSeats = Array.isArray(bus.seats)
            ? bus.seats.map((seat) =>
                seat.status === "selected"
                  ? { ...seat, status: "empty", gender: null }
                  : seat
              )
            : [];
          return { ...bus, seats: updatedSeats };
        })
      );
  
      setSelectedBus(busId);
    }
  };
  
  const calculateTotalCost = () => {
    if (!selectedBus) return 0;
    
    const selectedBusDetails = filteredBuses.find((bus) => bus.id === selectedBus);
    if (!selectedBusDetails) return 0;
  
    // Calculate the total cost by converting the ticket price to a number
    const ticketPrice = parseFloat(selectedBusDetails.price.replace(' TL', ''));
    return selectedSeatsWithGender.length * ticketPrice;
  };

  const sortedSeatsWithGender = [...selectedSeatsWithGender].sort((a, b) => a.seatId - b.seatId);
  const handleProceedToPayment = () => {
    if (!selectedBus || selectedSeatsWithGender.length === 0) {
        alert('Please select a bus and at least one seat!');
        return;
    }

    // Get the details of the selected bus
    const selectedBusDetails = filteredBuses.find(bus => bus.id === selectedBus);

    if (!selectedBusDetails) {
        alert('Selected bus details not found!');
        return;
    }

    // Convert the price to a number and calculate the total cost
    const ticketPrice = parseFloat(selectedBusDetails.price.replace(' TL', ''));
    const totalCost = ticketPrice * selectedSeatsWithGender.length;
    // Navigate to the payment screen
    navigate('/payment', {
        state: {
            trip_id: selectedBusDetails.trip_id,
            from: from,
            to: to,
            departureTime: selectedBusDetails.departureTime,
            date: date.toISOString().split('T')[0],
            seatIds: sortedSeatsWithGender.map(seat => seat.seatId), // Only seatId array
            genders: sortedSeatsWithGender.map(seat => seat.gender), // Only gender array
            busId: selectedBusDetails.id,
            totalCost: `${totalCost} TL`, // Send total cost
        },
    });
};

  return (
    <div className="expedition-container">
      <div className="search-container">
        <form className="search-form" onSubmit={handleSubmit}>
          <select
            value={tempFrom}
            onChange={(e) => setTempFrom(e.target.value)}
            required
          >
            <option value="" disabled>From</option>
            {cities.map((city, index) => (
              <option key={index} value={city}>{city}</option>
            ))}
          </select>
  
          <div className="switch-button" onClick={switchPlaces}>
            ⇄
          </div>
  
          <select
            value={tempTo}
            onChange={(e) => setTempTo(e.target.value)}
            required
          >
            <option value="" disabled>To</option>
            {cities.map((city, index) => (
              <option key={index} value={city}>{city}</option>
            ))}
          </select>
  
          <input
            type="date"
            value={date.toISOString().split('T')[0]}
            onChange={(e) => handleDateChange(e)}
            min={new Date().toISOString().split('T')[0]}
            required
          />
  
          <button type="submit">Search Bus</button>
        </form>
      </div>
  
      {filteredBuses.length === 0 ? (
        <p className="no-buses-message">{error}</p>
      ) : (
        filteredBuses.map((bus) => (
          <div key={bus.id} className="bus-card">
            <div className="bus-info">
              <img src={bus.companyLogo} alt={bus.companyName} className="company-logo" />
              <div className="details">
                <div className="time-container">
                  <img src={clockIcon} alt="clock" className="clock-icon" />
                  <div>
                    <p className="time">{bus.departureTime}</p>
                    <p className="duration">{bus.duration}</p>
                  </div>
                </div>
                <div className="type-container">
                  <img src={seatIcon} alt="seat" className="seat-icon" />
                  <p className="type">{bus.type}</p>
                </div>
                <p className="route">{bus.from} → {bus.to}</p>
              </div>
              <div className="price">{bus.price}</div>
              <button
                className="seat-select-btn"
                onClick={() => handleBusSelection(bus.id)}
              >
                Select Seat
              </button>
            </div>
  
            {selectedBus === bus.id && (
              <div className="seat-selection">
                <img src={busFrame} alt="bus frame" className="bus-frame" />
                <div className="bus-layout">
                  {bus.seats.map((seat, index) => (
                    <div
                    key={index}
                    className={`seat ${seat.status} ${
                      selectedSeats.includes(seat.id) ? 'selected' : ''
                    } ${
                      seat.status === 'filled'
                        ? seat.gender === 'male'
                          ? 'male'
                          : 'female'
                        : ''
                    }`}
                    onClick={() => {
                      if (seat.status === 'empty' || seat.status === 'selected') {
                        handleSeatClick(seat.id);
                      }
                    }}
                  >
                      {seat.status === 'corridor' ? '' : seat.id}
                      {genderSelection.seatId === seat.id && (
                        <div className="gender-selectionn">
                          <button className="gender-button" onClick={() => handleGenderSelection(seat.id, 'male')}>
                            <img src={male_Icon} alt="Male" style={{ width: '60px', height: '60px' }} />
                            <span className="gender-span">Male</span>
                          </button>
                          <button className="gender-button" onClick={() => handleGenderSelection(seat.id, 'female')}>
                            <img src={female_Icon} alt="Female" style={{ width: '60px', height: '60px' }} />
                            <span className="gender-span">Female</span>
                          </button>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
                <div className="seat-selection-header">
                  <div className="legend">
                    <div className="legend-item">
                      <div className="legend-seat male"></div> <span>Occupied Seat - Male</span>
                    </div>
                    <div className="legend-item">
                      <div className="legend-seat empty"></div> <span>Empty Seat</span>
                    </div>
                    <div className="legend-item">
                      <div className="legend-seat female"></div> <span>Occupied Seat - Female</span>
                    </div>
                    <div className="legend-item">
                      <div className="legend-seat selected"></div> <span>Chosen Seat</span>
                    </div>
                  </div>
                  <div className="divider"></div>
                  <div className="selected-seats-container">
                  <div className="selected-seats-list">
                    {selectedSeatsWithGender.length === 0 ? (
                      <p className="no-seat-message">Please select seat from above</p>
                    ) : (
                      [...selectedSeatsWithGender]
                        .sort((a, b) => a.seatId - b.seatId) // Sort seats in ascending order
                        .map((seat, index) => (
                          <div key={index} className={`selected-seat ${seat.gender}`}>
                            <span>{seat.seatId} - {seat.gender === 'male' ? 'Male' : 'Female'}</span>
                          </div>
                        ))
                    )}
                  </div>
                  <div className="total-cost-container-inline">
                    <p className="total-cost">Total Cost: {calculateTotalCost()} TL</p>
                    <button
                      className="purchase-btn"
                      onClick={handleProceedToPayment}
                    >
                      Purchase
                    </button>
                  </div>
                </div>
              </div>
                <div className="amenities">
                    {bus.amenities.wifi && (
                      <img src={wifiIcon} alt="Wi-Fi" className="amenity-icon" />
                    )}
                    {bus.amenities.electricity && (
                      <img src={electricityIcon} alt="Electricity" className="amenity-icon" />
                    )}
                    {bus.amenities.tv && (
                      <img src={tvIcon} alt="TV" className="amenity-icon" />
                    )}
                    {bus.amenities.catering && (
                      <img src={cateringIcon} alt="Catering" className="amenity-icon" />
                    )}
                </div>
              </div>
            )}
          </div>
        ))
      )}
    </div>
  );  
}

export default Expedition;
