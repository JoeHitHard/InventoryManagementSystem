import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Signup from './components/Signup';
import Dashboard from './components/Dashboard';
// import ProductManagement from './components/ProductManagement';
// import SalesPage from './components/SalesPage';
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
        <Route path="/" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
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
          path="/products"
          element={
            <PrivateRoute isLoggedIn={isLoggedIn}>
              {/* <ProductManagement /> */}
            </PrivateRoute>
          }
        />
        <Route
          path="/sales"
          element={
            <PrivateRoute isLoggedIn={isLoggedIn}>
              {/* <SalesPage /> */}
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;