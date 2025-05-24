// Main.js
import React, { useEffect, useState } from 'react';
import './Main.css';
import Card from "../Card/Card.jsx";
import { fetchStudentCount } from '../../api/studentApi';
import { fetchCourseCount} from "../../api/coursApi.js";
import {fetchTeachersCount} from "../../api/teacherApi.js";
import LogoutButton from "../LogoutButton.jsx";

const Main = () => {
    const [studentCount, setStudentCount] = useState(null);
    const [courseCount, setCourseCount] = useState(null);
    const [teacherCount, setTeacherCount] = useState(null);

    useEffect(() => {
        const getStudentCount = async () => {
            try {
                const count = await fetchStudentCount();
                setStudentCount(count);
            } catch (error) {
                console.error("Error fetching student count:", error);
            }
        };

        const getCourseCount = async () => {
            try {
                const count = await fetchCourseCount();
                setCourseCount(count);
            } catch (error) {
                console.error("Error fetching student count:", error);
            }
        };
        const getTeacherCount = async () => {
            try {
                const count = await fetchTeachersCount();
                setTeacherCount(count);
            } catch (error) {
                console.error("Error fetching student count:", error);
            }
        };

        getStudentCount();
        getCourseCount();
        getTeacherCount();
    }, []); // Empty dependency array to run only once on mount

    return (
        <main>
            <h1>Gestion Des Ecoles 2024/2025</h1>
            <div className="hori">
                <h6 className="title">Page d'accueil</h6>
                <div className="config">
                    <LogoutButton/>
                </div>
            </div>
            <div className="welcome-container">
                <div className="welcome-header">Bienvenue sur notre système de gestion scolaire !</div>
            </div>

            <div className="cards">
                {/* Pass the studentCount to the Card component */}
                <Card
                    count={studentCount}
                    label="Etudiants"
                    description="Nombre d'étudiants"
                />
                <Card
                    count={courseCount}
                    label="Cours"
                    description="Nombre des cours"
                />
                <Card
                    count={teacherCount}
                    label="enseignant"
                    description="Nombre des profs"
                />

            </div>


        </main>
    );
};

export default Main;
