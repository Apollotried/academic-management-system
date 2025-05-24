import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "./StylesAff.css"

const NotificationList = () => {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await axios.get("http://localhost:8099/api/notificationsEmail/all");
        setNotifications(response.data);
      } catch (error) {
        console.error("Erreur lors de la récupération des notifications :", error);
        toast.error("Erreur lors de la récupération des notifications.");
      }
    };

    fetchNotifications();
  }, []);

  return (
    <div className="notification-list">
      <h2>Liste des Emails</h2>
      {notifications.length > 0 ? (
        <ul className="ul">
          {notifications.map((notification) => (
            <li className="list" key={notification.id}>
              <strong>Objet :</strong> {notification.object || "Sans objet"} <br />
              <strong>Message :</strong> {notification.message} <br />
              <strong>Destinataire :</strong> {notification.recipient}
            </li>
          ))}
        </ul>
      ) : (
        <p>Aucune notification disponible.</p>
      )}
    </div>
  );
};

export default NotificationList;
