import React, { useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "./styles.css";

const SendNotification = () => {
  const [recipient, setRecipient] = useState("");
  const [subject, setSubject] = useState("");
  const [message, setMessage] = useState("");

  const handleSend = async () => {
    try {
      const notification = { recipient, object: subject, message };
      await axios.post("http://localhost:8099/api/notificationsEmail/send", notification);
      toast.success("Notification envoyée avec succès !");
    } catch (error) {
      console.error("Erreur lors de l'envoi de la notification :", error);
      toast.error("Erreur lors de l'envoi de la notification.");
    }
  };

  return (
    <div className="send-notification">
      <h2>Envoyer un Email</h2>
      <form onSubmit={(e) => e.preventDefault()}>
        <div>
          <label>Destinataire :</label>
          <input
            type="email"
            value={recipient}
            onChange={(e) => setRecipient(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Objet :</label>
          <input
            type="text"
            value={subject}
            onChange={(e) => setSubject(e.target.value)}
          />
        </div>
        <div>
          <label>Message :</label>
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            required
          />
        </div>
        <button type="button" onClick={handleSend}>
          Envoyer
        </button>
      </form>
    </div>
  );
};

export default SendNotification;
