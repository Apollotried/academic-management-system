import React, { useEffect, useState } from 'react';
import {fetchCourseInfo, fetchResultsByStudent} from "../../api/resultApi.js";

const StudentResults = () => {
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [courses, setCourses] = useState({});
    const [currentPage, setCurrentPage] = useState(1);
    const resultsPerPage = 5;

    const fetchStudentResults = async () => {
        try {
            setLoading(true);
            const response = await fetchResultsByStudent();
            if (!response || response.length === 0) {
                setResults([]);
                return;
            }

            setResults(response);


            const courseIds = [...new Set(response.map((result) => result.courseId))];
            const courseDetails = await fetchCourseDetails(courseIds);
            setCourses(courseDetails);
        } catch (err) {
            setError('Erreur lors de la récupération des résultats.');
        } finally {
            setLoading(false);
        }
    };

    const fetchCourseDetails = async (courseIds) => {
        const courseDetails = {};
        try {
            for (const id of courseIds) {
                const response = await fetchCourseInfo(id);
                if(response && response.title){
                    courseDetails[id] = response.title;
                }

            }
        } catch (err) {
            console.error('Erreur lors de la récupération des cours:', err);
        }
        return courseDetails;
    };

    useEffect(() => {
            fetchStudentResults();
    }, []);

    const indexOfLastResult = currentPage * resultsPerPage;
    const indexOfFirstResult = indexOfLastResult - resultsPerPage;
    const currentResults = results.slice(indexOfFirstResult, indexOfLastResult);

    const totalPages = Math.ceil(results.length / resultsPerPage);

    if (loading) {
        return <p>Chargement des résultats...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    const handleNext = () => {
        if (currentPage < totalPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    const handlePrev = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const containerStyle = {
        padding: '20px',
        backgroundColor: '#f4f4f9',
        borderRadius: '8px',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
        maxWidth: '1000px',
        margin: '20px auto'
    };

    const tableStyle = {
        width: '100%',
        borderCollapse: 'collapse',
        marginTop: '20px',
        transition: 'transform 0.3s ease', // Effet de zoom sur la table
    };

    const tableHoverStyle = {
        transform: 'scale(1.02)', // Zoom léger
    };

    const thStyle = {
        backgroundColor: '#4CAF50',
        color: 'white',
        padding: '12px 15px',
        textAlign: 'left',
        fontWeight: 'bold'
    };

    const tdStyle = {
        padding: '12px 15px',
        borderBottom: '1px solid #ddd'
    };

    const trStyle = {
        backgroundColor: '#fff',
        transition: 'background-color 0.3s ease'
    };

    const trHoverStyle = {
        backgroundColor: '#f1f1f1'
    };

    const buttonStyle = {
        backgroundColor: '#4CAF50',
        color: 'white',
        border: 'none',
        padding: '10px 20px',
        fontSize: '16px',
        cursor: 'pointer',
        borderRadius: '5px',
        margin: '0 10px',
        transition: 'background-color 0.3s ease',
    };

    const buttonHoverStyle = {
        backgroundColor: '#45a049',
    };

    return (
        <div style={containerStyle}>
            <h2 style={{ textAlign: 'center', color: '#333' }}>Résultats de l'Étudiant</h2>
            {results.length > 0 ? (
                <>
                    <table
                        style={tableStyle}
                        onMouseEnter={(e) => e.currentTarget.style.transform = tableHoverStyle.transform}
                        onMouseLeave={(e) => e.currentTarget.style.transform = ''}
                    >
                        <thead>
                            <tr style={trStyle}>
                                <th style={thStyle}>Cours</th>
                                <th style={thStyle}>Note</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentResults.map((result) => (
                                <tr key={result.id} style={trStyle} onMouseEnter={(e) => e.currentTarget.style.backgroundColor = trHoverStyle.backgroundColor} onMouseLeave={(e) => e.currentTarget.style.backgroundColor = ''}>
                                    <td style={tdStyle}>{courses[result.courseId] || 'Cours non disponible'}</td>
                                    <td style={tdStyle}>{result.grade}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    {/* Pagination */}
                    <div style={{ textAlign: 'center', marginTop: '20px' }}>
                        <button
                            style={buttonStyle}
                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = buttonHoverStyle.backgroundColor}
                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#4CAF50'}
                            onClick={handlePrev}
                            disabled={currentPage === 1}
                        >
                            Précédent
                        </button>
                        <span style={{ margin: '0 10px' }}>Page {currentPage} sur {totalPages}</span>
                        <button
                            style={buttonStyle}
                            onMouseEnter={(e) => e.currentTarget.style.backgroundColor = buttonHoverStyle.backgroundColor}
                            onMouseLeave={(e) => e.currentTarget.style.backgroundColor = '#4CAF50'}
                            onClick={handleNext}
                            disabled={currentPage === totalPages}
                        >
                            Suivant
                        </button>
                    </div>
                </>
            ) : (
                <p>Aucun résultat trouvé.</p>
            )}
        </div>
    );
};

export default StudentResults;
