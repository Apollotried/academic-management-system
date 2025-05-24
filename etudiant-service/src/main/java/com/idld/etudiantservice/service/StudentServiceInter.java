package com.idld.etudiantservice.service;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;
import com.idld.etudiantservice.model.Student;

import java.util.List;

public interface StudentServiceInter {
    public List<StudentDtoResponse> getAllStudents();
    public StudentDtoResponse getStudentById(long id);
    public void addStudent(StudentDtoRequest student);
    public void updateStudent(long id, StudentDtoRequest student);
    public void deleteStudent(long id);
    public long getTotalStudentsCount();

}
