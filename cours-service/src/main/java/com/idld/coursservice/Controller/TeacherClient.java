package com.idld.coursservice.Controller;

import com.idld.coursservice.DTO.TeacherDtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "prof-service", url = "http://localhost:8083")
public interface TeacherClient {

    @GetMapping("/api/teachers/{id}")
    TeacherDtoResponse getTeacherById(@PathVariable("id") long id);


}
