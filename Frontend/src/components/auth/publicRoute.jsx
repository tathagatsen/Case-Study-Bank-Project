// src/components/PublicRoute.jsx
import React from 'react';
import { Navigate } from 'react-router';
import { isUserAuthenticated } from '../../utils/auth';

const PublicRoute = ({ children }) => {
  if (isUserAuthenticated()) {
    return <Navigate to="/dashboard" replace />;
  }
  return children;
};
export default PublicRoute;