package com.idld.coursservice.Mapper;

import com.idld.coursservice.DTO.CourseRequestDTO;
import com.idld.coursservice.DTO.CourseResponseDTO;
import com.idld.coursservice.Entity.Course;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CourseMapperImpl implements CourseMapperInter {

    @Override
    public Course toCourse(CourseRequestDTO courseRequestDTO) {
        if (courseRequestDTO == null) {
            return null;
        }

        Course course = new Course();
        BeanUtils.copyProperties(courseRequestDTO, course);
        return course;
    }

    @Override
    public CourseResponseDTO toCourseDTO(Course course) {
        if (course == null) {
            return null;
        }

        CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
        BeanUtils.copyProperties(course, courseResponseDTO);

        // Manually set the syllabusId
        if (course.getSyllabus() != null) {
            courseResponseDTO.setSyllabusId(course.getSyllabus().getSyllabus_id());
        }else{
            courseResponseDTO.setSyllabusId(null);
        }

        return courseResponseDTO;
    }
}




