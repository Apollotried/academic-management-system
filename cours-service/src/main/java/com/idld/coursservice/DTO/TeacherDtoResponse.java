package com.idld.coursservice.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter

public class TeacherDtoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
