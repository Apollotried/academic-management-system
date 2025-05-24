import React, { useState, useEffect } from 'react';
import './resultHome.css';
import { fetchCourses } from "../../api/coursApi.js";
import { fetchStudentsGradeByCoursId } from "../../api/resultApi.js";
import Pagination from "../pagination/pagination.jsx";
import LogoutButton from "../LogoutButton.jsx";

const ResultHome = () => {
    const [courses, setCourses] = useState([]);
    const [students, setStudents] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);

    const [search, setSearch] = useState("");
    const [gradeFilter, setGradeFilter] = useState("all");
    const [sortOrder, setSortOrder] = useState("nameAsc");
    const [currentPage, setCurrentPage] = useState(1);
    const studentPerPage = 6;

    // Filter and Pagination logic
    const filteredStudents = students.filter(item =>
        item.student &&
        (item.student.firstName.toLowerCase().includes(search.toLowerCase()) ||
            item.student.lastName.toLowerCase().includes(search.toLowerCase())) &&
        (gradeFilter === "all" ||
            (gradeFilter === "sup" && item.grade > 10) ||
            (gradeFilter === "inf" && item.grade < 10) ||
            (gradeFilter === "egal" && item.grade === 10))
    );


    const sortedStudents = filteredStudents.sort((a, b) => {
        if (!a.student || !b.student) {
            return 0;
        }
        if (sortOrder === 'nameAsc') {
            return a.student.firstName.localeCompare(b.student.firstName);
        } else if (sortOrder === 'nameDesc') {
            return b.student.firstName.localeCompare(a.student.firstName);
        } else if (sortOrder === 'gradeAsc') {
            return a.grade - b.grade;
        } else if (sortOrder === 'gradeDesc') {
            return b.grade - a.grade;
        }
        return 0;
    });

    const handleGradeFilterChange = (e) => {
        setGradeFilter(e.target.value);
    };


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
            const response = await fetchStudentsGradeByCoursId(courseId);
            setStudents(response);
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
                                <label htmlFor="filterGrade">Filtrer par note:</label>
                                <select id="filterGrade" name="filterGrade" onChange={handleGradeFilterChange}>
                                    <option value="all">All</option>
                                    <option value="sup">&gt; 10</option>
                                    <option value="inf">&lt; 10</option>
                                    <option value="egal">= 10</option>
                                </select>
                            </div>
                            <div className="group">
                                <label htmlFor="sortOrder">Trier par:</label>
                                <select id="sortOrder" name="sortOrder" value={sortOrder} onChange={handleSortChange}>
                                    <option value="nameAsc">Nom (A-Z)</option>
                                    <option value="nameDesc">Nom (Z-A)</option>
                                    <option value="gradeAsc">Note (croissant)</option>
                                    <option value="gradeDesc">Note (décroissant)</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <table className="tableEtudiants">
                        <thead>
                        <tr>
                            <th>Id de l'étudiant</th>
                            <th>Nom de l'étudiant</th>
                            <th>Note</th>
                        </tr>
                        </thead>
                        <tbody>
                        {currentStudents.map(studentE => (
                            <tr key={studentE.studentId}>
                                <td>{studentE.studentId}</td>
                                <td>
                                    {studentE.student
                                        ? `${studentE.student.firstName || 'N/A'} ${studentE.student.lastName || ''}`
                                        : 'N/A'}
                                </td>
                                <td>{studentE.grade}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={setCurrentPage}
                    />
                </>
            )}
        </main>
    );
};

export default ResultHome;
