package com.idld.notificationservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity


public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String message;
    private String object;
    private String status;
    private LocalDateTime timestamp;

    public Email() {
    }

    public Email(String recipient, String message, String object, String status, LocalDateTime timestamp) {
        this.recipient = recipient;
        this.message = message;
        this.object = object;
        this.status = status;
        this.timestamp = timestamp;
    }
   }
