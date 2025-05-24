package com.idld.inscriptionservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class courseDTO {
    private Long id;
    private String title;
    private String description;
    private int credit;

    private Long syllabusId;
    private long TeacherId;
}
