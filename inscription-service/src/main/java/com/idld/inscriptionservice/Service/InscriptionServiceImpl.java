package com.idld.inscriptionservice.Service;

import com.idld.inscriptionservice.DTOs.*;
import com.idld.inscriptionservice.Entity.Inscription;
import com.idld.inscriptionservice.Mapper.InscriptionMapperInterface;
import com.idld.inscriptionservice.Model.Course;
import com.idld.inscriptionservice.Model.Student;
import com.idld.inscriptionservice.repository.InscriptionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscriptionServiceImpl implements InscriptionServiceInterface {

    private final InscriptionRepository inscriptionRepository;
    private final StudentFeignClient studentFeignClient;
    private final CourseFeignClient courseFeignClient;
    private final InscriptionMapperInterface inscriptionMapperInterface;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository,
                                  StudentFeignClient studentFeignClient,
                                  CourseFeignClient courseFeignClient,
                                  InscriptionMapperInterface inscriptionMapperInterface) {
        this.inscriptionRepository = inscriptionRepository;
        this.studentFeignClient = studentFeignClient;
        this.courseFeignClient = courseFeignClient;
        this.inscriptionMapperInterface=inscriptionMapperInterface;
    }

    @Override
    public ResponseInscriptionDTO inscrireEtudiant(RequestInscriptionDTO requestInscriptionDTO) {
        Student student = studentFeignClient.getStudentById(requestInscriptionDTO.getStudentId());
        if (student == null) {
            throw new RuntimeException("Student with ID " + requestInscriptionDTO.getStudentId() + " does not exist.");
        }
        courseDTO course = courseFeignClient.getCourseById(requestInscriptionDTO.getCourseId());
        if (course == null) {
            throw new RuntimeException("Course with ID " + requestInscriptionDTO.getCourseId() + " does not exist.");
        }
        Inscription inscription = inscriptionMapperInterface.toEntity(requestInscriptionDTO);
        inscription.setDateInscription(LocalDate.now().toString());
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapperInterface.toDto(inscription);
    }


    @Override
    public List<ResponseInscriptionDTO> getAllInscriptions() {
        List<ResponseInscriptionDTO> responseList = new ArrayList<>();
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        for (Inscription inscription : inscriptions) {
            ResponseInscriptionDTO responseDTO = new ResponseInscriptionDTO();
            BeanUtils.copyProperties(inscription, responseDTO);
            responseList.add(responseDTO);
        }

        return responseList;
    }

    @Override
    public void assignCoursesToStudent(AssignCoursesRequestDTO assignCoursesRequestDTO) {
        Long studentId = assignCoursesRequestDTO.getStudentId();
        List<Long> courseIds = assignCoursesRequestDTO.getCourseIds();

        // Remove existing courses for the student
        inscriptionRepository.deleteByStudentId(studentId);

        // Assign new courses
        for (Long courseId : courseIds) {
            Inscription inscription = new Inscription();
            inscription.setStudentId(studentId);
            inscription.setCourseId(courseId);
            inscription.setDateInscription(LocalDate.now().toString());
            inscriptionRepository.save(inscription);
        }
    }

    @Override
    public List<Long> findCourseIdsByStudentId(Long studentId){
        return inscriptionRepository.findCourseIdsByStudentId(studentId);
    }

    @Override
    public List<courseDTO> getCoursesForStudent(Long studentId) {
        // Fetch course IDs for the student
        List<Long> courseIds = inscriptionRepository.findCourseIdsByStudentId(studentId);

        // Fetch course details for each ID using Feign client
        return courseIds.stream()
                .map(courseFeignClient::getCourseById)
                .collect(Collectors.toList());
    }



    @Override
    public List<Student> findStudentsByCourseId(Long courseId) {
        List<Inscription> inscriptions = inscriptionRepository.findByCourseId(courseId);

        return inscriptions.stream()
                .map(inscription -> {
                    // Fetch student details using Feign client
                    return studentFeignClient.getStudentById(inscription.getStudentId());
                })
                .collect(Collectors.toList());
    }

}