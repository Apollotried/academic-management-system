package com.idld.resultatservice.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter

public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    private String gender;
    private LocalDate dob;
}
