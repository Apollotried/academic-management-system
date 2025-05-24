package com.idld.inscriptionservice.Service;

import com.idld.inscriptionservice.Model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient(name = "etudiant-service", url = "http://etudiant-dock:8080")
public interface StudentFeignClient {

    @GetMapping("/api/students/{id}")
    Student getStudentById(@PathVariable("id") Long id);
}