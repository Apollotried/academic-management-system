package com.idld.inscriptionservice.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseInscriptionDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String dateInscription;
}