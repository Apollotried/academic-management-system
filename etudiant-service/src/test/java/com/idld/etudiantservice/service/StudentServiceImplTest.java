package com.idld.etudiantservice.service;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;
import com.idld.etudiantservice.exception.StudentNotExists;
import com.idld.etudiantservice.mapper.StudentMapperInter;
import com.idld.etudiantservice.model.Student;
import com.idld.etudiantservice.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapperInter studentMapperInter;

    @InjectMocks
    StudentServiceImpl studentServiceImpl;



    @Test
    public void testGetAllStudents_ShouldReturnMappedResponses(){
        List<Student> students = new ArrayList<>();
        Student s1 = new Student();
        s1.setEmail("aze@gmail.com");
        s1.setFirstName("Aze");
        s1.setId(5L);
        s1.setLastName("Aze");
        students.add(s1);

        List<StudentDtoResponse> expectedResponses  = new ArrayList<>();
        StudentDtoResponse dto1 = new StudentDtoResponse();
        dto1.setEmail("aze@gmail.com");
        dto1.setFirstName("Aze");
        dto1.setId(5L);
        dto1.setLastName("Aze");
        expectedResponses.add(dto1);

        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapperInter.ToStudentDto(s1)).thenReturn(dto1);

        List<StudentDtoResponse> actualResponses = studentServiceImpl.getAllStudents();

        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses.get(0).getFirstName(), actualResponses.get(0).getFirstName());

        verify(studentRepository, times(1)).findAll();
        verify(studentMapperInter, times(1)).ToStudentDto(s1);

    }

    @Test
    public void testGetStudentById_ShouldReturnMappedResponse() {
        // Arrange
        long studentId = 6L;
        Student student = new Student();
        student.setId(studentId);
        student.setFirstName("Jane");

        StudentDtoResponse expectedResponse = new StudentDtoResponse();
        expectedResponse.setFirstName("Jane");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapperInter.ToStudentDto(student)).thenReturn(expectedResponse);

        // Act
        StudentDtoResponse actualResponse = studentServiceImpl.getStudentById(studentId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("Jane", actualResponse.getFirstName());
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentMapperInter, times(1)).ToStudentDto(student);
    }

    @Test
    public void testAddStudent_ShouldSaveMappedEntity() {

        StudentDtoRequest dtoRequest = new StudentDtoRequest();
        dtoRequest.setFirstName("Alice");

        Student student = new Student();
        student.setFirstName("Alice");

        when(studentMapperInter.ToStudent(dtoRequest)).thenReturn(student);


        studentServiceImpl.addStudent(dtoRequest);


        verify(studentMapperInter, times(1)).ToStudent(dtoRequest);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testUpdateStudent_ShouldUpdateEntity() {
        long studentId = 1L;
        StudentDtoRequest dtoRequest = new StudentDtoRequest();
        dtoRequest.setFirstName("Updated");
        dtoRequest.setEmail("updated@example.com");

        Student existingStudent = new Student();
        existingStudent.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));


        studentServiceImpl.updateStudent(studentId, dtoRequest);


        assertEquals("Updated", existingStudent.getFirstName());
        assertEquals("updated@example.com", existingStudent.getEmail());
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    public void testUpdateStudent_ShouldThrowException_WhenStudentNotExists() {

        long studentId = 1L;
        StudentDtoRequest dtoRequest = new StudentDtoRequest();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());


        assertThrows(StudentNotExists.class, () -> studentServiceImpl.updateStudent(studentId, dtoRequest));
        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    public void testDeleteStudent_ShouldCallRepositoryDelete() {

        long studentId = 1L;


        studentServiceImpl.deleteStudent(studentId);


        verify(studentRepository, times(1)).deleteById(studentId);
    }
}