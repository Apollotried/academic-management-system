import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchStudentById, updateStudent } from '../api/studentApi';
import { toast } from 'react-toastify';
import Header from "./Header/header.jsx";
import "./EditStudent.css";

const EditStudent = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        dob: '',
        address: '',
        phone: '',
        gender: ''
    });

    useEffect(() => {
        const getStudent = async () => {
            try {
                const student = await fetchStudentById(id);
                setFormData(student);
            } catch (error) {
                console.error('Error fetching student:', error);
            }
        };

        getStudent();
    }, [id]);

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
            await updateStudent(id, formData);
            toast.success('Étudiant mis à jour avec succès!');
            navigate(`/admin/students`, { replace: true });
        } catch (error) {
            console.error('Error updating student:', error);
            toast.error('Erreur lors de la mise à jour de l\'étudiant.');
        }
    };

    const resetForm = () => {
        setFormData({
            firstName: '',
            lastName: '',
            email: '',
            dob: '',
            address: '',
            phone: '',
            gender: ''
        });
    };

    const handleCancel = () => {
        resetForm();
        navigate(`/students`, { replace: true });
    };

    return (
        <div className="container">
            <Header />
            <main>
                <h1>Modifier L'Étudiant</h1>
                <form onSubmit={handleSubmit} className="edit-form">
                    <div className="form-group">
                        <label>
                            Nom:
                            <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Prénom:
                            <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Email:
                            <input type="email" name="email" value={formData.email} onChange={handleChange} />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Date de Naissance:
                            <input type="date" name="dob" value={formData.dob} onChange={handleChange} />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Adresse:
                            <input type="text" name="address" value={formData.address} onChange={handleChange} />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Téléphone:
                            <input type="text" name="phone" value={formData.phone} onChange={handleChange} />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Genre:
                            <select name="gender" value={formData.gender} onChange={handleChange}>
                                <option value="">Sélectionnez le genre</option>
                                <option value="male">Masculin</option>
                                <option value="female">Féminin</option>
                            </select>
                        </label>
                    </div>
                    <div className="buttons-wrapper">
                        <button type="submit" className="submit-button">Enregistrer les modifications</button>
                        <button type="button" className="annuler" onClick={handleCancel}>Annuler</button>
                    </div>
                </form>
            </main>
        </div>
    );
};

export default EditStudent;
