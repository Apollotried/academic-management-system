package com.idld.coursservice.Service;

import com.idld.coursservice.Service.KafkaProducerService;
import static org.junit.jupiter.api.Assertions.*;
import com.idld.coursservice.DTO.CourseRequestDTO;
import com.idld.coursservice.DTO.CourseResponseDTO;
import com.idld.coursservice.Entity.Course;
import com.idld.coursservice.Entity.Syllabus;
import com.idld.coursservice.Mapper.CourseMapperInter;
import com.idld.coursservice.Repository.CourseRepository;
import com.idld.coursservice.Repository.SyllabusRepository;
import com.idld.coursservice.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SyllabusRepository syllabusRepository;

    @Mock
    private CourseMapperInter courseMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @Test
    public void testGetAllCourses_ShouldReturnMappedResponses() {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setTitle("Course 1");
        courses.add(course);

        List<CourseResponseDTO> expectedResponses = new ArrayList<>();
        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setTitle("Course 1");
        expectedResponses.add(responseDTO);

        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.toCourseDTO(course)).thenReturn(responseDTO);

        List<CourseResponseDTO> actualResponses = courseServiceImpl.getAllCourses();

        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses.get(0).getTitle(), actualResponses.get(0).getTitle());
        verify(courseRepository, times(1)).findAll();
        verify(courseMapper, times(1)).toCourseDTO(course);
    }

    @Test
    public void testGetCourseById_ShouldReturnMappedResponse() {
        long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Course 1");

        CourseResponseDTO expectedResponse = new CourseResponseDTO();
        expectedResponse.setTitle("Course 1");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toCourseDTO(course)).thenReturn(expectedResponse);

        CourseResponseDTO actualResponse = courseServiceImpl.getCourseById(courseId);

        assertNotNull(actualResponse);
        assertEquals("Course 1", actualResponse.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseMapper, times(1)).toCourseDTO(course);
    }

    @Test
    public void testCreateCourse_ShouldSaveCourseAndSendMessage() {
        CourseRequestDTO courseRequestDTO = new CourseRequestDTO();
        courseRequestDTO.setTitle("New Course");

        Course course = new Course();
        course.setTitle("New Course");

        CourseResponseDTO expectedResponse = new CourseResponseDTO();
        expectedResponse.setTitle("New Course");

        when(courseMapper.toCourse(courseRequestDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toCourseDTO(course)).thenReturn(expectedResponse);

        CourseResponseDTO actualResponse = courseServiceImpl.createCourse(courseRequestDTO);

        assertNotNull(actualResponse);
        assertEquals("New Course", actualResponse.getTitle());
        verify(courseRepository, times(1)).save(course);
        verify(kafkaProducerService, times(1)).sendMessage(anyString(), anyString());
    }

    @Test
    public void testUpdateCourse_ShouldUpdateCourseDetails() {
        long courseId = 1L;
        CourseRequestDTO courseRequestDTO = new CourseRequestDTO();
        courseRequestDTO.setTitle("Updated Course");

        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setTitle("Old Course");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));

        courseServiceImpl.updateCourse(courseId, courseRequestDTO);

        assertEquals("Updated Course", existingCourse.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    public void testDeleteCourse_ShouldDeleteCourseAndReturnDTO() {
        long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setTitle("Course to Delete");

        CourseResponseDTO expectedResponse = new CourseResponseDTO();
        expectedResponse.setTitle("Course to Delete");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toCourseDTO(course)).thenReturn(expectedResponse);

        CourseResponseDTO actualResponse = courseServiceImpl.deleteCourse(courseId);

        assertNotNull(actualResponse);
        assertEquals("Course to Delete", actualResponse.getTitle());
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    public void testAssignSyllabus_ShouldAssignSyllabusToCourse() {
        long courseId = 1L;
        long syllabusId = 2L;

        Course course = new Course();
        course.setId(courseId);
        Syllabus syllabus = new Syllabus();
        syllabus.setSyllabus_id(syllabusId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.of(syllabus));

        Course updatedCourse = new Course();
        updatedCourse.setSyllabus(syllabus);
        when(courseRepository.save(course)).thenReturn(updatedCourse);

        Course result = courseServiceImpl.assignSyllabus(courseId, syllabusId);

        assertNotNull(result.getSyllabus());
        assertEquals(syllabusId, result.getSyllabus().getSyllabus_id());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testAssignSyllabus_ShouldThrowException_WhenCourseNotFound() {
        long courseId = 1L;
        long syllabusId = 2L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseServiceImpl.assignSyllabus(courseId, syllabusId));
        verify(courseRepository, times(1)).findById(courseId);
    }

    @Test
    public void testCreateSyllabus_ShouldSaveSyllabus() {
        Syllabus syllabus = new Syllabus();
        syllabus.setName("New Syllabus");

        when(syllabusRepository.save(syllabus)).thenReturn(syllabus);

        Syllabus actualSyllabus = courseServiceImpl.createSyllabus(syllabus);

        assertNotNull(actualSyllabus);
        assertEquals("New Syllabus", actualSyllabus.getName());
        verify(syllabusRepository, times(1)).save(syllabus);
    }

    @Test
    public void testDeleteSyllabus_ShouldDeleteSyllabus() {
        long syllabusId = 1L;
        Syllabus syllabus = new Syllabus();
        syllabus.setSyllabus_id(syllabusId);

        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.of(syllabus));

        courseServiceImpl.deleteSyllabus(syllabusId);

        verify(syllabusRepository, times(1)).delete(syllabus);
    }

    @Test
    public void testDeleteSyllabus_ShouldThrowException_WhenSyllabusNotFound() {
        long syllabusId = 1L;

        when(syllabusRepository.findById(syllabusId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseServiceImpl.deleteSyllabus(syllabusId));
        verify(syllabusRepository, times(1)).findById(syllabusId);
    }



}