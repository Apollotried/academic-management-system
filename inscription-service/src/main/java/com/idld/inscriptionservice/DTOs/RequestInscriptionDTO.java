package com.idld.inscriptionservice.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestInscriptionDTO {
    private Long studentId;
    private Long courseId;
}