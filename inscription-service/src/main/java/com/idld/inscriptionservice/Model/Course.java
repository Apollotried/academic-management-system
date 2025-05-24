package com.idld.inscriptionservice.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Course {
    private Long id;
    private String title;
    private String description;
    private int credit;
    private String instructor;
}