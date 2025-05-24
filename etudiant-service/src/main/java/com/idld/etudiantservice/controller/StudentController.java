package com.idld.etudiantservice.controller;

import com.idld.etudiantservice.Dtos.StudentDtoRequest;
import com.idld.etudiantservice.Dtos.StudentDtoResponse;

import com.idld.etudiantservice.service.StudentServiceInter;
import jakarta.validation.Valid;
import jakarta.ws.rs.InternalServerErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api")
public class StudentController {
    StudentServiceInter studentServiceInter;

    public StudentController(StudentServiceInter studentServiceInter) {
        this.studentServiceInter = studentServiceInter;
    }


    @GetMapping("/students")

    public List<StudentDtoResponse> getStudents() {
        return studentServiceInter.getAllStudents();
    }

    @GetMapping("/students/count")
    public long getStudentsCount() {
        return studentServiceInter.getTotalStudentsCount();
    }


    @GetMapping( "/specificStudent")
    public StudentDtoResponse getSpecificStudent(
            @RequestHeader( "X-Authenticated-UserId") String userIdHeader,
            @RequestHeader("X-Authenticated-Roles") String roles) {

        validateUserAccess(roles);
        try {
            long userId = Long.parseLong(userIdHeader);
            return studentServiceInter.getStudentById(userId);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID format");
        }
    }

    @GetMapping( "/students/{id:\\d+}")
    public StudentDtoResponse getStudent(@PathVariable Long id) {
            return studentServiceInter.getStudentById(id);
    }




    @PostMapping("/students")
    public void create(@RequestBody @Valid StudentDtoRequest student,
                       @RequestHeader("X-Authenticated-Roles") String roles
                       ) {
        validateAdminAccess(roles);
        studentServiceInter.addStudent(student);
    }

    @PutMapping("/students/{id}")

    public void update(
            @PathVariable long id,
            @RequestBody StudentDtoRequest student,
            @RequestHeader("X-Authenticated-Roles") String roles
    ) {
        validateAdminAccess(roles);
        studentServiceInter.updateStudent(id, student);
    }

    @DeleteMapping("/students/{id}")

    public void delete(@PathVariable long id,
                       @RequestHeader("X-Authenticated-Roles") String roles
    ) {
        validateAdminAccess(roles);
        studentServiceInter.deleteStudent(id);
    }

    @RequestMapping("/test")
    public String test(@RequestHeader("X-Authenticated-Roles") String roles
    ) {
        validateAdminAccess(roles);
        return "Controller is working!";
    }


    //helper
    private void validateAdminAccess(String rolesHeader) {
        List<String> roles = Optional.ofNullable(rolesHeader)
                .map(header -> Arrays.asList(header.split(",")))
                .orElse(Collections.emptyList());

        if (!roles.contains("ADMIN")) {
            throw new InternalServerErrorException("no access");
        }
    }

    //helper
    private void validateUserAccess(String rolesHeader) {
        List<String> roles = Optional.ofNullable(rolesHeader)
                .map(header -> Arrays.asList(header.split(",")))
                .orElse(Collections.emptyList());

        if (!roles.contains("USER")) {
            throw new InternalServerErrorException("no access");
        }
    }



}
