package com.idld.coursservice.Mapper;

import com.idld.coursservice.DTO.CourseRequestDTO;
import com.idld.coursservice.DTO.CourseResponseDTO;
import com.idld.coursservice.Entity.Course;

public interface CourseMapperInter {
    Course toCourse(CourseRequestDTO courseRequestDTO);

    CourseResponseDTO toCourseDTO(Course course);

}
