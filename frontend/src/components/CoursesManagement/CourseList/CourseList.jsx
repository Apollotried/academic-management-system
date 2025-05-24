import React, {useEffect, useState} from 'react';
import Pagination from "../../pagination/pagination.jsx";
import "./CourseList.css"
import {getCoursesByStudentId} from "../../../api/inscription.js";
import Modal from "react-modal";
import {
    addCourse,
    assignSyllabusToCourse,
    deleteCourse,
    fetchCourseById,
    fetchCourses,
    fetchSyllabus
} from "../../../api/coursApi.js";
import AddStudentModal from "../../../addStudentModal.jsx";
import {addStudent, deleteStudent, fetchStudentById, fetchStudents} from "../../../api/studentApi.js";
import {toast} from "react-toastify";
import AddCourseModal from "../addCourseModal.jsx";
import { useNavigate } from 'react-router-dom';
import LogoutButton from "../../LogoutButton.jsx";

const CourseList = () => {
    const navigate = useNavigate();
    const [courses, setCourses] = useState([]);
    const [ModalIsOpen, setModalIsOpen] = React.useState(false);
    const [isAddCourseModalOpen, setIsAddCourseModalOpen] = useState(false);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [selectedCourseId, setSelectedCourseId]= useState('');


    const [syllabi, setSyllabi] = useState([]);
    const [isAssignSyllabusModalOpen, setIsAssignSyllabusModalOpen] = useState(false);
    const [selectedSyllabusId, setSelectedSyllabusId] = useState('');

    const [search, setSearch] = useState("");

    const [currentPage, setCurrentPage] = useState(1);
    const coursPerPage = 5;

    // Filter and Pagination logic
    const filteredCourses = courses.filter(
        item => item.title.toLowerCase().includes(search.toLowerCase())
    );

    const indexOfLastCours = currentPage * coursPerPage;
    const indexOfFirstCours = indexOfLastCours - coursPerPage;
    const currentCours = filteredCourses.slice(indexOfFirstCours, indexOfLastCours);

    const totalPages = Math.ceil(filteredCourses.length / coursPerPage);






    const initialCourseForm = {
        title: '',
        description: '',
        credit: '',
        teacherId: '',
        syllabusId:''
    };
    const [formData, setFormData] = useState(initialCourseForm);

    const getSyllabi = async () => {
        try {
            const fetchedSyllabi = await fetchSyllabus();
            setSyllabi(fetchedSyllabi.filter(syllabus => !syllabus.assigned)); // Keep only unassigned syllabi
        } catch (error) {
            console.error("Error fetching syllabi:", error);
        }
    };

    useEffect(() => {
        const getCourses = async () => {
            try {
                const fetchedCourses = await fetchCourses();
                setCourses(fetchedCourses);
                console.log("Fetched Courses:", fetchedCourses);
            } catch (error) {
                console.error("Error fetching Courses:", error);
            }
        };

        getCourses();
        getSyllabi();
    }, []);


    const handleRowClick = async (id) => {
        try {
            const course = await fetchCourseById(id);
            setSelectedCourse(course);
        } catch (error) {
            console.error("Error fetching course id:", error);
            return;
        }


        setModalIsOpen(true);
    };

    const closeModal = ()=>{
        setModalIsOpen(false);
    }

    const resetForm = ()=>{
        setFormData(initialCourseForm);
    }

    const openAddCourseModal = () => {
        setIsAddCourseModalOpen(true);
    };

    const closeAddCourseModal = () => {
        resetForm();
        setIsAddCourseModalOpen(false);
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
            await addCourse(formData);
            toast.success('Cours ajouté avec succès!');

            // Get the data again
            const updatedCourses = await fetchCourses();
            setCourses(updatedCourses);

            closeAddCourseModal();
        } catch (error) {
            console.error("Error adding cours infos", error);
            toast.error('Erreur lors de l\'ajout du cours.');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteCourse(id);
            toast.success('Cours supprimé avec succès!');
            // Get the data again
            const updatedCourse = await fetchCourses();
            setCourses(updatedCourse);

            closeModal();
        } catch (error) {
            console.error("Error deleting Course", error);
            toast.error('Erreur lors de la suppression du Cours');
        }
    };

    const handleEditClick = (id) => {
        navigate(`/admin/edit-course/${id}`, { replace: true });
        closeModal();
    };

    const handleAddSyllabus = () => {
        navigate('/admin/add-syllabus');
    };

    const openAssignSyllabusModal = () => {
        setIsAssignSyllabusModalOpen(true);
    };
    const closeAssignSyllabusModal = () => {
        setIsAssignSyllabusModalOpen(false);
    };

    const handleAssignSyllabus = async () => {
        if (!selectedCourseId || !selectedSyllabusId)
            return;
        try {
            await assignSyllabusToCourse(selectedCourseId, selectedSyllabusId);
            toast.success('Syllabus assigné avec succès!');

            const updatedCourses = await fetchCourses();
            setCourses(updatedCourses);

            await getSyllabi();

            closeAssignSyllabusModal();
        } catch (error) {
            console.error("Error assigning syllabus:", error);
            toast.error('Erreur lors de l\'attribution du syllabus.');
        }
    };

    const handleSyllabusChange = (e) => {
        setSelectedSyllabusId(e.target.value);
    };

    const handleAssignSyllabusClick = (id) =>{
        setSelectedCourseId(id);
        openAssignSyllabusModal();
    }

    const handleViewSyllabus = (course) => {
        navigate('/admin/view-syllabus', { state: { course } });
    };



    return (
        <main className="CourseList">
            <h1>Gestion Des Ecoles 2024/2025</h1>
            <div className="hori">
                <h6 className="title">Gestion des Cours</h6>
                <div className="config">
                    <LogoutButton />
                </div>
            </div>

            <div className="listeCours">
                <div className="containerFlex">
                    <h2>Liste des Cours</h2>
                    <div class="buttonsTop">
                        <button className="ajouterC" onClick={openAddCourseModal}>Ajouter un nouveau Cours</button>
                        <button className="ajouterC" onClick={handleAddSyllabus}>Ajouter un nouveau Syllabus</button>
                    </div>

                </div>

                <form action="" method="GET" className="searchBar">
                    <input
                        type="text"
                        id="search"
                        placeholder="Rechercher par titre"
                        name="search"
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </form>
                <table>
                    <thead>
                    <tr>
                        <th>Code</th>
                        <th>title</th>
                        <th>description</th>
                        <th>credit</th>
                    </tr>
                    </thead>
                    <tbody>
                    {currentCours.map(cours => (
                        <tr key={cours.id}>
                            <td onClick={() => handleRowClick(cours.id)} style={{cursor: 'pointer'}}>{cours.id}</td>
                            <td onClick={() => handleRowClick(cours.id)} style={{cursor: 'pointer'}}>{cours.title}</td>
                            <td onClick={() => handleRowClick(cours.id)}
                                style={{cursor: 'pointer'}}>{cours.description}</td>
                            <td onClick={() => handleRowClick(cours.id)} style={{cursor: 'pointer'}}>{cours.credit}</td>
                            <td className="buttons_col">
                                {cours.syllabusId ? (
                                    <button className="course-button syllabus-button"
                                            onClick={()=>{
                                                handleViewSyllabus(cours);
                                            }}
                                    >
                                        Voir Syllabus
                                    </button>
                                ) : (
                                    <button className="course-button"
                                            onClick={() => handleAssignSyllabusClick(cours.id)}>
                                        Affecter un Syllabus
                                    </button>
                                )}
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


                {selectedCourse && (
                    <Modal
                        isOpen={ModalIsOpen}
                        onRequestClose={closeModal}
                        contentLabel="Cours Details"
                        className="Modal"
                        overlayClassName="Overlay"
                    >
                        <div className="x_container">
                            <h2>Détails du cours</h2>
                            <button className="fermer" onClick={closeModal}>x</button>
                        </div>
                        <div className="info-grid">
                            <p><strong>Id:</strong> {selectedCourse.id}</p>
                            <p><strong>Nom:</strong> {selectedCourse.title}</p>
                            <p><strong>Email:</strong> {selectedCourse.description}</p>
                            <p><strong>Phone:</strong> {selectedCourse.credit}</p>
                        </div>

                        <div className="bottom">
                            <h3>
                                Voir Syllabus
                            </h3>
                            <div className="button-container">
                                <button className="supprimer" onClick={() => handleDelete(selectedCourse.id)}>Supprimer</button>
                                <button className="modifier" onClick={() => handleEditClick(selectedCourse.id)}>Modifier</button>
                            </div>
                        </div>
                    </Modal>
                )}

                <AddCourseModal
                    isOpen={isAddCourseModalOpen}
                    onRequestClose={closeAddCourseModal}
                    formData={formData}
                    handleChange={handleChange}
                    handleSubmit={handleSubmit}
                />

                {setSelectedCourseId && (
                    <Modal
                        isOpen={isAssignSyllabusModalOpen}
                        onRequestClose={closeAssignSyllabusModal}
                        contentLabel="Assign Syllabus"
                        className="Modal"
                        overlayClassName="Overlay"
                    >
                        <div className="x_container">
                            <h2>Affecter un Syllabus</h2>
                            <button className="fermer" onClick={closeAssignSyllabusModal}>x</button>
                        </div>
                        <div className="info-grid">
                            <label>Choisir un Syllabus:</label>
                            <select className="selectS" value={selectedSyllabusId} onChange={handleSyllabusChange} required>
                                <option className="SelectSO" value="" disabled={true}>Sélectionner un Syllabus</option>
                                {syllabi.map((syllabus) => (
                                    <option key={syllabus.syllabus_id} value={syllabus.syllabus_id}>
                                        {syllabus.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="buttons-wrapper">
                            <button onClick={handleAssignSyllabus} className="submit-button">Assigner</button>
                            <button onClick={closeAssignSyllabusModal} className="annuler">Annuler</button>
                        </div>
                    </Modal>
                )}


            </div>


        </main>
    )
}

export default CourseList;
