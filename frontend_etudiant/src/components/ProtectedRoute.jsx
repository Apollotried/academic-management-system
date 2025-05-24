    import React, { useContext } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import {AuthContext} from "./contexe/AuthContext.jsx";


const ProtectedRoute = () => {
    const { isAuthenticated, userProfile } = useContext(AuthContext);
    const location = useLocation();

    if (!isAuthenticated || !userProfile.role.includes("USER")) {
        return <Navigate to="/" replace state={{ from: location }} />;
    }

    return <Outlet />;
};

export default ProtectedRoute;
