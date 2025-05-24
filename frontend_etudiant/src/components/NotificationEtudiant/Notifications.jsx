import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaBell } from "react-icons/fa"; // Notification bell icon
import "./notifications.css"; // Import CSS file

const Notifications = ({ studentId = 1 }) => {
    const [generalMessages, setGeneralMessages] = useState([]);
    const [gradeMessages, setGradeMessages] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0); // Unread notification count
    const [showNotifications, setShowNotifications] = useState(false); // Toggle notification list

    // Fetch general messages
const fetchGeneralMessages = async () => {
    try {
        const response = await axios.get("http://localhost:7081/api/kafka/messages");
        const rawMessages = response.data;

        // Clean the messages by trimming whitespace and removing newlines
        const cleanedMessages = rawMessages.map((message) => message.replace(/[\r\n]+/g, ' ').trim());

        setGeneralMessages((prevMessages) => {
            const uniqueMessages = cleanedMessages.filter(
                (msg) => !prevMessages.includes(msg)
            );
            if (uniqueMessages.length > 0) {
                setUnreadCount((prevCount) => prevCount + uniqueMessages.length); // Increase unread count
                return [...prevMessages, ...uniqueMessages];
            }
            return prevMessages; // No change if messages are duplicates
        });
    } catch (error) {
        console.error("Error fetching general messages:", error);
    }
};


    // Fetch grade-related messages
    const fetchGradeMessages = async () => {
        if (!studentId) return; // Skip fetching if studentId is not provided

        try {
            const response = await axios.get(`http://localhost:8082/api/results/studentKafka/${studentId}`);
            if (response.data && response.data.length > 0) {
                const newMessages = await Promise.all(response.data.map(async (gradeUpdate) => {
                    const courseResponse = await axios.get(`http://localhost:8082/api/results/course-info/${gradeUpdate.courseId}`);
                    const courseInfo = courseResponse.data; // Assuming course data includes details you need

                    return `Grade Updated: Student ID: ${gradeUpdate.studentId}, Course: ${courseInfo.title}, New Grade: ${gradeUpdate.grade}`;
                }));

                setGradeMessages((prevMessages) => {
                    const uniqueMessages = newMessages.filter(
                        (msg) => !prevMessages.includes(msg)
                    );
                    if (uniqueMessages.length > 0) {
                        setUnreadCount((prevCount) => prevCount + uniqueMessages.length); // Increase unread count
                        return [...prevMessages, ...uniqueMessages];
                    }
                    return prevMessages;
                });
            }
        } catch (error) {
            console.error("Error fetching grade messages:", error);
        }
    };

    // Fetch messages on component mount or when studentId changes
    useEffect(() => {
        fetchGeneralMessages();
        fetchGradeMessages();
    }, [studentId]);

    // Toggle notification list and reset unread count
    const handleNotificationClick = () => {
        setShowNotifications(!showNotifications);
        if (!showNotifications) {
            setUnreadCount(0); // Reset unread count when notifications are viewed
        }
    };

    return (
        <div style={{ position: "relative", display: "inline-block" }}>
            {/* Notification icon with badge */}
            <div className="notification-icon" onClick={handleNotificationClick}>
                <FaBell size={20} />
                {unreadCount > 0 && (
                    <span className="notification-badge">{unreadCount}</span>
                )}
            </div>

            {/* Notification list */}
            {showNotifications && (
                <div className="notification-list">
                    <h4>Notifications</h4>

                    {/* General notifications */}
                    {generalMessages.length > 0 && (
                        <div>
                            <h5>General Notifications</h5>
                            <ul>
                                {generalMessages.map((message, index) => (
                                    <li key={index} className="general-notification">
                                        {message}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}

                    {/* Grade notifications */}
                    {gradeMessages.length > 0 && (
                        <div>
                            <h5>Grade Notifications</h5>
                            <ul>
                                {gradeMessages.map((message, index) => (
                                    <li key={index} className="grade-notification">
                                        {message}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default Notifications;
