package com.marouane.authservice.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer Id;
    private String token;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
