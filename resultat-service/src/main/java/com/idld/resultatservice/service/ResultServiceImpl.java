package com.idld.resultatservice.service;

import com.idld.resultatservice.Dtos.*;
import com.idld.resultatservice.Producer.KafkaProducerService;
import com.idld.resultatservice.controller.CourseClient;
import com.idld.resultatservice.controller.StudentClient;
import com.idld.resultatservice.entities.GradeNotificationEvent;
import com.idld.resultatservice.entities.Result;
import com.idld.resultatservice.exceptions.EntityNotFoundException;
import com.idld.resultatservice.mapper.ResultMapperInterf;
import com.idld.resultatservice.repository.ResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResultServiceImpl implements ResultServiceInterf{

    ResultRepository resultRepository;
    CourseClient courseClient;
    StudentClient studentClient;
    ResultMapperInterf resultMapperInterf;
    KafkaProducerService kafkaProducerService;

    public ResultServiceImpl(ResultRepository resultRepository, StudentClient studentClient, CourseClient courseClient, ResultMapperInterf resultMapperInterf, KafkaProducerService kafkaProducerService) {
        this.resultRepository = resultRepository;
        this.courseClient = courseClient;
        this.studentClient = studentClient;
        this.resultMapperInterf = resultMapperInterf;
        this.kafkaProducerService=kafkaProducerService;
    }



    @Override
    public Result createResult(ResultDTORequest resultDto) {
        // Fetch student and course details
        StudentDto student = studentClient.getStudentById(resultDto.getStudentId());
        CourseDto course = courseClient.getCourseById(resultDto.getCourseId());

        // Validate if the student and course exist
        if (student == null || course == null) {
            throw new EntityNotFoundException("Student or Course not found");
        }

        // Map DTO to Result entity
        Result result = resultMapperInterf.resultDtoToResult(resultDto);

        // Save the result in the database
        Result savedResult = resultRepository.save(result);

        // Publish the result to Kafka
        // String message = String.format("New Result: Student ID: %d, Course ID: %d, Grade: %.2f",
           //     savedResult.getStudentId(), savedResult.getCourseId(), savedResult.getGrade());
        //kafkaProducerService.sendMessage(message);
        String courseName = courseClient.getCourseById(savedResult.getCourseId()).getTitle();


        GradeNotificationEvent gradeEvent = new GradeNotificationEvent();
        gradeEvent.setUserId(savedResult.getStudentId());
        gradeEvent.setCourseId(savedResult.getCourseId());
        gradeEvent.setGrade(savedResult.getGrade());
        gradeEvent.setCourseName(courseName);

        kafkaProducerService.sendMessage(gradeEvent);

        return savedResult;
    }



    @Override
    public List<ResultDto> getResultByStudent(long studentId) {

        List<Result> results = resultRepository.findByStudentId(studentId);
        List<ResultDto> resultsDto = new ArrayList<>();
        for (Result result : results) {
            resultsDto.add(resultMapperInterf.resultToResultDto(result));
        }
        return resultsDto;

    }

    @Override
    public List<ResultDto> getResultByCourse(long courseId) {

        List<Result> results = resultRepository.findByCourseId(courseId);

        List<ResultDto> resultsDto = new ArrayList<>();
        for (Result result : results) {
            resultsDto.add(resultMapperInterf.resultToResultDto(result));
        }
        return resultsDto;
    }

    @Override
    public Result updateResult(long resultId, ResultDto resultDto) {
        // Fetch the result by its ID
        Optional<Result> resultOptional = resultRepository.findById(resultId);

        if (resultOptional.isPresent()) {
            Result result = resultOptional.get();
            result.setGrade(resultDto.getGrade()); // Update grade from DTO
            return resultRepository.save(result);  // Save the updated result
        } else {
            throw new IllegalArgumentException("Result not found");
        }
    }


    @Override
    public void deleteResult(long resultId) {
        resultRepository.deleteById(resultId);

    }

    @Override
    public StudentDto getStudentById(long studentId) {
        return studentClient.getStudentById(studentId);
    }
    @Override
    public CourseDto getCourseById(long courseId) {
        return courseClient.getCourseById(courseId);
    }


    @Override
    public List<ResultDto> getStudentsWithGradesByCourse(long courseId) {
        List<Result> results = resultRepository.findByCourseId(courseId);
        return results.stream().map(result ->{
            StudentDto student = studentClient.getStudentById(result.getStudentId());
            return new ResultDto(result.getStudentId(), result.getCourseId(), result.getGrade(),  student);
        }).collect(Collectors.toList());
    }


    @Override
    public List<ResultDetails> getResultDetailsByStudent(long studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        if (results.isEmpty()) {
            throw new RuntimeException("No results found for studentId: " + studentId);
        }


        return results.stream().map(result -> {
            StudentDto student = studentClient.getStudentById(studentId); // Fetch student details

            return new ResultDetails(result.getStudentId(), result.getCourseId(), result.getGrade(), null, student); // Include full course & student
        }).collect(Collectors.toList());
    }



    @Override
    public void applyBatchGrades(List<ResultDTORequest> results) {
        // Validate and process each result
        List<Result> resultEntities = results.stream().map(resultDto -> {
            // Validate student and course
            StudentDto student = studentClient.getStudentById(resultDto.getStudentId());
            CourseDto course = courseClient.getCourseById(resultDto.getCourseId());

            if (student == null || course == null) {
                throw new EntityNotFoundException("Student or Course not found for ID: "
                        + resultDto.getStudentId() + ", " + resultDto.getCourseId());
            }

            // Check if the result already exists in the database
            Result existingResult = resultRepository.findByStudentIdAndCourseId(resultDto.getStudentId(), resultDto.getCourseId());

            if (existingResult != null) {
                // If the result already exists, update the grade
                existingResult.setGrade(resultDto.getGrade());
                publishGradeNotification(existingResult, course.getTitle());
                return existingResult;
            } else {
                // If it does not exist, create a new result entity
                Result newResult = resultMapperInterf.resultDtoToResult(resultDto);
                publishGradeNotification(newResult, course.getTitle());
                return newResult;
            }
        }).collect(Collectors.toList());

        // Save all results in a batch
        resultRepository.saveAll(resultEntities);
    }


    @Override
    public void publishGradeNotification(Result result, String courseName) {
        try {
            GradeNotificationEvent gradeEvent = new GradeNotificationEvent();
            gradeEvent.setUserId(result.getStudentId());
            gradeEvent.setCourseId(result.getCourseId());
            gradeEvent.setGrade(result.getGrade());
            gradeEvent.setCourseName(courseName);

            kafkaProducerService.sendMessage(gradeEvent);

            log.info("Successfully published grade notification for studentId: {}, course: {}", result.getStudentId(), courseName);
        } catch (Exception e) {
            log.error("Error while publishing grade notification", e);
        }
    }







}