import React, { useEffect, useState } from 'react';
import './Main.css';
import { fetchStudentById } from '../../api/studentApi';

import Cookies from 'js-cookie';
import LogoutButton from "../LogoutButton.jsx";
import NotificationComponent from "../NotificationComponent.jsx";  // Import js-cookie to work with cookies

const Main = () => {
    const [student, setStudent] = useState(null);

    useEffect(() => {
        // Check for the token in cookies and set it in localStorage if it exists
        const tokenFromCookie = Cookies.get('token');
        if (tokenFromCookie) {
            localStorage.setItem('token', tokenFromCookie); // Store it in localStorage
        }

        // Fetch the student by ID (you can change '1' to dynamic user ID if needed)
        const getStudentById = async () => {
            try {
                const student = await fetchStudentById();
                console.log(student);
                setStudent(student);
            } catch (error) {
                console.error("Error fetching student:", error);
            }
        };

        getStudentById(); // Empty dependency array to run only once on mount

    }, []); // Empty dependency array to run only once on mount

    return (
        <main>
            <h1>Gestion Des Ecoles 2024/2025</h1>
            <div className="hori">
                <h6 className="title">Page d'accueil</h6>
                <div className="config">
                        <LogoutButton />
                       <NotificationComponent />

                </div>
            </div>

            <div className="welcome-container">
                <div className="welcome-header">Bienvenue {student ? student.firstName : "etudiant"} !</div>
                <div className="welcome-message">
                    Nous sommes ravis de vous accueillir. Notre système est conçu pour vous aider à gérer efficacement vos informations,
                    explorer vos cours, consulter vos résultats et bien plus encore.
                    Naviguez facilement en utilisant les liens ci-dessous :
                </div>

            </div>
        </main>
    );
};

export default Main;
