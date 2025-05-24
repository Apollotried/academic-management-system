package com.idld.inscriptionservice.Controller;


import com.idld.inscriptionservice.DTOs.RequestInscriptionDTO;
import com.idld.inscriptionservice.DTOs.ResponseInscriptionDTO;
import com.idld.inscriptionservice.DTOs.courseDTO;
import com.idld.inscriptionservice.Model.Student;
import com.idld.inscriptionservice.Service.InscriptionServiceInterface;
import com.idld.inscriptionservice.Service.CourseFeignClient;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.idld.inscriptionservice.DTOs.AssignCoursesRequestDTO;
import org.springframework.web.server.ResponseStatusException;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api/inscriptions")
public class InscriptionController{
    private final InscriptionServiceInterface inscriptionService;
    private final CourseFeignClient courseFeignClient;

    public InscriptionController(InscriptionServiceInterface inscriptionService, CourseFeignClient courseFeignClient) {
        this.inscriptionService = inscriptionService;
        this.courseFeignClient = courseFeignClient;
    }


    @PostMapping
    public ResponseInscriptionDTO inscrireEtudiant(@RequestBody RequestInscriptionDTO requestInscriptionDTO,@RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        return inscriptionService.inscrireEtudiant(requestInscriptionDTO);
    }


    @GetMapping
    public List<ResponseInscriptionDTO> getAllInscriptions() {
        return inscriptionService.getAllInscriptions();
    }



    @PostMapping("/assign-courses")
    public ResponseEntity<?> assignCoursesToStudent(
            @RequestBody AssignCoursesRequestDTO assignCoursesRequestDTO,
            @RequestHeader("X-Authenticated-Roles") String roles)
    {
        validateAdminAccess(roles);
        try {
            inscriptionService.assignCoursesToStudent(assignCoursesRequestDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @GetMapping("/CoursesByStudentId/{studentId}")
    public List<courseDTO> getCoursesForStudent(@PathVariable Long studentId) {
        // Fetch course IDs for the student
        List<Long> courseIds = inscriptionService.findCourseIdsByStudentId(studentId);

        // Fetch course details for each ID using Feign client
        return courseIds.stream()
                .map(courseFeignClient::getCourseById)
                .collect(Collectors.toList());
    }

    @GetMapping("/CoursesBySpecificStudent")
    public List<courseDTO> getCoursesBySpecificStudent(
            @RequestHeader( "X-Authenticated-UserId") String userIdHeader,
            @RequestHeader("X-Authenticated-Roles") String roles
    ) {
        validateUserAccess(roles);

        try {
            long studentId = Long.parseLong(userIdHeader);
            List<Long> courseIds = inscriptionService.findCourseIdsByStudentId(studentId);
            return courseIds.stream()
                    .map(courseFeignClient::getCourseById)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID format");
        }

    }



    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<List<Student>> getStudentsByCourseId(@PathVariable Long courseId) {
        try {
            List<Student> students = inscriptionService.findStudentsByCourseId(courseId);
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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












