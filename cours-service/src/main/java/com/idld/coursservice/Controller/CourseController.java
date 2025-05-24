package com.idld.coursservice.Controller;

import com.idld.coursservice.DTO.CourseRequestDTO;
import com.idld.coursservice.DTO.CourseResponseDTO;
import com.idld.coursservice.Entity.Course;
import com.idld.coursservice.Entity.Syllabus;
import com.idld.coursservice.Service.CourseService;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{courseId}/details")

    public CourseResponseDTO getCourseDetails(@PathVariable long courseId) {
        return courseService.getCourseDetails(courseId);
    }

    @GetMapping("/count")

    public long getCoursesCount(){
        return courseService.getTotalCoursesCount();
    }


    @GetMapping

    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }



    @GetMapping("/{id}")

    public CourseResponseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public CourseResponseDTO createCourse(
            @RequestBody CourseRequestDTO courseRequestDTO,
            @RequestHeader("X-Authenticated-Roles") String roles
    ) {
        validateAdminAccess(roles);
        return courseService.createCourse(courseRequestDTO);
    }

    @PutMapping("/{id}")
    public void updateCourse(@PathVariable Long id, @RequestBody CourseRequestDTO courseRequestDTO, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        courseService.updateCourse(id, courseRequestDTO);
    }

    @DeleteMapping("/{id}")
    public CourseResponseDTO deleteCourse(@PathVariable Long id, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        return courseService.deleteCourse(id);
    }

    @PostMapping("/syllabus")
    public Syllabus createSyllabus(@RequestBody Syllabus syllabus, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        return courseService.createSyllabus(syllabus);
    }

    @PostMapping("/assignSyllabus/{courseId}/{syllabusId}")
    public ResponseEntity<Course> assignSyllabus(@PathVariable Long courseId, @PathVariable Long syllabusId, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        Course updatedCourse = courseService.assignSyllabus(courseId, syllabusId);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @GetMapping("/syllabus")

    public List<Syllabus> getAllSyllabus(){
        return courseService.getAllSyllabus();
    }

    @GetMapping("/syllabus/{syllabusId}")

    public Syllabus getSyllabusById(@PathVariable Long syllabusId) {
        return courseService.getSyllabusById(syllabusId);
    }


    @PutMapping("/syllabus/{syllabusId}")
    public Syllabus updateSyllabus(@PathVariable Long syllabusId, @RequestBody Syllabus syllabus, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        return courseService.updateSyllabus(syllabusId, syllabus);
    }

    @DeleteMapping("/syllabus/{syllabusId}")
    public void deleteSyllabus(@PathVariable Long syllabusId, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        courseService.deleteSyllabus(syllabusId);
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

}
