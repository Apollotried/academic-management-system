package com.idld.resultatservice.Dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class CourseDto {
    private Long id;
    private String title;
    private String description;
    private int credit;

    private Long TeacherId;
    private Long syllabusId;
}
