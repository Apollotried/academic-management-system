package com.idld.inscriptionservice.Service;

import com.idld.inscriptionservice.DTOs.courseDTO;
import com.idld.inscriptionservice.Model.Course;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cours-service", url = "http://cours-dock:8081")
@Component
public interface CourseFeignClient {

    @GetMapping("/api/courses/{id}")
    courseDTO getCourseById(@PathVariable("id") Long id);
}