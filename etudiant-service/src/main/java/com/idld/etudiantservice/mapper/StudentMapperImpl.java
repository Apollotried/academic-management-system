package com.idld.etudiantservice.mapper;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;
import com.idld.etudiantservice.model.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class StudentMapperImpl implements StudentMapperInter {
    @Override
    public Student ToStudent(StudentDtoRequest student) {
        if (student == null) {
            return null;
        }

        Student s = new Student();
        BeanUtils.copyProperties(student, s);
        return s;
    }

    @Override
    public StudentDtoResponse ToStudentDto(Student student) {
        if (student == null) {
            return null;
        }

        StudentDtoResponse dto = new StudentDtoResponse();
        BeanUtils.copyProperties(student, dto);
        return dto;
    }
}
