import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { addSyllabus } from '../../../api/coursApi';
import "./AddSyllabus.css";
import Header from "../../Header/header.jsx";

const AddSyllabus = () => {
    const [formData, setFormData] = useState({
        name: '',
        weeklyTopics: [{ semaine: 1, sujet: '' }],
        readings: [{ semaine: 1, lecture: '' }],
        assignments: [{ semaine: 1, devoir: '' }],
        exams: [{ semaine: 1, examen: '' }],
        gradingPolicy: ''
    });

    const navigate = useNavigate();

    const handleChange = (e, section, index) => {
        const { name, value } = e.target;
        const updatedSection = [...formData[section]];
        updatedSection[index][name] = value;
        setFormData({ ...formData, [section]: updatedSection });
    };

    const handleNameChange = (e) => {
        setFormData({ ...formData, name: e.target.value });
    };

    const handleAddField = (section) => {
        const newField = { semaine: formData[section].length + 1, [section.slice(0, -1)]: '' };
        setFormData({ ...formData, [section]: [...formData[section], newField] });
    };

    const handleDeleteField = (section, index) => {
        const updatedSection = formData[section].filter((_, i) => i !== index);
        setFormData({ ...formData, [section]: updatedSection });
    };

    const handleGradingPolicyChange = (e) => {
        setFormData({ ...formData, gradingPolicy: e.target.value });
    };

    const handleCancel = () => {
        setFormData({
            name: '',
            weeklyTopics: [{ semaine: 1, sujet: '' }],
            readings: [{ semaine: 1, lecture: '' }],
            assignments: [{ semaine: 1, devoir: '' }],
            exams: [{ semaine: 1, examen: '' }],
            gradingPolicy: ''
        });
        navigate('/courses');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Transform the formData into the expected structure
        const transformSectionToMap = (section) => {
            return formData[section].reduce((acc, item) => {
                acc[item.semaine] = item[section.slice(0, -1)];
                return acc;
            }, {});
        };

        const syllabusData = {
            name: formData.name,
            weeklyTopics: transformSectionToMap('weeklyTopics'),
            readings: transformSectionToMap('readings'),
            assignments: transformSectionToMap('assignments'),
            exams: transformSectionToMap('exams'),
            gradingPolicy: formData.gradingPolicy
        };

        try {
            await addSyllabus(syllabusData);
            toast.success('Syllabus ajouté avec succès!');
            navigate('/courses');
        } catch (error) {
            console.error('Erreur lors de l\'ajout du syllabus:', error);
            toast.error('Erreur lors de l\'ajout du syllabus.');
        }
    };

    return (
        <div className="container">
            <Header />

            <div className="ajouterSyllabusSection">
                <h1>Ajouter un nouveau Syllabus</h1>
                <form className="syllabus-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <input
                            className="titreSyllabus"
                            type="text"
                            name="name"
                            placeholder="Nom ou titre pour identifier votre Syllabus:"
                            value={formData.name}
                            onChange={handleNameChange}
                            required
                        />
                    </div>
                    <div className="flexible-form">
                        {['weeklyTopics', 'readings', 'assignments', 'exams'].map(section => (
                            <div key={section} className="form-section">
                                <h2>{section === 'weeklyTopics' ? 'Sujets Hebdomadaires' : section.charAt(0).toUpperCase() + section.slice(1)}</h2>
                                {formData[section].map((item, index) => (
                                    <div key={index} className="form-group">
                                        <label>Semaine {item.semaine}:</label>
                                        <div className="input-container">
                                            <input
                                                type="text"
                                                name={section.slice(0, -1)}
                                                value={item[section.slice(0, -1)]}
                                                onChange={(e) => handleChange(e, section, index)}
                                                required
                                            />
                                            <button
                                                type="button"
                                                className="delete-button"
                                                onClick={() => handleDeleteField(section, index)}
                                            >
                                                x
                                            </button>
                                        </div>
                                    </div>
                                ))}
                                <button type="button" onClick={() => handleAddField(section)}>Ajouter</button>
                            </div>
                        ))}
                    </div>
                    <div className="form-group">
                        <label>Politique de notation:</label>
                        <textarea
                            name="gradingPolicy"
                            value={formData.gradingPolicy}
                            onChange={handleGradingPolicyChange}
                            required
                        />
                    </div>
                    <div className="buttons-wrapper">
                        <button type="submit" className="submit-button">Ajouter le Syllabus</button>
                        <button type="button" className="annuler" onClick={handleCancel}>Annuler</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AddSyllabus;
