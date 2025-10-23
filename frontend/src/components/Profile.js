import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext'; // Corrected import path
import { useNavigate } from 'react-router-dom';

const Profile = () => {
  const { user, logout, updateProfile, error, clearError } = useAuth();
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    displayName: '',
    bio: ''
  });
  const [loading, setLoading] = useState(false);

  // --- NEW HELPER FUNCTION ---
  // This function safely formats dates, handling null or invalid values
  const formatDate = (dateString, options = {}) => {
    // Check if the date string is null, undefined, or empty
    if (!dateString) {
      return 'N/A';
    }

    const date = new Date(dateString);

    // Check if the date object is valid
    if (isNaN(date.getTime())) {
      return 'N/A'; // Was 'Invalid Date', now returns 'N/A'
    }

    // Use toLocaleDateString() if dateOnly is requested, otherwise toLocaleString()
    if (options.dateOnly) {
      return date.toLocaleDateString();
    }
    return date.toLocaleString();
  };
  // --- END OF HELPER FUNCTION ---

  useEffect(() => {
    if (user) {
      setFormData({
        displayName: user.displayName || '',
        bio: user.bio || ''
      });
    }
  }, [user]);

  useEffect(() => {
    if (error) {
      const timer = setTimeout(() => {
        clearError();
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [error, clearError]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    const result = await updateProfile(formData);
    if (result.success) {
      setIsEditing(false);
    }
    
    setLoading(false);
  };

  const handleCancel = () => {
    setFormData({
      displayName: user.displayName || '',
      bio: user.bio || ''
    });
    setIsEditing(false);
    clearError();
  };

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  if (!user) {
    return (
      <div className="container">
        <div className="loading">
          <div className="spinner"></div>
          <p>Loading profile...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="profile">
        <header className="profile-header">
          <div className="profile-info">
            <div className="avatar">
              {user.avatarUrl ? (
                <img src={user.avatarUrl} alt="Profile Avatar" />
              ) : (
                <div className="avatar-placeholder">
                  {user.displayName ? user.displayName.charAt(0).toUpperCase() : 'U'}
                </div>
              )}
            </div>
            <div className="user-details">
              <h1>{user.displayName || 'User'}</h1>
              <p className="email">{user.email}</p>
              <p className="member-since">
                {/* --- UPDATED --- */}
                Member since {formatDate(user.createdAt, { dateOnly: true })}
              </p>
            </div>
          </div>
          <div className="profile-actions">
            <button 
              className="btn btn-secondary"
              onClick={() => navigate('/')}
            >
              ← Home
            </button>
            <button 
              className="btn btn-danger"
              onClick={handleLogout}
            >
              Logout
            </button>
          </div>
        </header>

        {error && (
          <div className="error-message">
            <p>{error}</p>
            <button onClick={clearError}>×</button>
          </div>
        )}

        <div className="profile-content">
          <div className="profile-section">
            <div className="section-header">
              <h2>Profile Information</h2>
              {!isEditing && (
                <button 
                  className="btn btn-primary"
                  onClick={() => setIsEditing(true)}
                >
                  Edit Profile
                </button>
              )}
            </div>

            {isEditing ? (
              <form onSubmit={handleSubmit} className="profile-form">
                <div className="form-group">
                  <label htmlFor="displayName">Display Name</label>
                  <input
                    type="text"
                    id="displayName"
                    name="displayName"
                    value={formData.displayName}
                    onChange={handleInputChange}
                    required
                    placeholder="Enter your display name"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="bio">Bio</label>
                  <textarea
                    id="bio"
                    name="bio"
                    value={formData.bio}
                    onChange={handleInputChange}
                    rows="4"
                    placeholder="Tell us about yourself..."
                  />
                </div>

                <div className="form-actions">
                  <button 
                    type="button"
                    className="btn btn-secondary"
                    onClick={handleCancel}
                    disabled={loading}
                  >
                    Cancel
                  </button>
                  <button 
                    type="submit"
                    className="btn btn-primary"
                    disabled={loading}
                  >
                    {loading ? 'Saving...' : 'Save Changes'}
                  </button>
                </div>
              </form>
            ) : (
              <div className="profile-details">
                <div className="detail-item">
                  <label>Display Name</label>
                  <p>{user.displayName || 'Not set'}</p>
                </div>
                <div className="detail-item">
                  <label>Email</label>
                  <p>{user.email}</p>
                </div>
                <div className="detail-item">
                  <label>Bio</label>
                  <p>{user.bio || 'No bio provided'}</p>
                </div>
                <div className="detail-item">
                  <label>Last Updated</label>
                  {/* --- UPDATED --- */}
                  <p>{formatDate(user.updatedAt)}</p>
                </div>
              </div>
            )}
          </div>

          <div className="profile-section">
            <h2>Account Information</h2>
            <div className="account-details">
              <div className="detail-item">
                <label>User ID</label>
                <p>{user.id}</p>
              </div>
              <div className="detail-item">
                <label>Account Created</label>
                {/* --- UPDATED --- */}
                <p>{formatDate(user.createdAt)}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

