import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import "./AddSyllabus.css";
import Header from "../../Header/header.jsx";
import { fetchSyllabusById, updateSyllabus } from "../../../api/coursApi.js";
const ViewSyllabus = () => {
    const location = useLocation();
    const { course } = location.state || {};
    const [syllabus, setSyllabus] = useState(null);
    const [isChanged, setIsChanged] = useState(false);

    useEffect(() => {
        if (course && course.syllabusId) {
            fetchSyllabusById(course.syllabusId).then(data => {
                setSyllabus(data);
            }).catch(error => {
                console.error("Error fetching syllabus:", error);
                toast.error("Error fetching syllabus");
            });
        }
    }, [course]);

    if (!course) {
        return <div>No course data provided.</div>;
    }

    if (!syllabus) {
        return <div>Loading syllabus data...</div>;
    }

    const { id: courseId, title } = course;
    const { weeklyTopics, readings, assignments, exams, gradingPolicy, name } = syllabus;

    const handleChange = (e) => {
        const { name, value } = e.target;
        setSyllabus((prevSyllabus) => ({
            ...prevSyllabus,
            [name]: value,
        }));
        setIsChanged(true);
    };

    const handleWeeklyTopicChange = (week, value) => {
        setSyllabus((prevSyllabus) => ({
            ...prevSyllabus,
            weeklyTopics: {
                ...prevSyllabus.weeklyTopics,
                [week]: value,
            },
        }));
        setIsChanged(true);
    };

    const handleReadingChange = (week, value) => {
        setSyllabus((prevSyllabus) => ({
            ...prevSyllabus,
            readings: {
                ...prevSyllabus.readings,
                [week]: value,
            },
        }));
        setIsChanged(true);
    };

    const handleAssignmentChange = (week, value) => {
        setSyllabus((prevSyllabus) => ({
            ...prevSyllabus,
            assignments: {
                ...prevSyllabus.assignments,
                [week]: value,
            },
        }));
        setIsChanged(true);
    };

    const handleExamChange = (week, value) => {
        setSyllabus((prevSyllabus) => ({
            ...prevSyllabus,
            exams: {
                ...prevSyllabus.exams,
                [week]: value,
            },
        }));
        setIsChanged(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Create a new syllabus object with the correct structure
        const updatedSyllabus = {
            syllabus_id: syllabus.syllabus_id,
            name: syllabus.name,
            weeklyTopics: syllabus.weeklyTopics,
            readings: syllabus.readings,
            assignments: syllabus.assignments,
            exams: syllabus.exams,
            gradingPolicy: syllabus.gradingPolicy,
        };

        console.log("Request payload:", JSON.stringify(updatedSyllabus, null, 2)); // Log the payload for inspection

        try {
            const response = await updateSyllabus(syllabus.syllabus_id, updatedSyllabus);
            console.log("Response:", response); // Log the response for inspection
            setIsChanged(false);
            toast.success('Syllabus updated successfully!');
        } catch (error) {
            console.error("Error updating syllabus:", error);
            toast.error('Error updating syllabus.');
        }
    };

    return (
        <div className="container">
            <Header />
            <ToastContainer />
            <div className="ajouterSyllabusSection">
                <h1>Voir le syllabus pour le cours: {title} (ID: {courseId})</h1>
                <form className="syllabus-form" onSubmit={handleSubmit}>
                    <div className="form-group flexTop">
                        <input
                            className="titreSyllabus"
                            type="text"
                            name="name"
                            placeholder="Nom ou titre pour identifier votre Syllabus:"
                            value={name}
                            onChange={handleChange}
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
                                        onChange={(e) => handleWeeklyTopicChange(week, e.target.value)}
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
                                        onChange={(e) => handleReadingChange(week, e.target.value)}
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
                                        onChange={(e) => handleAssignmentChange(week, e.target.value)}
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
                                        onChange={(e) => handleExamChange(week, e.target.value)}
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
                            onChange={handleChange}
                        />
                    </div>
                    <div className="buttons-wrapper">
                        <button type="button" className="annuler" onClick={() => window.history.back()}>Retour</button>
                        {isChanged && (
                            <button type="submit" className="submit-button">Appliquer les modifications</button>
                        )}
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ViewSyllabus;
