package com.idld.coursservice.DTO;


import com.idld.coursservice.Entity.Syllabus;
import com.idld.coursservice.modele.Teacher;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private int credit;

    private Long TeacherId;
    private Long syllabusId;

}
