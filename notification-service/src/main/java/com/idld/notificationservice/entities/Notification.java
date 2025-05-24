package com.idld.notificationservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId; // ID of the user receiving the notification
    private String title;
    private String message;
    private String type; // e.g., EMAIL, WEBSOCKET
    private boolean isRead; // For UI to show unread notifications

    @CreationTimestamp
    private LocalDateTime createdAt;
}
