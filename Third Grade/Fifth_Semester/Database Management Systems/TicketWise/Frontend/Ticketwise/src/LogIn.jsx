import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './App.css';
import seeIcon from './components/See.png'; // Show icon
import dontSeeIcon from './components/Dontsee.png'; // Hide icon
import apiConfig from './apiConfig';

function Login({onLogin}) {
  const [email, setEmail] = useState(''); // State to store email input
  const [password, setPassword] = useState(''); // State to store password input
  const [showPassword, setShowPassword] = useState(false); // Toggle visibility of password
  const [message, setMessage] = useState(''); // Message to display success or error
  const [isAlertVisible, setIsAlertVisible] = useState(false); // State to control visibility of alert box
  const navigate = useNavigate(); // Hook for navigation

  const handleLogInFormSubmit = async (e) => {
    e.preventDefault();

    // Check if email and password fields are filled
    if (!email || !password) {
      setMessage('Please fill in all fields.');
      return;
    }

    const handleLoginSuccess = () => {
      onLogin();
    };

    // Prepare data to send for login
    const dataToSendLogIn = {
      email,
      password,
    };

    try {
      // Send login request to the server
      const response = await axios.post(`${apiConfig.baseUrl}/api/CustomerApi/Login`, dataToSendLogIn);

      if (response.data.success) {
        const userName = `${response.data.customer.name} ${response.data.customer.surname}`.trim() || 'User';
        const userId = response.data.customer.id;
        setMessage('Login successful!');
        onLogin(userName, userId);
        setTimeout(() => {
          navigate('/');
        }, 1000);
      } else {
        // Handle unsuccessful login
        setMessage('Please check your e-mail address and password and try again.');
        setIsAlertVisible(true);
      }
    } catch (error) {
      // Handle server or network errors
      console.error('Axios Error:', error);
      setMessage(error.response?.data?.message || 'An error occurred. Please try again later.');
      setIsAlertVisible(true);
    }
  };

  const closeAlert = () => {
    setIsAlertVisible(false); 
  };

  return (
    <div className="signup-container" lang="en">
      <h2>Welcome !</h2>
      <p>Please fill the required information.</p>
      <form onSubmit={handleLogInFormSubmit}>
        {/* Email Input */}
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          onInvalid={(e) => e.target.setCustomValidity('Please enter your email.')} 
          onInput={(e) => e.target.setCustomValidity('')} 
        />

        {/* Password Input */}
        <div className="password-container">
          <input
            type={showPassword ? 'text' : 'password'}
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            onInvalid={(e) => e.target.setCustomValidity('Please enter your password.')} 
            onInput={(e) => e.target.setCustomValidity('')} 
          />
          <img
            src={showPassword ? dontSeeIcon : seeIcon}
            alt="toggle password visibility"
            className="password-toggle-icon"
            onClick={() => setShowPassword(!showPassword)}
          />
        </div>
        {/* Display success or error message */}
        {message && <p className="message">{message}</p>}

        <button type="submit">Log In</button>
      </form>
      <p></p>
      {/* Link to sign-up page */}
      <p>Not a member yet? <a href="/signup">Sign Up</a></p>
    {/* Alert Box for Errors */}
    {isAlertVisible && (
        <div className="alert-overlay">
          <div className="alert-box">
            <div className="alert-header">
              <span className="alert-icon">❕</span>
              <h2 className="alert-title">Error</h2>
              <span className="alert-icon">❕</span>
            </div>
            <p className="alert-message">{message}</p>
            <button className="alert-buttonn" onClick={closeAlert}>
              Okey
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default Login;
