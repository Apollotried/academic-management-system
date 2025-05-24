import React, { useEffect, useState } from "react";
import { deleteStudent, fetchStudents, fetchStudentById, addStudent } from "../../api/studentApi";
import { getCoursesByStudentId } from "../../api/inscription.js";
import './StudentList.css';
import Modal from 'react-modal';
import AddStudentModal from "../../addStudentModal.jsx";
import { toast } from "react-toastify";
import { useNavigate } from 'react-router-dom';
import CourseAssignmentModal from "../../CourseAssignmentModal.jsx";
import Pagination from "../pagination/pagination.jsx";
import LogoutButton from "../LogoutButton.jsx";

const StudentList = () => {
    const navigate = useNavigate();
    const [students, setStudents] = useState([]);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [selectedStudentId, setSelectedStudentId] = useState(null);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [modal2IsOpen, setModal2IsOpen] = useState(false);
    const [coursesOpen, setCoursesOpen] = useState(false);
    const [isAddStudentModalOpen, setIsAddStudentModalOpen] = useState(false);
    const [studentCourses, setStudentCourses] = useState([]);
    const [search, setSearch] = useState("");

    const [currentPage, setCurrentPage] = useState(1);
    const studentsPerPage = 5;

    const initialDataForm = {
        firstName: '',
        lastName: '',
        email: '',
        dob: '',
        address: '',
        phone: '',
        gender: ''
    };
    const [formData, setFormData] = useState(initialDataForm);

    useEffect(() => {
        const getStudents = async () => {
            try {
                const fetchedStudents = await fetchStudents();
                setStudents(fetchedStudents);
                console.log("Fetched Students:", fetchedStudents);
            } catch (error) {
                console.error("Error fetching students:", error);
            }
        };

        getStudents();
    }, []);

    const handleRowClick = async (id) => {
        try {
            const student = await fetchStudentById(id);
            setSelectedStudent(student);
        } catch (error) {
            console.error("Error fetching student id:", error);
            return;
        }

        try {
            const courses = await getCoursesByStudentId(id);
            setStudentCourses(courses);
        } catch (error) {
            console.error("Error fetching courses for student:", error);
        }

        setModalIsOpen(true);
    };

    const closeModal = () => {
        setModalIsOpen(false);
        setSelectedStudent(null);
        setStudentCourses([]); // Clear courses when closing the modal
    };

    const openAddStudentModal = () => {
        setIsAddStudentModalOpen(true);
    };

    const closeAddStudentModal = () => {
        resetForm();
        setIsAddStudentModalOpen(false);
    };

    const resetForm = () => {
        setFormData(initialDataForm);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await addStudent(formData);
            toast.success('Étudiant ajouté avec succès!');

            // Get the data again
            const updatedStudents = await fetchStudents();
            setStudents(updatedStudents);

            closeAddStudentModal();
        } catch (error) {
            console.error("Error adding student infos", error);
            toast.error('Erreur lors de l\'ajout de l\'étudiant.');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteStudent(id);
            toast.success('Étudiant supprimé avec succès!');
            // Get the data again
            const updatedStudents = await fetchStudents();
            setStudents(updatedStudents);

            closeModal();
        } catch (error) {
            console.error("Error deleting student", error);
            toast.error('Erreur lors de la suppression de l\'étudiant.');
        }
    };

    const handleEditClick = (id) => {
        navigate(`/admin/edit-student/${id}`, { replace: true });
        closeModal();
    };

    const handleAssignButton = (studentId) => {
        setSelectedStudentId(studentId);
        setModal2IsOpen(true);
    };

    const closeModal2 = () => {
        setModal2IsOpen(false);
        setSelectedStudentId(null);
    };

    // Filter and Pagination logic
    const filteredStudents = students.filter(
        item => item.firstName.toLowerCase().includes(search.toLowerCase()) ||
            item.lastName.toLowerCase().includes(search.toLowerCase())
    );

    const indexOfLastStudent = currentPage * studentsPerPage;
    const indexOfFirstStudent = indexOfLastStudent - studentsPerPage;
    const currentStudents = filteredStudents.slice(indexOfFirstStudent, indexOfLastStudent);

    const totalPages = Math.ceil(filteredStudents.length / studentsPerPage);

    return (
        <main>
            <h1>Gestion Des Ecoles 2024/2025</h1>
            <div className="hori">
                <h6 className="title">Gestion des Etudiants</h6>
                <div className="config">
                    <LogoutButton />
                </div>
            </div>

            <div className="listeEtud">
                <div className="containerFlex">
                    <h2>Liste des Étudiants</h2>
                    <button className="ajouter" onClick={openAddStudentModal}>Ajouter un nouveau etudiant</button>
                </div>

                <form action="" method="GET" className="searchBar">
                    <input
                        type="text"
                        id="search"
                        placeholder="Rechercher par nom ou prénom"
                        name="search"
                        onChange={(e) => {
                            setSearch(e.target.value);
                            setCurrentPage(1);
                        }}
                    />
                </form>
                <table>
                    <thead>
                    <tr>
                        <th>Code</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                    </tr>
                    </thead>
                    <tbody>
                    {currentStudents.map(student => (
                        <tr key={student.id}>
                            <td onClick={() => handleRowClick(student.id)} style={{ cursor: 'pointer' }}>{student.id}</td>
                            <td onClick={() => handleRowClick(student.id)} style={{ cursor: 'pointer' }}>{student.firstName}</td>
                            <td onClick={() => handleRowClick(student.id)} style={{ cursor: 'pointer' }}>{student.lastName}</td>
                            <td onClick={() => handleRowClick(student.id)} style={{ cursor: 'pointer' }}>{student.email}</td>
                            <td>
                                <button className="course-button" onClick={() => handleAssignButton(student.id)}>Affecter des cours</button>
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

                {selectedStudent && (
                    <Modal
                        isOpen={modalIsOpen}
                        onRequestClose={closeModal}
                        contentLabel="Student Details"
                        className="Modal"
                        overlayClassName="Overlay"
                    >
                        <div className="x_container">
                            <h2>Détails de l'étudiant</h2>
                            <button className="fermer" onClick={closeModal}>x</button>
                        </div>
                        <div className="info-grid">
                            <p><strong>Id:</strong> {selectedStudent.id}</p>
                            <p><strong>Nom:</strong> {selectedStudent.firstName} {selectedStudent.lastName}</p>
                            <p><strong>Email:</strong> {selectedStudent.email}</p>
                            <p><strong>Phone:</strong> {selectedStudent.phone}</p>
                            <p><strong>Address:</strong> {selectedStudent.address}</p>
                            <p><strong>Genre:</strong> {selectedStudent.gender}</p>
                        </div>
                        <p className="dob"><strong>Date de Naissance:</strong> {selectedStudent.dob}</p>

                        <div className="bottom">
                            <h3 onClick={() => setCoursesOpen(!coursesOpen)} style={{ cursor: 'pointer' }}>
                                Cours Inscrits {coursesOpen ? '-' : '+'}
                            </h3>
                            <div className="button-container">
                                <button className="supprimer" onClick={() => handleDelete(selectedStudent.id)}>Supprimer</button>
                                <button className="modifier" onClick={() => handleEditClick(selectedStudent.id)}>Modifier</button>
                            </div>
                        </div>
                        {coursesOpen && (
                            <ul>
                                {studentCourses.map(course => (
                                    <li key={course.id}>{course.title} - {course.description}</li>
                                ))}
                            </ul>
                        )}
                    </Modal>
                )}

                <AddStudentModal
                    isOpen={isAddStudentModalOpen}
                    onRequestClose={closeAddStudentModal}
                    formData={formData}
                    handleChange={handleChange}
                    handleSubmit={handleSubmit}
                />

                <CourseAssignmentModal
                    isOpen={modal2IsOpen}
                    onRequestClose={closeModal2}
                    studentId={selectedStudentId}
                />
            </div>
        </main>
    );
};


export default StudentList;
