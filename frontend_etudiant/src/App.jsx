// App.jsx

import { useState } from 'react';
import './global.js'
import './App.css';
import './index.css';
import Landing from './Landing.jsx';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import StudentDetails from './StudentDetails.jsx';  // L'importation par défaut
import StudentResults from "./StudentResults.jsx";
import {AuthProvider} from "./components/contexe/AuthContext.jsx";
import LoginPage from "./components/Login/LoginPage.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import CourseListeManagement from "./CourseListeManagement.jsx";
import ViewSyllabus from "./components/coursList/viewSyllabus.jsx";
import WebSocketComponent from "./components/NotificationComponent.jsx";
import Grades from "./components/resultats/Grades.jsx";


function App() {
    return (

        <AuthProvider>
                <Router>
                    <div>
                        <ToastContainer />
                        <Routes>
                            <Route path="/" element={<LoginPage />} />
                            <Route path="/user" element={<ProtectedRoute />} >
                                <Route path="landing" element={<Landing />} />
                                <Route path="StudentDetails" element={<StudentDetails />} />
                                <Route path="StudentResults" element={<StudentResults />} />
                                <Route path="CourseListe" element={<CourseListeManagement />} />
                                <Route path="view-syllabus" element={<ViewSyllabus />} />
                                <Route path="grades" element={<Grades />} />

                            </Route>

                        </Routes>
                    </div>
                </Router>
        </AuthProvider>
    );
}

export default App;
