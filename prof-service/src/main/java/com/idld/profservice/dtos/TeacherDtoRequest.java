package com.idld.profservice.dtos;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDtoRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @Email
    private String email;

    private String phone;
    private String address;
}
