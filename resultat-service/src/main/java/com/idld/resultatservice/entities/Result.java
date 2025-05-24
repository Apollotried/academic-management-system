package com.idld.resultatservice.entities;

import com.idld.resultatservice.Dtos.CourseDto;
import com.idld.resultatservice.Dtos.StudentDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long studentId;
    private long courseId;
    private double grade;

    @Transient
    private StudentDto student;

    @Transient
    private CourseDto course;

    public Result(long studentId, long courseId, double grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }
}
