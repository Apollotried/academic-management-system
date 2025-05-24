package com.idld.coursservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
    private String title;
    private String description;
    private int credit;
    private long TeacherId;

}

