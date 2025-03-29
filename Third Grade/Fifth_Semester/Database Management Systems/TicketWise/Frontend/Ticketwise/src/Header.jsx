import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './App.css';
import Logo from './components/Logo-Blue.jpg';
import personIcon from './components/Person.png';
import './Header.css';

function Header({ isLoggedIn, isSignedUp, setIsLoggedIn, setIsSignedUp, userName }) {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuRef = useRef(null);
  const navigate = useNavigate();

  // Toggles the dropdown menu visibility
  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  // Closes the dropdown menu
  const closeMenu = () => {
    setIsMenuOpen(false);
  };

  // Handles the logout process and redirects to the homepage
  const handleLogout = () => {
    setIsLoggedIn(false);
    setIsSignedUp(false);
    setTimeout(() => {
      navigate('/'); // Redirect to the homepage after 1 second
      closeMenu(); // Close the dropdown menu
    }, 1000);
  };

  // Navigates to the About Us page
  const handleAboutUsClick = () => {
    navigate('/about-us');
  };

  // Adds a listener to detect clicks outside the dropdown menu
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        closeMenu();
      }
    };

    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside); // Cleanup the event listener on component unmount
    };
  }, []);

  return (
    <>
      <header className="header">
        {/* Logo section */}
        <div className="logo-container">
          <Link to="/">
            <img src={Logo} alt="Logo" className="logo-image" />
          </Link>
          <div className="logo-text">
            <span style={{ color: '#000000' }}>TICKET</span>
            <span style={{ color: '#FFD700' }}>WISE</span>
          </div>
        </div>

        {/* Navigation links */}
        <nav className="nav-links">
          <Link to="/about-us" onClick={handleAboutUsClick}>About Us</Link>
          <span>|</span>
          {!isLoggedIn && !isSignedUp ? (
            // If the user is not logged in or signed up, show Sign Up and Log In links
            <>
              <Link to="/signup">Sign Up</Link>
              <span>|</span>
              <Link to="/login">Log In</Link>
            </>
          ) : (
            // If the user is logged in, show the user profile dropdown menu
            <div className="user-profile" ref={menuRef}>
              <img src={personIcon} alt="User Icon" className="user-icon" />
              <span className="user-name">{userName}</span>
              <button className="dropdown-toggle" onClick={toggleMenu}>
                ▼
              </button>
              {isMenuOpen && (
                <div className="dropdown-menu">
                  <Link to="/mytravel" onClick={closeMenu}>My Travels</Link>
                  <Link to="/account" onClick={closeMenu}>My User Information</Link>
                  <Link
                    to="#"
                    onClick={(e) => {
                      e.preventDefault();
                      handleLogout();
                    }}
                    className="logout-link"
                  >
                    Log Out
                  </Link>
                </div>
              )}
            </div>
          )}
        </nav>
      </header>
    </>
  );
}

export default Header;
