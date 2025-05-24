import React, { useState, useEffect } from 'react';
import { Badge, IconButton, Menu, MenuItem, Typography, Box, List, ListItem, ListItemText } from '@mui/material';
import NotificationsIcon from '@mui/icons-material/Notifications';

const NotificationComponent = () => {
    const [notifications, setNotifications] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);
    const [unreadCount, setUnreadCount] = useState(0);

    // Fetch user-specific and global notifications from SSE
    useEffect(() => {
        const userId = 1; // Replace with dynamic userId

        // Subscribe to user-specific notifications
        const userEventSource = new EventSource(`http://localhost:8099/sse/notifications/user/${userId}`);

        userEventSource.onmessage = (event) => {
            const newNotification = {
                id: Date.now(), // Unique ID for each notification
                message: event.data,
                read: false,
                type: 'user', // Indicate that this is a user-specific notification
            };

            // Add the new notification to the list
            setNotifications((prevNotifications) => {
                const updatedNotifications = [newNotification, ...prevNotifications];
                return updatedNotifications.slice(0, 10); // Keep only the last 10 notifications
            });

            setUnreadCount((prevCount) => prevCount + 1);
        };

        userEventSource.onerror = (error) => {
            console.error('User EventSource error:', error);
            userEventSource.close();
        };

        // Subscribe to global notifications
        const globalEventSource = new EventSource('http://localhost:8099/sse/notifications/global');

        globalEventSource.onmessage = (event) => {
            const newNotification = {
                id: Date.now(), // Unique ID for each notification
                message: event.data,
                read: false,
                type: 'global', // Indicate that this is a global notification
            };

            // Add the new notification to the list
            setNotifications((prevNotifications) => {
                const updatedNotifications = [newNotification, ...prevNotifications];
                return updatedNotifications.slice(0, 10); // Keep only the last 10 notifications
            });

            setUnreadCount((prevCount) => prevCount + 1);
        };

        globalEventSource.onerror = (error) => {
            console.error('Global EventSource error:', error);
            globalEventSource.close();
        };

        // Cleanup on unmount
        return () => {
            userEventSource.close();
            globalEventSource.close();
        };
    }, []);

    // Handle click on the notification bell
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    // Handle closing the notification dropdown
    const handleClose = () => {
        setAnchorEl(null);
    };

    // Handle clicking a notification
    const handleNotificationClick = (id) => {
        // Mark the notification as read
        setNotifications((prevNotifications) =>
            prevNotifications.map((notification) =>
                notification.id === id ? { ...notification, read: true } : notification
            )
        );

        // Decrement the unread count
        setUnreadCount((prevCount) => (prevCount > 0 ? prevCount - 1 : 0));
    };

    return (
        <Box>
            {/* Notification Bell Icon */}
            <IconButton color="inherit" onClick={handleClick}>
                <Badge badgeContent={unreadCount} color="error">
                    <NotificationsIcon />
                </Badge>
            </IconButton>

            {/* Notification Dropdown */}
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleClose}
                PaperProps={{
                    style: {
                        width: '300px',
                        maxHeight: '400px',
                        overflow: 'auto',
                    },
                }}
            >
                {notifications.length === 0 ? (
                    <MenuItem>
                        <Typography variant="body2" color="textSecondary">
                            No new notifications.
                        </Typography>
                    </MenuItem>
                ) : (
                    <List>
                        {notifications.map((notification) => (
                            <ListItem
                                key={notification.id}
                                button
                                onClick={() => handleNotificationClick(notification.id)}
                                style={{
                                    backgroundColor: notification.read ? '#f5f5f5' : '#fff',
                                }}
                            >
                                <ListItemText
                                    primary={notification.message}
                                    secondary={
                                        <>
                                            <Typography variant="body2" color="textSecondary">
                                                {new Date(notification.id).toLocaleString()}
                                            </Typography>
                                            <Typography variant="caption" color="textSecondary">
                                                {notification.type === 'user' ? 'Personal' : 'Global'}
                                            </Typography>
                                        </>
                                    }
                                />
                            </ListItem>
                        ))}
                    </List>
                )}
            </Menu>
        </Box>
    );
};

export default NotificationComponent;