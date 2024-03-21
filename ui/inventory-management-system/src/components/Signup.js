import React, { useState } from 'react';
import './Login.css';

function Signup({ setIsLoggedIn }) {
  const [userId, setUserId] = useState('');
  const [name, setName] = useState('');
  const [emailId, setEmailId] = useState('');
  const [role] = useState('admin');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/api/auth/public/signUp', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId, name, emailId, role, password }),
      });

      if (response.ok) {
        await response.json();
        window.location.href = "/"
      } else {
        setError("Failed to create user");
      }
    } catch (error) {
      setError('An error occurred while signup in' + error);
    }
  };

  return (
    <div className="sign-up-container">
      <h1>Inventory Management System</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="userId">UserId:</label>
          <input
            type="text"
            id="userId"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label htmlFor="name">Name:</label>
          <input
            type="text"
            id="password"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label htmlFor="emailId">EmailId:</label>
          <input
            type="email"
            id="emailId"
            value={emailId}
            onChange={(e) => setEmailId(e.target.value)}
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        {error && <div className="error">{error}</div>}
        <button type="submit">Signup</button>
      </form>
    </div>
  );
}

export default Signup;