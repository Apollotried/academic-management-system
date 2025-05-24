package com.idld.coursservice.Service;

import com.idld.coursservice.Controller.TeacherClient;
import com.idld.coursservice.DTO.CourseRequestDTO;
import com.idld.coursservice.DTO.CourseResponseDTO;
import com.idld.coursservice.DTO.TeacherDtoResponse;
import com.idld.coursservice.Entity.Course;
import com.idld.coursservice.Entity.Syllabus;
import com.idld.coursservice.Mapper.CourseMapperInter;
import com.idld.coursservice.Repository.CourseRepository;
import com.idld.coursservice.Repository.SyllabusRepository;
import com.idld.coursservice.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapperInter courseMapper;
    private final TeacherClient teacherClient;
    private final SyllabusRepository syllabusRepository;
    private final KafkaProducerService kafkaProducerService;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapperInter courseMapper, TeacherClient teacherClient, SyllabusRepository syllabusRepository, KafkaProducerService kafkaProducerService) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.teacherClient = teacherClient;
        this.syllabusRepository = syllabusRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponseDTO> courseResponseDTOs = new ArrayList<>();

        for (Course course : courses) {
            courseResponseDTOs.add(courseMapper.toCourseDTO(course));
        }

        return courseResponseDTOs;
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).get();
        return courseMapper.toCourseDTO(course);
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO) {
        Course course = courseMapper.toCourse(courseRequestDTO);
        Course savedCourse = courseRepository.save(course);

        String message = "Course " + savedCourse.getTitle() + " (ID: " + savedCourse.getId() + ") has been created.";
        kafkaProducerService.sendMessage("course-notifications", message);

        return courseMapper.toCourseDTO(savedCourse);
    }

    @Override
    public void updateCourse(Long id, CourseRequestDTO courseRequestDTO) {
        // Retrieve the existing course from the database
        Course existingCourse = courseRepository.findById(id).get();

        // Manually set the values from the DTO to the existing entity
        existingCourse.setTitle(courseRequestDTO.getTitle());
        existingCourse.setDescription(courseRequestDTO.getDescription());
        existingCourse.setCredit(courseRequestDTO.getCredit());
        existingCourse.setTeacherId(courseRequestDTO.getTeacherId());

        courseRepository.save(existingCourse);

    }


    @Override
    public CourseResponseDTO deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));

        // Convert to DTO before modifying the entity
        CourseResponseDTO courseResponseDTO = courseMapper.toCourseDTO(course);

        if (course.getSyllabus() != null) {
            Syllabus syllabus = course.getSyllabus();
            course.setSyllabus(null);
            courseRepository.save(course);
            syllabusRepository.delete(syllabus);
        }

        courseRepository.delete(course);
        return courseResponseDTO;
    }


    @Override
    public CourseResponseDTO getCourseDetails(long courseId) {
        // Fetch course details from your repository or service
        CourseResponseDTO course = getCourseById(courseId);

        // Use Feign client to get teacher details
        //TeacherDtoResponse teacher = teacherClient.getTeacherById(course.getTeacherId());


        return course;
    }

    @Override
    public long getTotalCoursesCount(){
        return courseRepository.count();
    }

    @Override
    public Course assignSyllabus(Long courseId, Long syllabusId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<Syllabus> optionalSyllabus = syllabusRepository.findById(syllabusId);


        if (optionalCourse.isPresent() && optionalSyllabus.isPresent()) {
            Course course = optionalCourse.get();
            Syllabus syllabus = optionalSyllabus.get();

            // Check for null to be extra cautious
            if (course != null && syllabus != null) {
                course.setSyllabus(syllabus);
                syllabus.setAssigned(true);
                return courseRepository.save(course);
            } else {
                throw new ResourceNotFoundException("Course or Syllabus not found");
            }
        } else {
            throw new ResourceNotFoundException("Course or Syllabus not found");
        }
    }


    @Override
    public Syllabus createSyllabus(Syllabus syllabus) {
        syllabus.setAssigned(false);
        return syllabusRepository.save(syllabus);
    }

    @Override
    public List<Syllabus> getAllSyllabus(){
        return syllabusRepository.findAll();
    }

    @Override
    public Syllabus getSyllabusById(Long syllabusId) {
        return syllabusRepository.findById(syllabusId) .orElseThrow(
                () -> new EntityNotFoundException("Syllabus not found with id: " + syllabusId));
    }


    @Override
    public Syllabus updateSyllabus(Long syllabusId, Syllabus updatedSyllabus) {
        Syllabus existingSyllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new EntityNotFoundException("Syllabus not found with id: " + syllabusId));

        existingSyllabus.setName(updatedSyllabus.getName());
        existingSyllabus.setWeeklyTopics(updatedSyllabus.getWeeklyTopics());
        existingSyllabus.setReadings(updatedSyllabus.getReadings());
        existingSyllabus.setAssignments(updatedSyllabus.getAssignments());
        existingSyllabus.setExams(updatedSyllabus.getExams());
        existingSyllabus.setGradingPolicy(updatedSyllabus.getGradingPolicy());

        return syllabusRepository.save(existingSyllabus);
    }


    @Override
    public void deleteSyllabus(Long syllabusId) {
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new EntityNotFoundException("Syllabus not found with id: " + syllabusId));

        syllabusRepository.delete(syllabus);
    }

}
