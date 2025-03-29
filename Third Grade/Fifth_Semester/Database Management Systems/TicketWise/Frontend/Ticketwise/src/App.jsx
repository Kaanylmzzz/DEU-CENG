import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router,  Route, Routes } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import './App.css';
import SignUp from './SignUp'; // SignUp component
import KvkkPage from './KvkkPage'; // KvkkPage component
import LogIn from './LogIn'; // LogIn component
import Expedition from './Expedition';  // Expedition component
import Payment from './Payment'; // Payment component
import Header from './Header'; // Header component
import Account from './Account'; // Account component
import PaymentSuccess from './PaymentSuccess'; // PaymentSuccess component
import AboutUs from './AboutUs'; // AboutUs component
import MyTravel from './MyTravel'; // MyTravel component
import premium_partners from './components/Premium-Partners.png';
import budget_friendly from './components/Budget-Friendly.png';
import secure_payment from './components/Secure-Payment.png';
import customer_support from './components/Customer-Support.png';

// Search Form Component
function SearchForm({ from, to, date, setFrom, setTo, setDate, switchPlaces }) {
  const [error, setError] = useState('');
  const navigate = useNavigate(); 

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (from === to) {
      setError('From and To locations cannot be the same. Please select different cities.');
    } else {
      setError('');
      navigate(`/expedition?from=${from}&to=${to}&date=${date}`);
    }
  };

  const cities = [
    // List of cities
    "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin", "Aydın", 
    "Balıkesir", "Bartın", "Batman", "Bayburt", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", 
    "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Düzce", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", 
    "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Iğdır", "Isparta", "İstanbul", "İzmir", "Kahramanmaraş", 
    "Karabük", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kırıkkale", "Kırklareli", "Kırşehir", "Kilis", "Kocaeli", "Konya", 
    "Kütahya", "Malatya", "Manisa", "Mardin", "Mersin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Osmaniye", "Rize", "Sakarya", 
    "Samsun", "Siirt", "Sinop", "Sivas", "Şanlıurfa", "Şırnak", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Uşak", "Van", "Yalova", "Yozgat", "Zonguldak"
  ];

  const handleCityChange = (field, value) => {
    let updatedFrom = field === 'from' ? value : from;
    let updatedTo = field === 'to' ? value : to;

    if (field === 'from') {
      setFrom(value);
    } else {
      setTo(value);
    }

    if (updatedFrom === updatedTo) {
      setError('From and To locations cannot be the same. Please select different cities.');
    } else {
      setError('');
    }
  };
  

  return (
    <form className="search-form" onSubmit={handleSubmit}>
      <select 
      value={from}
      onChange={(e) => handleCityChange('from', e.target.value)}
      required
      onInvalid={(e) => e.target.setCustomValidity('Please select a city from the list.')}
      onInput={(e) => e.target.setCustomValidity('')} 
      >
        <option value="" disabled>From</option>
        {cities.map((city, index) => (
          <option key={index} value={city}>{city}</option>
        ))}
      </select>

      <div className="switch-button" onClick={switchPlaces}>
        ⇄
      </div>

      <select value={to} 
      onChange={(e) => handleCityChange('to', e.target.value)} 
      required
      onInvalid={(e) => e.target.setCustomValidity('Please select a city from the list.')}
      onInput={(e) => e.target.setCustomValidity('')} 
      >
        <option value="" disabled>To</option>
        {cities.map((city, index) => (
          <option key={index} value={city}>{city}</option>
        ))}
      </select>

      <input
        type="date"
        value={date}
        onChange={(e) => setDate(e.target.value)}
        min={new Date().toISOString().split('T')[0]} 
        required
        onInvalid={(e) => e.target.setCustomValidity('Please select a date.')}
        onInput={(e) => e.target.setCustomValidity('')} 
      />

      <button type="submit">Search Bus</button>
      {error && <p className="error animated-error">{error}</p>}
    </form>
  );
}

// Date Options Component
function DateOptions({ setTodayDate, setTomorrowDate }) {
  return (
    <div className="date-options">
      <label>
        <input type="radio" name="date-option" onClick={setTodayDate} />
        Today
      </label>
      <label>
        <input type="radio" name="date-option" onClick={setTomorrowDate} />
        Tomorrow
      </label>
    </div>
  );
}

// Features Section Component
function FeaturesSection() {
  const features = [
    // List of features
    {
      img: customer_support,
      title: "7/24 Customer Support",
      description: "Our team is available 7/24 to assist you with any inquiries or issues you may encounter."
    },
    {
      img: secure_payment,
      title: "Secure Payment",
      description: "We offer fast and secure payment options to ensure your transactions are safe."
    },
    {
      img: budget_friendly,
      title: "Budget Friendly",
      description: "Compare and choose from the most affordable bus tickets that fit your budget."
    },
    {
      img: premium_partners,
      title: "Premium Partners",
      description: "Travel with trusted and top-rated bus companies for a comfortable journey."
    }
  ];

  return (
    <section className="features-container">
      {features.map((feature, index) => (
        <div key={index} className="feature-card">
          <img src={feature.img} alt={feature.title} />
          <h3>{feature.title}</h3>
          <p>{feature.description}</p>
        </div>
      ))}
    </section>
  );
}

// Animated Header Component
function AnimatedHeader() {
  const [textIndex, setTextIndex] = useState(0);
  const texts = [
    // List of rotating texts
    "The World's Most Reliable Travel App",
    "The World's Highest Quality Travel App",
    "The World's Most Comprehensive Travel App",
    "The World's Most Innovative Travel App",
    "The World's Fastest And Easiest Travel App"
  ];

  useEffect(() => {
    const animationDuration = 4000; 
    const timeout = setTimeout(() => {
      setTextIndex((prevIndex) => (prevIndex + 1) % texts.length);
    }, animationDuration);
    return () => clearTimeout(timeout); 
  }, [textIndex]);

  return (
    <div className="animated-header">
      <h1 className="rotating-text">{texts[textIndex]}</h1>
    </div>
  );
}

// Main App Component
function App() {
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");
  const [date, setDate] = useState("");
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Login status
  const [isSignedUp, setIsSignedUp] = useState(false); // Signup status
  const [userName, setUserName] = useState(""); // User name
  const [userId, setUserId] = useState(null); // User ID

  const handleLogin = (fullName, id) => {
    setIsLoggedIn(true);
    setUserName(fullName);
    setUserId(parseInt(id, 10)); // Convert id to int and store user ID
  };

  const handleSignUp = (fullName, id) => {
    setIsSignedUp(true);
    setUserName(fullName);
    setUserId(parseInt(id, 10)); // Convert id to int and store user ID
  };

  const switchPlaces = () => {
    setFrom((prevFrom) => to);
    setTo((prevTo) => from);
  };

  const setTodayDate = () => {
    const today = new Date().toISOString().split("T")[0];
    setDate(today);
  };

  const setTomorrowDate = () => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    setDate(tomorrow.toISOString().split("T")[0]);
  };

  return (
    <Router>
      <div className="App" lang="en">
        <Header isLoggedIn={isLoggedIn}  isSignedUp={isSignedUp} setIsLoggedIn={setIsLoggedIn} setIsSignedUp={setIsSignedUp} userName={userName} />
        <main className="main-content">
          <Routes>
            <Route
              path="/"
              element={
                <>
                  <AnimatedHeader />
                  <div className="search-container">
                    <SearchForm
                      from={from}
                      to={to}
                      date={date}
                      setFrom={setFrom}
                      setTo={setTo}
                      setDate={setDate}
                      switchPlaces={switchPlaces}
                    />
                    <DateOptions setTodayDate={setTodayDate} setTomorrowDate={setTomorrowDate} />
                  </div>
                </>
              }
            />

            <Route path="/signup" element={<SignUp onSignUp={(fullName, id) => handleSignUp(fullName, id)} />} />
            <Route path="/kvkk" element={<KvkkPage />} />
            <Route path="/login" element={<LogIn onLogin={(fullName, id) => handleLogin(fullName, id)} />} />
            <Route path="/expedition" element={<Expedition />} />
            <Route path="/payment" element={<Payment customerId={userId} />} />
            <Route path="/paymentsuccess" element={<PaymentSuccess />} />
            <Route path="/account" element={<Account id={userId} />} />
            <Route path="/about-us" element={<AboutUs />} />
            <Route path="/mytravel" element={<MyTravel id={userId}/>} />
          </Routes>
          <FeaturesSection />
        </main>
      </div>
    </Router>
  );
}

export default App;
