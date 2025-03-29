import React, { useState, useEffect } from 'react';
import './Account.css'; 
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css'; 
import axios from 'axios';
import apiConfig from './apiConfig';

function Account({ id }) {
  const [formData, setFormData] = useState({});
  const [isEditing, setIsEditing] = useState(false);
  const [phoneError, setPhoneError] = useState('');
  const [identityError, setIdentityError] = useState('');
  const [loading, setLoading] = useState(true);
  const [initialFormData, setInitialFormData] = useState({});

  // Fetch user information when the component loads or the ID changes
  useEffect(() => {
    if (id) {
      axios.post(`${apiConfig.baseUrl}/api/CustomerApi/GetCustomerDetails?id=${id}`)
        .then((response) => {
          setFormData(response.data);
          setInitialFormData(response.data); // Save the initial data for later comparison
          setLoading(false);
        })
        .catch((error) => {
          console.error('Failed to retrieve user information:', error);
          alert('Failed to retrieve user information');
          setLoading(false);
        });
    }
  }, [id]);

  // Function to validate phone number
  const validatePhone = (value) => {
    const phoneRegex = /^[0-9]{10,14}$/;
    if (!value || !phoneRegex.test(value)) {
      setPhoneError('Please enter a valid phone number.'); 
    } else {
      setPhoneError('');
    }
  };

  // Handle input changes and validate identity number
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    if (name === 'tcNo') {
      const identityRegex = /^[1-9][0-9]{10}$/;
      if (!identityRegex.test(value)) {
        setIdentityError('Identity must be a valid number (11 digits).');
      } else {
        setIdentityError('');
      }
    }
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();

    if (phoneError || identityError) {
      alert('Please fix the errors before submitting.');
      return;
    }

    // Check for changes
    const hasChanges = Object.keys(formData).some(
      (key) => formData[key] !== initialFormData[key]
    );

    if (hasChanges) {
      // If changes are made, prepare patch data
      const patchData = Object.keys(formData).reduce((acc, key) => {
        if (formData[key] !== null && formData[key] !== undefined) {
          acc.push({
            op: "replace",
            path: `/${key}`,
            value: formData[key],
          });
        }
        return acc;
      }, []);

      axios
        .patch(`${apiConfig.baseUrl}/api/CustomerApi/EditCustomer?id=${id}`, patchData, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        .then(() => {
          alert('Your changes have been saved successfully.'); 
          setInitialFormData({ ...formData }); 
          setIsEditing(false);
        })
        .catch((error) => {
          console.error('An error occurred while updating:', error);
          alert('An error occurred while updating your information.');
        });
    } else {
      alert('No changes detected, continuing without updates.'); 
    }
  };

  // Handle account deletion
  const handleDeleteAccount = () => {
    if (window.confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      axios.delete(`${apiConfig.baseUrl}/api/CustomerApi/DeleteCustomer?id=${id}`)
        .then(() => {
          alert('Your account has been deleted successfully.');
          window.location.href = '/'; 
        })
        .catch((error) => {
          console.error('An error occurred while deleting the account:', error);
          alert('An error occurred while deleting the account.');
        });
    }
  };

  return (
    <div className="account-container-ac">
      <h2 className="account-header">My Informations</h2>
      <form onSubmit={handleSubmit} className="account-form-ac">
        {/* Name input field */}
        <label className="account-label">
          Name: 
          <input
            type="text"
            name="name"
            value={formData.name || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          />
        </label>
        {/* Surname input field */}
        <label className="account-label">
          Surname:
          <input
            type="text"
            name="surname"
            value={formData.surname || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          />
        </label>
        {/* Identity input field */}
        <label className="account-label">
          Identity:
          <input
            type="text"
            name="identity"
            value={formData.identity || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          />
          {identityError && <div className="error-message">{identityError}</div>}
        </label>
        {/* Birthday date input field */}
        <label className="account-label">
          Birthday Date:
          <input
            type="date"
            name="birthday"
            value={formData.birthday || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          />
        </label>
        {/* Gender selection field */}
        <label className="account-label">
          Gender:
          <select
            name="gender"
            value={formData.gender || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          >
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>
        </label>
        {/* Phone number input field */}
        <label className="account-label">
          Phone Number:
          <PhoneInput
            country={'tr'}
            value={formData.phoneNumber || ''}
            onChange={(value) => {
              setFormData({ ...formData, phone: value });
              validatePhone(value);
            }}
            inputProps={{
              name: 'phone',
              disabled: !isEditing,
            }}
          />
          {phoneError && <div className="error-message">{phoneError}</div>}
        </label>
        {/* Email input field */}
        <label className="account-label">
          E-Mail: 
          <input
            type="email"
            name="email"
            value={formData.email || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          />
        </label>
        {/* Password input field */}
        <label className="account-label">
          Password: 
          <input
            type="text"
            name="password"
            value={formData.password || ''}
            onChange={handleInputChange}
            disabled={!isEditing}
          />
        </label>

        {/* Conditional rendering of buttons */}
        {isEditing ? (
          <button type="submit" className="account-button">Update My Information</button>
        ) : (
          <button
            type="button"
            className="account-button"
            onClick={() => {
              setIsEditing(true);
            }}
          >
            Edit
          </button>
        )}
        <button
          type="button"
          className="account-button delete-button"
          onClick={handleDeleteAccount}
        >
          Delete My Account
        </button>
      </form>
    </div>
  );
}

export default Account;
