import React, { useState } from 'react';
import axios from 'axios';
import './Login.css';

function Login({ setIsLoggedIn }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/api/auth/public/login', { username, password });

      if (response.status === 200) {
        localStorage.setItem('jwtToken', response.data);
        setIsLoggedIn(true);
        window.location.href = "/dashboard"
      } else {
        setError('Invalid username or password');
      }
    } catch (error) {
      setError('An error occurred while logging in' + error);
    }
  };

  return (
    <div className="login-container">
      <h1>Inventory Management System</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
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
        <button type="submit">Login</button>
      </form>
    </div>
  );
}

export default Login;