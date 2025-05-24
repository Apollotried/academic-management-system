import React, { useState, useEffect } from 'react';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';

const NotificationsEtudiant = () => {
    const [notifications, setNotifications] = useState([]);
    const [visible, setVisible] = useState(false);  // Toggle visibility

    // Fonction pour récupérer les notifications
    const fetchNotifications = async () => {
        try {
            const response = await axios.get("http://localhost:8081/api/kafka/messages");
            if (response.data && response.data.length > 0) {
                setNotifications(response.data);
                response.data.forEach(message => {
                    toast.info(message, {
                        position: "top-right",
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: true,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                    });
                });
            } else {
                toast.info("Aucune nouvelle notification.");
            }
        } catch (error) {
            console.error("Erreur lors de la récupération des messages Kafka:", error);
            toast.error("Erreur lors de la récupération des notifications.");
        }
    };

    // Afficher/Masquer les notifications
    const toggleNotifications = () => {
        setVisible(!visible);
    };

    useEffect(() => {
        fetchNotifications(); // Chargement initial des notifications
        const interval = setInterval(fetchNotifications, 30000); // Rafraîchir toutes les 30 secondes
        return () => clearInterval(interval);
    }, []);

    return (
        <div>
            <ToastContainer />
            <button onClick={toggleNotifications} className="notification-toggle-button">
                {visible ? "Masquer les notifications" : "Afficher les notifications"}
            </button>
            {visible && (
                <div className="notifications-list">
                    <h3>Notifications Étudiant</h3>
                    {notifications.length > 0 ? (
                        <ul>
                            {notifications.map((notification, index) => (
                                <li key={index}>{notification}</li>
                            ))}
                        </ul>
                    ) : (
                        <p>Aucune notification disponible.</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default NotificationsEtudiant;
