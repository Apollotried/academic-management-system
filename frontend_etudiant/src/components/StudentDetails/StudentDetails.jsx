import React, { useEffect, useState } from 'react';

import { fetchStudentById } from "../../api/studentApi.js";

const StudentDetails = ({ studentId = 1 }) => {
    const [student, setStudent] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchStudentDetails = async () => {
        try {
            console.log('Fetching student details for ID:', studentId);
            setLoading(true);

            const response = await fetchStudentById(studentId);
            console.log('Response data:', response.data);

            if (response) {
                setStudent(response);
            } else {
                setError('Aucune donnée trouvée.');
            }
        } catch (err) {
            console.error('Error fetching student details:', err.response ? err.response.data : err.message); // Afficher l'erreur complète
            setError("Erreur lors de la récupération des données de l'étudiant.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStudentDetails();
    }, []);

    const containerStyle = {
        maxWidth: '800px',
        margin: '30px auto',
        padding: '20px',
        backgroundColor: '#fff',
        borderRadius: '8px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    };

    const headingStyle = {
        textAlign: 'center',
        fontFamily: 'Arial, sans-serif',
        color: '#333',
        fontSize: '24px',
        marginBottom: '20px',
    };

    const tableStyle = {
        width: '100%',
        borderCollapse: 'collapse',
        marginTop: '20px',
        transition: 'transform 0.3s ease', // Effet de zoom sur la table
    };

    const tableHoverStyle = {
        transform: 'scale(1.05)', // Zoom plus important
    };

    const thStyle = {
        backgroundColor: '#2980b9',
        color: 'white',
        padding: '12px 15px',
        textAlign: 'left',
        fontWeight: 'bold',
    };

    const tdStyle = {
        padding: '12px 15px',
        borderBottom: '1px solid #ddd',
    };

    const trStyle = {
        backgroundColor: '#fff',
        transition: 'background-color 0.3s ease',
    };

    const trHoverStyle = {
        backgroundColor: '#f1f1f1',
    };

    const loadingErrorStyle = {
        textAlign: 'center',
        fontSize: '18px',
        fontWeight: 'bold',
    };

    const errorStyle = {
        color: '#e74c3c',
    };

    const buttonStyle = {
        backgroundColor: '#2980b9',
        color: 'white',
        border: 'none',
        padding: '10px 20px',
        marginTop: '20px',
        borderRadius: '5px',
        cursor: 'pointer',
        fontSize: '16px',
        transition: 'background-color 0.3s ease',
    };

    const buttonHoverStyle = {
        backgroundColor: '#3498db',
    };

    if (loading) {
        return <p style={{ ...loadingErrorStyle, color: '#e74c3c' }}>Chargement des informations...</p>;
    }

    if (error) {
        return <p style={{ ...loadingErrorStyle, ...errorStyle }}>{error}</p>;
    }

    return (
        <div style={containerStyle}>
            <h2 style={headingStyle}>Informations de l'étudiant</h2>
            {student ? (
                <table
                    style={tableStyle}
                    onMouseEnter={(e) => e.currentTarget.style.transform = tableHoverStyle.transform}
                    onMouseLeave={(e) => e.currentTarget.style.transform = ''}
                >
                    <thead>
                        <tr style={trStyle}>
                            <th style={thStyle}>Champ</th>
                            <th style={thStyle}>Détails</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr style={trStyle}>
                            <td style={tdStyle}><strong>ID :</strong></td>
                            <td style={tdStyle}>{student.id}</td>
                        </tr>
                        <tr style={trStyle}>
                            <td style={tdStyle}><strong>Prénom :</strong></td>
                            <td style={tdStyle}>{student.firstName}</td>
                        </tr>
                        <tr style={trStyle}>
                            <td style={tdStyle}><strong>Nom :</strong></td>
                            <td style={tdStyle}>{student.lastName}</td>
                        </tr>
                        <tr style={trStyle}>
                            <td style={tdStyle}><strong>Email :</strong></td>
                            <td style={tdStyle}>{student.email}</td>
                        </tr>
                        <tr style={trStyle}>
                            <td style={tdStyle}><strong>Téléphone :</strong></td>
                            <td style={tdStyle}>{student.phone}</td>
                        </tr>
                        <tr style={trStyle}>
                            <td style={tdStyle}><strong>Adresse :</strong></td>
                            <td style={tdStyle}>{student.address}</td>
                        </tr>
                    </tbody>
                </table>
            ) : (
                <p>Aucune donnée trouvée.</p>
            )}
        </div>
    );
};

export default StudentDetails;
