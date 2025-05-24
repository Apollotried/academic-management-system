import React from 'react';
import Header from "./components/Header/header.jsx";
import StudentList from "./components/StudentList/StudentList.jsx";

const StudentManagement = () => {
    return (
            <div className="container">
                <Header/>
                <StudentList/>
            </div>
    )
}


export default StudentManagement;