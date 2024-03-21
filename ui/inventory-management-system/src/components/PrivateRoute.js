import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';

function PrivateRoute({ children }) {
  const jwtToken = localStorage.getItem('jwtToken');
  const location = useLocation();

  if (!jwtToken) {
    // Redirect to the login page if the user is not authenticated
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Render the protected component if the user is authenticated
  return children;
}

export default PrivateRoute;