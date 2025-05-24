import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import {AuthContext} from "./contexe/AuthContext.jsx";


const LogoutButton = () => {
    const { setIsAuthenticated, setUserProfile } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        // Clear token from local storage
        localStorage.removeItem('token');

        // Reset authentication state
        setIsAuthenticated(false);
        setUserProfile({ username: '', role: '' });

        // Redirect to login page
        navigate('/');
    };

    return (
        <a href="#" onClick={handleLogout}>
            <i className="fas fa-sign-out-alt"></i> se déconnecter
        </a>
    );
};

export default LogoutButton;
