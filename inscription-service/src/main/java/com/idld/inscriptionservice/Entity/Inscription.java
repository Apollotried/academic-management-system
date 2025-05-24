package com.idld.inscriptionservice.Entity;


import com.idld.inscriptionservice.Model.Course;
import com.idld.inscriptionservice.Model.Student;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    @Transient
    private Student student;

    private Long courseId;
    @Transient
    private Course course;

    private String dateInscription;
}