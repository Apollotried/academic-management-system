import React from 'react';
import Modal from 'react-modal';

const AddStudentModal = ({ isOpen, onRequestClose, formData, handleChange, handleSubmit }) => {

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="Ajouter un nouveau etudiant"
            className="ModalAdd"
            overlayClassName="Overlay"
        >
            <h2>Ajouter un nouveau etudiant</h2>
            <form onSubmit={handleSubmit}>
                <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} placeholder="Prénom" required />
                <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} placeholder="Nom" required />
                <input type="email" name="email" value={formData.email} onChange={handleChange} placeholder="Email" required />
                <input type="date" name="dob" value={formData.dob} onChange={handleChange} placeholder="Date de naissance" required />
                <input type="text" name="address" value={formData.address} onChange={handleChange} placeholder="Adresse" required />
                <input type="text" name="phone" value={formData.phone} onChange={handleChange} placeholder="Numéro de téléphone" required />
                <select name="gender" value={formData.gender} onChange={handleChange} required>
                    <option value="" disabled hidden>Genre</option>
                    <option value="male">Masculin</option>
                    <option value="female">Féminin</option>
                </select>
                <button type="submit">Ajouter</button>
                <button type="button" onClick={onRequestClose}>Annuler</button>
            </form>
        </Modal>
    );
};

export default AddStudentModal;
