package com.idld.etudiantservice.service;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;
import com.idld.etudiantservice.mapper.StudentMapperInter;
import com.idld.etudiantservice.model.Student;
import com.idld.etudiantservice.exception.StudentNotExists;
import com.idld.etudiantservice.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentServiceInter{
    private final StudentRepository studentRepository;
    private final StudentMapperInter studentMapperInter;

    public StudentServiceImpl(StudentRepository studentRepository, StudentMapperInter studentMapperInter) {
        this.studentRepository = studentRepository;
        this.studentMapperInter = studentMapperInter;
    }



    @Override
    public List<StudentDtoResponse> getAllStudents() {
    List<Student> students = studentRepository.findAll();
    List<StudentDtoResponse> studentDtoResponses = new ArrayList<>();
    for (Student student : students) {
        studentDtoResponses.add(studentMapperInter.ToStudentDto(student));
    }
        return studentDtoResponses;
    }

    @Override
    public long getTotalStudentsCount(){
        return studentRepository.count();
    }

    @Override
    public StudentDtoResponse getStudentById(long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotExists("Student not found"));
        return studentMapperInter.ToStudentDto(student);
    }

    @Override
    public void addStudent(StudentDtoRequest student) {
        studentRepository.save(studentMapperInter.ToStudent(student));

    }

    @Override
    public void updateStudent(long id, StudentDtoRequest student) {
        Student s = studentRepository.findById(id).orElseThrow(() ->
                new StudentNotExists("Student not found with ID: " + id));        assert s != null;
        s.setAddress(student.getAddress());
        s.setEmail(student.getEmail());
        s.setFirstName(student.getFirstName());
        s.setLastName(student.getLastName());
        s.setPhone(student.getPhone());

        studentRepository.save(s);
    }

    @Override
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);

    }
}
