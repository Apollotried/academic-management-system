import React from 'react';
import Header from "./components/Header/header.jsx";
import ListeCourse from "./components/coursList/ListeCourse.jsx";
import "./components/styles.css"

const StudentManagement = () => {
    return (
        <div className="container">
            <Header/>
            <ListeCourse/>
        </div>
    )
}


export default StudentManagement;