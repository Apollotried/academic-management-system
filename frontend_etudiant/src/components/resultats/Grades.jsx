import React, { useEffect, useState } from "react";

import "./Results.css";
import {fetchResultDetailsByStudentId, fetchStudentsGradeByCoursId} from "../../api/resultApi.js";

const Grades = () => {
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const getResults = async () => {
            try {
                const data = await fetchResultDetailsByStudentId(10);
                setResults(data);
            } catch (err) {
                setError("Failed to load results.");
            } finally {
                setLoading(false);
            }
        };

        const test = async () =>{
            const data = fetchStudentsGradeByCoursId(10);
            console.log(data);
        }

        getResults();
        test();
    }, []);

    if (loading) return <p className="loading">Loading results...</p>;
    if (error) return <p className="error">{error}</p>;

    return (
        <div className="results-container">
            <h2>Student Results</h2>
            {results.length === 0 ? (
                <p className="no-results">No results available.</p>
            ) : (
                <table className="results-table">
                    <thead>
                    <tr>
                        <th>Course ID</th>
                        <th>Course Name</th>
                        <th>Grade</th>
                    </tr>
                    </thead>
                    <tbody>
                    {results.map((result) => (
                        <tr key={result.courseId}>
                            <td>{result.courseId}</td>
                            <td>{result.course.title}</td>
                            <td>{result.grade.toFixed(2)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default Grades;
