package com.idld.resultatservice.Dtos;

import lombok.*;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTORequest {
    private long studentId;
    private long courseId;
    private double grade;
}
