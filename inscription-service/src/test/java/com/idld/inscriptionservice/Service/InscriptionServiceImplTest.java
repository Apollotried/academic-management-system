package com.idld.inscriptionservice.Service;

import static org.junit.jupiter.api.Assertions.*;
import com.idld.inscriptionservice.DTOs.*;
import com.idld.inscriptionservice.Entity.Inscription;
import com.idld.inscriptionservice.Mapper.InscriptionMapperInterface;
import com.idld.inscriptionservice.repository.InscriptionRepository;
import com.idld.inscriptionservice.Model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class InscriptionServiceImplTest {

    @Mock
    private InscriptionRepository inscriptionRepository;

    @Mock
    private StudentFeignClient studentFeignClient;

    @Mock
    private CourseFeignClient courseFeignClient;

    @Mock
    private InscriptionMapperInterface inscriptionMapperInterface;

    @InjectMocks
    private InscriptionServiceImpl inscriptionServiceImpl;

    @Test
    public void testInscrireEtudiant_ShouldSaveMappedEntity() {
        RequestInscriptionDTO requestDto = new RequestInscriptionDTO();
        requestDto.setStudentId(1L);
        requestDto.setCourseId(1L);

        Student student = new Student();
        student.setId(1L);
        courseDTO course = new courseDTO();
        course.setId(1L);
        Inscription inscription = new Inscription();
        inscription.setDateInscription(LocalDate.now().toString());

        when(studentFeignClient.getStudentById(requestDto.getStudentId())).thenReturn(student);
        when(courseFeignClient.getCourseById(requestDto.getCourseId())).thenReturn(course);
        when(inscriptionMapperInterface.toEntity(requestDto)).thenReturn(inscription);
        when(inscriptionRepository.save(inscription)).thenReturn(inscription);
        when(inscriptionMapperInterface.toDto(inscription)).thenReturn(new ResponseInscriptionDTO());

        ResponseInscriptionDTO responseDto = inscriptionServiceImpl.inscrireEtudiant(requestDto);

        assertNotNull(responseDto);
        verify(studentFeignClient, times(1)).getStudentById(requestDto.getStudentId());
        verify(courseFeignClient, times(1)).getCourseById(requestDto.getCourseId());
        verify(inscriptionRepository, times(1)).save(inscription);
    }


    @Test
    public void testAssignCoursesToStudent_ShouldAssignCourses() {
        AssignCoursesRequestDTO requestDto = new AssignCoursesRequestDTO();
        requestDto.setStudentId(1L);
        List<Long> courseIds = List.of(1L, 2L);
        requestDto.setCourseIds(courseIds);

        inscriptionServiceImpl.assignCoursesToStudent(requestDto);

        verify(inscriptionRepository, times(1)).deleteByStudentId(requestDto.getStudentId());
        verify(inscriptionRepository, times(courseIds.size())).save(any(Inscription.class));
    }

    @Test
    public void testFindCourseIdsByStudentId_ShouldReturnCourseIds() {
        Long studentId = 1L;
        List<Long> courseIds = List.of(1L, 2L);

        when(inscriptionRepository.findCourseIdsByStudentId(studentId)).thenReturn(courseIds);

        List<Long> result = inscriptionServiceImpl.findCourseIdsByStudentId(studentId);

        assertEquals(courseIds.size(), result.size());
        verify(inscriptionRepository, times(1)).findCourseIdsByStudentId(studentId);
    }

    @Test
    public void testGetCoursesForStudent_ShouldReturnCourses() {
        Long studentId = 1L;
        List<Long> courseIds = List.of(1L, 2L);
        List<courseDTO> courses = List.of(new courseDTO(), new courseDTO());

        when(inscriptionRepository.findCourseIdsByStudentId(studentId)).thenReturn(courseIds);
        when(courseFeignClient.getCourseById(anyLong())).thenReturn(new courseDTO());

        List<courseDTO> result = inscriptionServiceImpl.getCoursesForStudent(studentId);

        assertEquals(courseIds.size(), result.size());
        verify(inscriptionRepository, times(1)).findCourseIdsByStudentId(studentId);
        verify(courseFeignClient, times(courseIds.size())).getCourseById(anyLong());
    }
}