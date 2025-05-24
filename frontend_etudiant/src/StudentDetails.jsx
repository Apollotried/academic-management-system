import React from 'react';
import Details from "./components/StudentDetails/StudentDetails.jsx";
import Header from "./components/Header/header.jsx";
import NotificationsEtudiant from "./components/NotificationEtudiant/Notifications.jsx"

const StudentDetails = () => {
    return (
            <div className="container">
                <Header/>
                <Details/>
            </div>
    )
}


export default StudentDetails;
