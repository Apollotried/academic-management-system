import React, {useEffect, useState} from 'react';
import "./ListeCourse.css"
import LogoutButton from "../LogoutButton.jsx";

import {getCoursesByStudentId} from "../../api/inscription.js";
import Modal from "react-modal";
import {useNavigate} from "react-router-dom";



const ListeCourse = () => {
    const navigate = useNavigate();
    const [studentCourses, setStudentCourses] = useState([]);
    const [selectedSyllabus, setSelectedSyllabus] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);


    useEffect(() => {
        const getCoursesByStudent = async () => {
            try{
                const fetchedCourses = await getCoursesByStudentId(1);
                setStudentCourses(fetchedCourses);
                console.log("Fetched Courses:", fetchedCourses);
            }catch (error) {
                console.error("Error fetching Courses:", error);
            }
        }

        getCoursesByStudent();

    }, []);

    const handleCourseClick = async (course) => {
        navigate('/user/view-syllabus', { state: { course } });
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedSyllabus(null);
    };

    return (
        <main>
            <h1>Gestion Des Ecoles 2024/2025</h1>
            <div className="hori">
                <h6 className="title">Liste de Vos cours</h6>
                <div className="config">
                    <LogoutButton />
                </div>
            </div>
            <div className="courses-container">
                <h1 className="courses-title">Vos Cours</h1>
                <div className="courses-list">
                    {studentCourses.map((course) => (
                        <div key={course.id} className="course-card">
                            <h2 className="course-name">{course.title}</h2>
                            <p className="course-desc"><strong>Description :</strong> {course.description}</p>
                            <p className="course-credits"><strong>Crédits :</strong> {course.credit}</p>
                            {course.syllabusId && (
                                <button
                                    onClick={() => handleCourseClick(course)}
                                    className="syllabus-button enabled"
                                >
                                    Voir Syllabus
                                </button>
                            )}

                        </div>
                    ))}
                </div>
            </div>


            {selectedSyllabus && (
                <Modal
                    onClose={closeModal}
                    isOpen={isModalOpen}
                    onRequestClose={closeModal}
                    contentLabel="Syllabus Details"
                    className="ajouterSyllabusSection"
                    overlayClassName="Overlay"
                >
                    <h1>Syllabus pour le cours : {selectedSyllabus.name}</h1>

                    <form className="syllabus-form">
                        <div className="form-group flexTop">
                            <input
                                className="titreSyllabus"
                                type="text"
                                name="name"
                                value={selectedSyllabus.name}
                                disabled
                            />
                        </div>
                        <div className="flexible-form">
                            <div className="form-section">
                                <h2>Sujets Hebdomadaires</h2>
                                {Object.entries(selectedSyllabus.weeklyTopics).map(([week, topic]) => (
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
                                {Object.entries(selectedSyllabus.readings).map(([week, reading]) => (
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
                                {Object.entries(selectedSyllabus.assignments).map(([week, assignment]) => (
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
                                {Object.entries(selectedSyllabus.exams).map(([week, exam]) => (
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
                                value={selectedSyllabus.gradingPolicy}
                                disabled
                            />
                        </div>

                    </form>
                </Modal>

            )}

        </main>
    );
};

export default ListeCourse;
