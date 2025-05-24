import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchCourseById, updateCourse } from '../../api/coursApi.js';
import { toast } from 'react-toastify';
import Header from "../Header/header.jsx";


const EditCourse = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        credit: '',
        teacherId: ''
    });

    useEffect(() => {
        const getCourse = async () => {
            try {
                const course = await fetchCourseById(id);
                setFormData(course);
            } catch (error) {
                console.error('Error fetching course:', error);
            }
        };

        getCourse();
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
            await updateCourse(id, formData);
            toast.success('Cours mis à jour avec succès!');
            navigate(`/courses`, { replace: true });
        } catch (error) {
            console.error('Error updating course:', error);
            toast.error('Erreur lors de la mise à jour du cours.');
        }
    };

    const resetForm = () => {
        setFormData({
            title: '',
            description: '',
            credit: '',
            teacherId: ''
        });
    };

    const handleCancel = () => {
        resetForm();
        navigate(`/courses`, { replace: true });
    };

    return (
        <div className="container">
            <Header />
            <main>
                <h1>Modifier Le Cours</h1>
                <form onSubmit={handleSubmit} className="edit-form">
                    <div className="form-group">
                        <label>
                            Titre:
                            <input type="text" name="title" value={formData.title} onChange={handleChange} required />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Description:
                            <input type="text" name="description" value={formData.description} onChange={handleChange} required />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            Crédits:
                            <input type="number" name="credit" value={formData.credit} onChange={handleChange} required />
                        </label>
                    </div>
                    <div className="form-group">
                        <label>
                            ID de l'Enseignant:
                            <input type="number" name="teacherId" value={formData.teacherId} onChange={handleChange} required />
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

export default EditCourse;
