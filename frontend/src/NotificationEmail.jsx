import React from 'react';
import Header from "./components/Header/header.jsx";
import SendEmail from "./components/Notifications/SendNotification.jsx";

const NotificationEmail = () => {
    return (
            <div className="container">
                <Header/>
                <SendEmail/>
            </div>
    )
}


export default NotificationEmail;