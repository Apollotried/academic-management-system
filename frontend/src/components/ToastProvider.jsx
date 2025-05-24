// ToastProvider.jsx
import React from 'react';
import { ToastContainer } from 'react-toastify';
import {Outlet} from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute.jsx";

const ToastProvider = () => {
    return (
        <>
            <ToastContainer />
            <ProtectedRoute/>
        </>
    );
};

export default ToastProvider;
