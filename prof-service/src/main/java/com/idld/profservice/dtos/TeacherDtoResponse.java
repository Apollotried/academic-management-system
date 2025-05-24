package com.idld.profservice.dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDtoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
