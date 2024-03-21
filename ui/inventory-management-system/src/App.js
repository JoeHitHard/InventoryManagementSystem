import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard';
import PrivateRoute from './components/PrivateRoute';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const jwtToken = localStorage.getItem('jwtToken');
    if (jwtToken) {
      setIsLoggedIn(true);
    }
  }, []);

  return (
    <Router>
      <Routes>
        {isLoggedIn === false && <Route path="/" element={<Login setIsLoggedIn={setIsLoggedIn} />} />}
        {isLoggedIn === true &&  <Route
          path="/"
          element={
            <PrivateRoute isLoggedIn={isLoggedIn}>
              <Dashboard />
            </PrivateRoute>
          }
        />}
        <Route
          path="/dashboard"
          element={
            <PrivateRoute isLoggedIn={isLoggedIn}>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/signup"
          element={
            <Signup />
          }
        />
        <Route
          path="/login"
          element={
            <Login setIsLoggedIn={setIsLoggedIn} />
          }
        />
      </Routes>
    </Router>
  );
}

export default App;