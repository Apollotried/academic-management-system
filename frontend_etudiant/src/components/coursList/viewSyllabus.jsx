
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import "./AddSyllabus.css";
import {useEffect, useState} from "react";
import Header from "../Header/header.jsx";
import {fetchSyllabusById} from "../../api/coursApi.js";
import {useLocation} from "react-router-dom";
const ViewSyllabus = () => {
    const location = useLocation();


    const { course } = location.state || {};
    const [syllabus, setSyllabus] = useState(null);

    useEffect(() => {
        if (course.syllabusId) {
            fetchSyllabusById(course.syllabusId).then(data => {
                setSyllabus(data);
            }).catch(error => {
                console.error("Error fetching syllabus:", error);
                toast.error("Error fetching syllabus");
            });
        }
    }, [course.syllabusId]);


    if (!syllabus) {
        return <div>Loading syllabus data...</div>;
    }

    const { weeklyTopics, readings, assignments, exams, gradingPolicy, name } = syllabus;

    return (
        <div className="container">
            <Header />
            <ToastContainer />
            <div className="ajouterSyllabusSection">
                <h1>Voir le syllabus pour le cours: {course.title} </h1>
                <form className="syllabus-form">
                    <div className="form-group flexTop">
                        <input
                            className="titreSyllabus"
                            type="text"
                            name="name"
                            value={syllabus.name}
                            disabled
                        />
                    </div>
                    <div className="flexible-form">
                        <div className="form-section">
                            <h2>Sujets Hebdomadaires</h2>
                            {Object.entries(weeklyTopics).map(([week, topic]) => (
                                <div key={week} className="form-group">
                                    <label>Semaine {week}:</label>
                                    <input
                                        type="text"
                                        value={topic}
                                        disabled
                                    />
                                </div>
                            ))}
                        </div>
                        <div className="form-section">
                            <h2>Readings</h2>
                            {Object.entries(readings).map(([week, reading]) => (
                                <div key={week} className="form-group">
                                    <label>Semaine {week}:</label>
                                    <input
                                        type="text"
                                        value={reading}
                                        disabled
                                    />
                                </div>
                            ))}
                        </div>
                        <div className="form-section">
                            <h2>Assignments</h2>
                            {Object.entries(assignments).map(([week, assignment]) => (
                                <div key={week} className="form-group">
                                    <label>Semaine {week}:</label>
                                    <input
                                        type="text"
                                        value={assignment}
                                        disabled
                                    />
                                </div>
                            ))}
                        </div>
                        <div className="form-section">
                            <h2>Exams</h2>
                            {Object.entries(exams).map(([week, exam]) => (
                                <div key={week} className="form-group">
                                    <label>Semaine {week}:</label>
                                    <input
                                        type="text"
                                        value={exam}
                                        disabled
                                    />
                                </div>
                            ))}
                        </div>
                    </div>
                    <div className="form-group">
                        <label>Politique de notation:</label>
                        <textarea
                            name="gradingPolicy"
                            value={gradingPolicy}
                            disabled

                        />
                    </div>
                    <div className="buttons-wrapper">
                        <button type="button" className="annuler" onClick={() => window.history.back()}>Retour</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ViewSyllabus;
