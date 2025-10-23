import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Configure axios defaults
  useEffect(() => {
    axios.defaults.withCredentials = true;
    axios.defaults.baseURL = 'http://localhost:8080';
  }, []);

  const checkAuthStatus = async () => {
    try {
      setLoading(true);
      const response = await axios.get('/api/user');
      if (response.data.authenticated) {
        setUser(response.data.user);
      } else {
        setUser(null);
      }
    } catch (err) {
      console.error('Auth check failed:', err);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const login = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
  };

  const logout = async () => {
    try {
      await axios.post('/api/logout');
      setUser(null);
      window.location.href = '/';
    } catch (err) {
      console.error('Logout failed:', err);
      setError('Logout failed. Please try again.');
    }
  };

  const updateProfile = async (profileData) => {
    try {
      const response = await axios.post('/api/profile', profileData);
      if (response.data.success) {
        setUser(response.data.user);
        return { success: true };
      } else {
        throw new Error(response.data.error || 'Profile update failed');
      }
    } catch (err) {
      console.error('Profile update failed:', err);
      setError(err.response?.data?.error || 'Profile update failed');
      return { success: false, error: err.response?.data?.error || 'Profile update failed' };
    }
  };

  const clearError = () => setError(null);

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const value = {
    user,
    loading,
    error,
    login,
    logout,
    updateProfile,
    checkAuthStatus,
    clearError
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
