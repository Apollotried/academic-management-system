import React, { useState, useEffect } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "./style.css"

const KafkaNotifications = () => {
    const [message, setMessage] = useState("");
    const [messages, setMessages] = useState([]);

    const sendMessage = async () => {
        if (!message.trim()) {
            toast.error("Veuillez entrer un message.");
            return;
        }

        try {
            await axios.post("http://localhost:7081/api/kafka/send", message, {
                headers: { "Content-Type": "text/plain" },
            });
            toast.success("Message envoyé avec succès à Kafka.");
            setMessage("");
        } catch (error) {
            console.error("Erreur lors de l'envoi du message à Kafka :", error);
            toast.error("Erreur lors de l'envoi du message à Kafka.");
        }
    };

    const fetchMessages = async () => {
        try {
            const response = await axios.get("http://localhost:7081/api/kafka/messages");
            setMessages(response.data);
        } catch (error) {
            console.error("Erreur lors de la récupération des messages Kafka :", error);
            toast.error("Erreur lors de la récupération des messages Kafka.");
        }
    };

    useEffect(() => {
        fetchMessages();
    }, []);

    return (
        <div className="kafka-notifications">
            <h2>Gestion des Messages Kafka</h2>

            <div className="send-message">
                <h3>Envoyer un message</h3>
                <textarea
                    rows="4"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Entrez votre message ici..."
                />
                <button onClick={sendMessage}>Envoyer</button>
            </div>

            <div className="received-messages">
                <h3>Messages Consommés</h3>
                {messages.length > 0 ? (
                    <ul>
                        {messages.map((msg, index) => (
                            <li key={index}>{msg}</li>
                        ))}
                    </ul>
                ) : (
                    <p>Aucun message consommé disponible.</p>
                )}
                <button onClick={fetchMessages}>Rafraîchir</button>
            </div>
        </div>
    );
};

export default KafkaNotifications;
