import React, { useState, useEffect } from 'react'; 
import { useNavigate } from 'react-router-dom';
import apiConfig from './apiConfig';
import axios from 'axios';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css'; 
import './SignUp.css';
import seeIcon from './components/See.png'; 
import dontSeeIcon from './components/Dontsee.png'; 

function SignUp({onSignUp}) {
  const [phone, setPhone] = useState('');
  const [phoneError, setPhoneError] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [gender, setGender] = useState('');
  const [kvkk, setKvkk] = useState(false);
  const [passwordError, setPasswordError] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    email: '',
    identity: '',
    birthday: '',
  });

  const [identityError, setIdentityError] = useState(''); 
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  // Function to handle input changes and update state
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    
  };

  // Function to validate the password
  const handlePasswordChange = (e) => {
    const value = e.target.value;
    setPassword(value);

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/;
    if (!passwordRegex.test(value)) {
      setPasswordError('Password must contain at least one lowercase letter, one uppercase letter, and one digit.');
    } else {
      setPasswordError('');
    }
  };

  // Function to validate phone number
  const validatePhone = (value) => {
    const phoneRegex = /^[0-9]{10,14}$/; // Phone number should have 10-14 digits
    if (!value || !phoneRegex.test(value)) {
      setPhoneError('Please enter a valid phone number.');
    } else {
      setPhoneError('');
    }
  };
  

  const handleFormSubmit = (e) => {
    e.preventDefault();
    if (validatePhone()) {
      alert('Form submitted successfully!');
    }
  };

  // Function to validate identity number
  const handleIdentityChange = (e) => {
    const value = e.target.value;
    const identityRegex = /^[1-9][0-9]{10}$/; // Turkish ID format
    setFormData({ ...formData, identity: value });
  
    if (!identityRegex.test(value)) {
      setIdentityError('Identity must be a valid number (11 digits).');
    } else {
      setIdentityError('');
    }
  };

  
  // Function to handle form submission
  const handleInputFormSubmit = async (e) => {
    e.preventDefault(); 

    if (passwordError) {
        setErrorMessage('Please check your password on the form.');
        return;
    }

    // Prepare data to send to the API
    const dataToSend = {
        name: formData.name,
        surname: formData.surname,
        email: formData.email,
        phoneNumber: phone, 
        gender,
        identity: formData.identity,
        birthday: formData.birthday, 
        password,
    };

    try {
        // Make an API call to register the user
        const response = await axios.post(`${apiConfig.baseUrl}/api/CustomerApi/AddCustomer`, dataToSend);
        console.log('Response:', response.data);
        setSuccessMessage('Registration successful!'); 
        
        if (onSignUp) {
          const userName = `${formData.name} ${formData.surname}`.trim() || 'User';
          const userId = response.data.id; 
          onSignUp(userName, userId);
        } // Redirect to the homepage after successful registration
        setTimeout(() => {
          navigate('/'); 
        }, 1000); 
        setErrorMessage(''); 
    } catch (error) {
        console.error('Axios Error:', error);
        setErrorMessage(error.response?.data?.message || 'An error occurred. Please try again later.');
        setSuccessMessage(''); 
    }
  };


  return (
    <div className="signup-container" lang="en">
      <h2>Create Your Account</h2>
      <p>Please fill the required information.</p>
      <form onSubmit={handleInputFormSubmit}>
        {/* Name */}
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={formData.name}
          onChange={handleInputChange}
          required
          onInvalid={(e) => e.target.setCustomValidity('Please enter your name.')} // Özelleştirilmiş mesaj
          onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını temizle
        />
        {/* Surname */}
        <input
          type="text"
          name="surname"
          placeholder="Surname"
          value={formData.surname}
          onChange={handleInputChange}
          required
          onInvalid={(e) => e.target.setCustomValidity('Please enter your surname.')} // Özelleştirilmiş mesaj
          onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını temizle
        />
        {/* Email */}
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleInputChange}
          required
          onInvalid={(e) => e.target.setCustomValidity('Please enter your email.')} // Özelleştirilmiş mesaj
          onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını temizle
        />
        {/* Identity */}
        <input
          type="text"
          name="identity"
          placeholder="Identity"
          value={formData.identity}
          onChange={handleIdentityChange}
          required
          onInvalid={(e) => e.target.setCustomValidity('Please enter your identity.')} // Özelleştirilmiş mesaj
          onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını temizle
        />
        {identityError && <div className="identity-error">{identityError}</div>}

        {/* Password */}
        <div className="password-container">
          <input
            type={showPassword ? 'text' : 'password'}
            placeholder="Password"
            value={password}
            onChange={handlePasswordChange}
            required
            onInvalid={(e) => e.target.setCustomValidity('Please enter a valid password.')} // Özelleştirilmiş mesaj
            onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını temizle
          />
          <img
            src={showPassword ? dontSeeIcon : seeIcon}
            alt="Toggle password visibility"
            className="password-toggle-icon"
            onClick={() => setShowPassword(!showPassword)}
          />
          {passwordError && <div className="password-error">{passwordError}</div>}
        </div>

        {/* Birthday */}
        <input
          type="date"
          name="birthday"
          placeholder="Birthday"
          value={formData.birthday}
          onChange={handleInputChange}
          required
          onInvalid={(e) => e.target.setCustomValidity('Please enter your birthday.')} // Özelleştirilmiş mesaj
          onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını tem
        />

        {/* Phone Input */}
        <PhoneInput
            country={'tr'}
            value={phone}
            onChange={(value) => {
              setPhone(value);
              validatePhone(value); // Anlık doğrulama
            }}
            inputProps={{
              name: 'phone',
              required: true,
            }}
          />
          {phoneError && <div className="phone-error">{phoneError}</div>}
          
        {/* Gender Selection */}
        <div className="gender-container">
            <label>
              <input
                type="radio"
                name="gender"
                value="Male"
                checked={gender === 'Male'}
                onChange={() => setGender('Male')}
                required
              /> Male
            </label>
            <label>
              <input
                type="radio"
                name="gender"
                value="Female"
                checked={gender === 'Female'}
                onChange={() => setGender('Female')}
                required
              /> Female
            </label>
          </div>

        {/* KVKK Agreement */}
        <div className="kvkk-agreement">
          <label>
            <input
              type="checkbox"
              checked={kvkk}
              onChange={() => setKvkk(!kvkk)}
              required
              onInvalid={(e) => e.target.setCustomValidity('Please tick this box if you wish to proceed.')} // Özelleştirilmiş mesaj
              onInput={(e) => e.target.setCustomValidity('')} // Kullanıcı tekrar yazarken hata mesajını tem
            />
            <a href="/kvkk" target="_blank" rel="noopener noreferrer">
              KVKK Agreement
            </a>
          </label>
        </div>

        {/* Submit Button */}
        <button type="submit">Sign Up</button>
      </form>

      {/* Success and Error Messages */}
      {successMessage && <div className="success-message">{successMessage}</div>}
      {errorMessage && <div className="error-message">{errorMessage}</div>}
      <p></p>
      <p>Already a member? <a href="/login">Log In</a></p>
    </div>
  );

}

export default SignUp;
