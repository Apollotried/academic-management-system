package com.idld.resultatservice.controller;

import com.idld.resultatservice.Dtos.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "etudiant-service", url = "http://etudiant-dock:8080")
@Component
public interface StudentClient {
    @GetMapping("/api/students/{id}")
    StudentDto getStudentById(@PathVariable("id") long studentId);

    @GetMapping("/api/students")
    List<StudentDto> getStudents();
}
