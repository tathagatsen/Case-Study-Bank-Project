// src/components/ProtectedRoute.jsx
import React from 'react';
import { Navigate } from 'react-router';
import { isUserAuthenticated } from '../../utils/auth';

const ProtectedRoute = ({ children }) => {
  if (!isUserAuthenticated()) {
    return <Navigate to="/login" replace />;
  }
  return children;
};
export default ProtectedRoute;