package com.idld.coursservice.Service;


import com.idld.coursservice.DTO.CourseRequestDTO;
import com.idld.coursservice.DTO.CourseResponseDTO;
import com.idld.coursservice.Entity.Course;
import com.idld.coursservice.Entity.Syllabus;

import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> getAllCourses();
    CourseResponseDTO getCourseById(Long id);
    CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO);
    void updateCourse(Long id, CourseRequestDTO courseRequestDTO);
    CourseResponseDTO deleteCourse(Long id);
    public CourseResponseDTO getCourseDetails(long courseId);
    public long getTotalCoursesCount();

    public Course assignSyllabus(Long courseId, Long syllabusId);
    public Syllabus createSyllabus(Syllabus syllabus);
    public List<Syllabus> getAllSyllabus();
    public Syllabus getSyllabusById(Long syllabusId);
    public Syllabus updateSyllabus(Long syllabusId, Syllabus updatedSyllabus);

    public void deleteSyllabus(Long syllabusId);
}
