package com.idld.inscriptionservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignCoursesRequestDTO {
    private Long studentId;
    private List<Long> courseIds;
}
