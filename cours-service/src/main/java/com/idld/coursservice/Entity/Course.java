package com.idld.coursservice.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre du cours est obligatoire")
    private String title;

    @NotBlank(message = "La description du cours est obligatoire")
    private String description;

    private int credit;

    private long TeacherId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;
}
