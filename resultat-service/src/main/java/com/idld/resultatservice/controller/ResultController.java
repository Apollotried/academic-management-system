package com.idld.resultatservice.controller;

import com.idld.resultatservice.Dtos.*;
import com.idld.resultatservice.KafkaGradeConsumer.KafkaConsumerService;
import com.idld.resultatservice.entities.Result;
import com.idld.resultatservice.service.ResultServiceInterf;

import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api/results")
public class ResultController {

        private final KafkaConsumerService kafkaConsumerService;

        private final ResultServiceInterf resultService;

        // Constructor Injection
        public ResultController(ResultServiceInterf resultService,KafkaConsumerService  kafkaConsumerService) {
            this.resultService = resultService;
            this. kafkaConsumerService =  kafkaConsumerService;
        }


    //testing the communication
    @GetMapping("/student-info/{studentId}")
    public ResponseEntity<StudentDto> getStudentInfo(@PathVariable long studentId) {
        StudentDto student = resultService.getStudentById(studentId);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/course-info/{courseId}")
    public ResponseEntity<CourseDto> getCoursetInfo(@PathVariable long courseId) {
        CourseDto course = resultService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }


    // Create a new result
    @PostMapping
    public ResponseEntity<Result> createResult(@RequestBody ResultDTORequest resultDto,@RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        Result result = resultService.createResult(resultDto);
        return ResponseEntity.ok(result); // Respond with HTTP 200 and the created result
    }

    // Get results by student ID

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ResultDto>> getResultsByStudent(@PathVariable long studentId) {
        List<ResultDto> results = resultService.getResultByStudent(studentId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/resultsForSpecificStudent")
    public ResponseEntity<List<ResultDto>> getResultsForSpecificStudent(
            @RequestHeader( "X-Authenticated-UserId") String userIdHeader,
            @RequestHeader("X-Authenticated-Roles") String roles
    ) {
        validateUserAccess(roles);
        try {
            long studentId = Long.parseLong(userIdHeader);
            List<ResultDto> results = resultService.getResultByStudent(studentId);
            return ResponseEntity.ok(results);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID format");
        }

    }

    @GetMapping("/studentKafka/{studentId}")
    public ResponseEntity<List<ResultDto>> getResultsByStudentKafka(@PathVariable long studentId) {
        // Get all results
        List<ResultDto> allResults = kafkaConsumerService.getResultDtos();
        List<ResultDto> filteredResults = new ArrayList<>();

        // Using for loop to filter the results based on studentId
        for (ResultDto result : allResults) {
            if (result.getStudentId() == studentId) {
                filteredResults.add(result);
            }
        }

        // Return the filtered results wrapped in ResponseEntity
        if (filteredResults.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no results found
        } else {
            return ResponseEntity.ok(filteredResults); // Return 200 with the list of results
        }
    }




    // Get results by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ResultDto>> getResultsByCourse(@PathVariable long courseId) {
        List<ResultDto> results = resultService.getResultByCourse(courseId);
        return ResponseEntity.ok(results);
    }

    //student infos and grade by courseId
    @GetMapping("/course/{courseId}/students-grades")
    public List<ResultDto> getStudentsWithGradesByCourse(@PathVariable long courseId){
        return resultService.getStudentsWithGradesByCourse(courseId);
    }


    @GetMapping("/details/{studentId}")
    public List<ResultDetails> getResultsDetailsByStudentId(@PathVariable long studentId) {
            return resultService.getResultDetailsByStudent(studentId);
    }




    // Update a result
    @PutMapping("/{resultId}")
    public ResponseEntity<Result> updateResult(@PathVariable long resultId, @RequestBody ResultDto resultDto, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        Result updatedResult = resultService.updateResult(resultId, resultDto);
        return ResponseEntity.ok(updatedResult);
    }

    // Delete a result
    @DeleteMapping("/{resultId}")
    public ResponseEntity<Void> deleteResult(@PathVariable long resultId, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        resultService.deleteResult(resultId);
        return ResponseEntity.noContent().build(); // Respond with HTTP 204 (No Content)
    }


    @PostMapping("/batch")
    public ResponseEntity<String> applyBatchGrades(@RequestBody List<ResultDTORequest> results, @RequestHeader("X-Authenticated-Roles") String roles) {
        validateAdminAccess(roles);
        try {
            resultService.applyBatchGrades(results);
            return ResponseEntity.ok("Batch grades applied successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error applying batch grades: " + e.getMessage());
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
