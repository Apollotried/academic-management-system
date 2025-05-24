import React from 'react';
import Modal from 'react-modal';

const AddCourseModal = ({ isOpen, onRequestClose, formData, handleChange, handleSubmit }) => {
    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="Ajouter un nouveau cours"
            className="ModalAdd"
            overlayClassName="Overlay"
        >
            <h2>Ajouter un nouveau cours</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                    placeholder="Titre"
                    required
                />
                <input
                    type="text"
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    placeholder="Description"
                    required
                />
                <input
                    type="number"
                    name="credit"
                    value={formData.credit}
                    onChange={handleChange}
                    placeholder="Crédits"
                    required
                />
                <button type="submit">Ajouter</button>
                <button type="button" onClick={onRequestClose}>Annuler</button>
            </form>
        </Modal>
    );
};

export default AddCourseModal;
