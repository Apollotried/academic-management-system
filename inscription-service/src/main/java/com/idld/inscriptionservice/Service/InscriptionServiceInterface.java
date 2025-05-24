package com.idld.inscriptionservice.Service;

import com.idld.inscriptionservice.DTOs.*;
import com.idld.inscriptionservice.Model.Course;
import com.idld.inscriptionservice.Model.Student;

import java.util.List;

public interface InscriptionServiceInterface {
    ResponseInscriptionDTO inscrireEtudiant(RequestInscriptionDTO requestInscriptionDTO);
    List<ResponseInscriptionDTO> getAllInscriptions();

    void assignCoursesToStudent(AssignCoursesRequestDTO assignCoursesRequestDTO);

    public List<courseDTO> getCoursesForStudent(Long studentId);
    public List<Long> findCourseIdsByStudentId(Long studentId);

    public List<Student> findStudentsByCourseId(Long courseId);
}