import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import { getCoursesByStudentId, AssignCoursesToStudent } from './api/inscription.js';
import {fetchCourses} from './api/coursApi.js';
import { toast } from 'react-toastify';
import './CourseAssignmentModal.css';
import Pagination from "./components/pagination/pagination.jsx";

const CourseAssignmentModal = ({ isOpen, onRequestClose, studentId }) => {
    const [courses, setCourses] = useState([]);
    const [selectedCourses, setSelectedCourses] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const CoursePerPage = 5;

    useEffect(() => {
        if (isOpen && studentId) {
            const getCourses = async () => {
                try {
                    const fetchedCourses = await fetchCourses();
                    setCourses(fetchedCourses);
                } catch (error) {
                    console.error("Error fetching courses:", error);
                }
            };

            getCourses();
        }
    }, [isOpen, studentId]);

    const handleCourseChange = (courseId) => {
        setSelectedCourses(prevSelected => {
            if (prevSelected.includes(courseId)) {
                return prevSelected.filter(id => id !== courseId);
            } else {
                return [...prevSelected, courseId];
            }
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await AssignCoursesToStudent({ studentId, courseIds: selectedCourses });
            toast.success('Cours affectés avec succès!');
            onRequestClose();
        } catch (error) {
            console.error("Error assigning courses:", error);
            toast.error('Erreur lors de l\'affectation des cours.');
        }
    };

    const indexOfLastCourse = currentPage * CoursePerPage;
    const indexOfFirstCourse = indexOfLastCourse - CoursePerPage;
    const currentCourses = courses.slice(indexOfFirstCourse, indexOfLastCourse);

    const totalPages = Math.ceil(courses.length / CoursePerPage);

    return (
        <Modal isOpen={isOpen} onRequestClose={onRequestClose} contentLabel="Affecter des Cours" className="ModalAssign" overlayClassName="Overlay">
            <h2>Affecter des Cours</h2>
            <form onSubmit={handleSubmit}>
                {currentCourses.map(course => (
                    <div key={course.id} className="form-group">
                        <div className="label">
                            <input
                                type="checkbox"
                                value={course.id}
                                checked={selectedCourses.includes(course.id)}
                                onChange={() => handleCourseChange(course.id)}
                            />
                            <div className="content">
                                {course.title}
                            </div>
                        </div>
                    </div>
                ))}
                <button type="submit" className="submit-button">Affecter les cours</button>
            </form>
            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </Modal>
    );
};

export default CourseAssignmentModal;
