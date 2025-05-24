package com.idld.etudiantservice.mapper;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;
import com.idld.etudiantservice.model.Student;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class StudentMapperImplTest {
    private final StudentMapperImpl studentMapper = new StudentMapperImpl();


    @Test
    void testToStudent_ShouldMapCorrectly() {
        StudentDtoRequest dto = new StudentDtoRequest("John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                "123 Main St",
                "test",
                LocalDate.of(1995, 10, 25));


        Student student = studentMapper.ToStudent(dto);


        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john.doe@example.com", student.getEmail());
        assertEquals("123 Main St", student.getAddress());
        assertEquals("1234567890", student.getPhone());
    }

    @Test
    public void testToStudent_ShouldReturnNull_WhenInputIsNull() {

        Student student = studentMapper.ToStudent(null);
        assertNull(student);
    }

    @Test
    public void testToStudentDto_ShouldMapCorrectly() {

        Student student = new Student();
        student.setFirstName("Jane");
        student.setLastName("Smith");
        student.setEmail("jane.smith@example.com");
        student.setAddress("456 Elm St");
        student.setPhone("9876543210");


        StudentDtoResponse dtoResponse = studentMapper.ToStudentDto(student);


        assertNotNull(dtoResponse);
        assertEquals("Jane", dtoResponse.getFirstName());
        assertEquals("Smith", dtoResponse.getLastName());
        assertEquals("jane.smith@example.com", dtoResponse.getEmail());
        assertEquals("456 Elm St", dtoResponse.getAddress());
        assertEquals("9876543210", dtoResponse.getPhone());
    }

    @Test
    public void testToStudentDto_ShouldReturnNull_WhenInputIsNull() {
        StudentDtoResponse dtoResponse = studentMapper.ToStudentDto(null);

        assertNull(dtoResponse);
    }
}