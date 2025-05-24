import React from 'react';
import AffEmail from "./components/Notifications/NotificationList.jsx";
import Header from "./components/Header/header.jsx";

const NotificationEmail = () => {
    return (
            <div className="container">
                <Header/>
                <AffEmail/>
            </div>
    )
}


export default NotificationEmail;