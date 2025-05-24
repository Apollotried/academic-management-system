import React, { useState, useEffect } from 'react';
import './resultHome.css';
import { fetchCourses } from "../../api/coursApi.js";
import {applyBatchGrades, fetchStudentsGradeByCoursId} from "../../api/resultApi.js";
import Pagination from "../pagination/pagination.jsx";
import {fetchStudentsByCourseId} from "../../api/inscription.js";
import {toast} from "react-toastify";
import LogoutButton from "../LogoutButton.jsx";

const GradeHome = () => {
    const [courses, setCourses] = useState([]);
    const [students, setStudents] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [studentGrades, setStudentGrades] = useState({});


    const [search, setSearch] = useState("");
    const [sortOrder, setSortOrder] = useState("nameAsc");
    const [currentPage, setCurrentPage] = useState(1);
    const studentPerPage = 6;

    // Filter and Pagination logic
    const filteredStudents = students
        .filter(item => item && item.firstName && item.lastName && (
            item.firstName.toLowerCase().includes(search.toLowerCase()) ||
            item.lastName.toLowerCase().includes(search.toLowerCase())
        ));


    const sortedStudents = filteredStudents.sort((a, b) => {
        if (sortOrder === 'nameAsc') {
            return a.firstName.localeCompare(b.firstName);
        } else if (sortOrder === 'nameDesc') {
            return b.firstName.localeCompare(a.firstName);
        }
        return 0;
    });

    const indexOfLastStudent = currentPage * studentPerPage;
    const indexOfFirstStudent = indexOfLastStudent - studentPerPage;
    const currentStudents = sortedStudents.slice(indexOfFirstStudent, indexOfLastStudent);

    const totalPages = Math.ceil(filteredStudents.length / studentPerPage);

    useEffect(() => {
        const getCourses = async () => {
            try {
                const fetchedCourses = await fetchCourses();
                setCourses(fetchedCourses);
                console.log("Fetched Courses:", fetchedCourses);
            } catch (error) {
                console.error("Error fetching Courses:", error);
            }
        }

        getCourses();
    }, []);

    const handleCourseClick = async (courseId) => {
        setSelectedCourse(courseId);
        try {
            const response = await fetchStudentsByCourseId(courseId);
            setStudents(response || []);
            console.log("Fetched Students:", response);
        } catch (error) {
            console.error("Error fetching Students:", error);
        }
    };

    const handleSearchChange = (e) => {
        setSearch(e.target.value);
        setCurrentPage(1); // Reset to the first page when search term changes
    };

    const handleSortChange = (e) => {
        setSortOrder(e.target.value);
    };


    const handleGradeChange = (studentId, grade) => {
        setStudentGrades(prevGrades => ({
            ...prevGrades,
            [studentId]: grade,
        }));
    };


    const handleApplyNotes = async () => {
        const invalidGrades = Object.entries(studentGrades).some(([studentId, grade]) => {
            const numericGrade = parseFloat(grade);
            return isNaN(numericGrade) || numericGrade < 0 || numericGrade > 20;
        });

        if (invalidGrades) {
            toast.error("Note must be a number between 0 and 20 for all students.");
            return;
        }

        const gradesToSubmit = Object.entries(studentGrades).map(([studentId, grade]) => ({
            studentId,
            courseId: selectedCourse,  // Add courseId here
            grade: parseFloat(grade),  // Ensure grade is a number
        }));

        try {
            await applyBatchGrades(gradesToSubmit); // Assuming endpoint accepts courseId and grades array
            toast.success("Notes applied successfully!"); // Use toastify for success message
        } catch (error) {
            console.error("Error applying grades:", error);
            toast.error("Failed to apply notes. Please try again.");
        }
    };


    return (
        <main>
            <h1>Gestion Des Ecoles</h1>
            <div className="hori">
                <h6 className="title">Gestion des Résultats</h6>
                <div className="config">
                    <LogoutButton />
                </div>
            </div>

            <h1 className="title2">Veuillez choisir un cours</h1>

            <div className="listeCourse">
                {courses.map(course => (
                    <div key={course.id} className="courseCard" onClick={() => handleCourseClick(course.id)}>
                        {course.title}
                    </div>
                ))}
            </div>

            {selectedCourse && (
                <>
                    <h1 className="title3">Liste des étudiants du cours sélectionné</h1>

                    <div className="optionSettings">
                        <form action="" method="GET" className="searchBar">
                            <input
                                type="text"
                                id="search"
                                placeholder="Rechercher par Nom ou Prenom"
                                name="search"
                                value={search}
                                onChange={handleSearchChange}
                            />
                        </form>

                        <div className="filterSortOptions">
                            <div className="group">
                                <label htmlFor="sortOrder">Trier par:</label>
                                <select id="sortOrder" name="sortOrder" value={sortOrder} onChange={handleSortChange}>
                                    <option value="nameAsc">Nom (A-Z)</option>
                                    <option value="nameDesc">Nom (Z-A)</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <table className="tableEtudiants">
                        <thead>
                        <tr>
                            <th>Id de l'étudiant</th>
                            <th>Prenom</th>
                            <th>Nom</th>
                            <th>Note</th>
                        </tr>
                        </thead>
                        <tbody>
                        {currentStudents.map(studentE => (
                            <tr key={studentE.id}>
                                <td>{studentE.id}</td>
                                <td>{studentE.firstName}</td>
                                <td>{studentE.lastName}</td>
                                <td>
                                    <input
                                        type="number"
                                        className="gradeInput"
                                        placeholder="Entrer la note"
                                        min="0"
                                        max="20"
                                        onChange={(e) => handleGradeChange(studentE.id, e.target.value)}
                                    />
                                </td>

                            </tr>
                        ))}
                        </tbody>

                    </table>
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={setCurrentPage}
                    />


                    <button onClick={handleApplyNotes} className="ApplyNotes">Appliquer les Notes</button>

                </>
            )}
        </main>
    );
};

export default GradeHome;
