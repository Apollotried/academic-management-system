package com.idld.etudiantservice.mapper;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;
import com.idld.etudiantservice.model.Student;

public interface StudentMapperInter {
    public Student ToStudent(StudentDtoRequest student);
    public StudentDtoResponse ToStudentDto(Student student);
}
