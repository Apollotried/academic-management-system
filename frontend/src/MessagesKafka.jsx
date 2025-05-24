import React from 'react';
import MessageKafka from "./components/KafkaMessaging/KafkaMessaging.jsx";
import Header from "./components/Header/header.jsx";

const NotificationEmail = () => {
    return (
            <div className="container">
                <Header/>
                <MessageKafka/>
            </div>
    )
}


export default NotificationEmail;