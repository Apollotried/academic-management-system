package com.idld.resultatservice.controller;


import org.springframework.cloud.openfeign.FeignClient;
import com.idld.resultatservice.Dtos.CourseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient(name = "cours-service", url = "http://cours-dock:8081")
public interface CourseClient {
    @GetMapping("/api/courses/{id}")
    CourseDto getCourseById(@PathVariable("id") long courseId);

    @GetMapping("/api/courses")
    List<CourseDto> getAllCourses();
}
