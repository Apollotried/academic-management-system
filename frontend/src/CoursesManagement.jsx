import React from 'react';
import Header from "./components/Header/header.jsx";
import CourseList from "./components/CoursesManagement/CourseList/CourseList.jsx";

const CoursesManagement = () => {
    return (
        <div className="container">
            <Header/>
            <CourseList/>
        </div>
    )
}


export default CoursesManagement;