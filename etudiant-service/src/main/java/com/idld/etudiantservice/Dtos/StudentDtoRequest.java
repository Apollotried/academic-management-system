package com.idld.etudiantservice.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDtoRequest {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    private String phone;
    private String address;

    private String gender;
    private LocalDate dob;
}
