package com.idld.profservice.service;

import com.idld.profservice.dtos.TeacherDtoRequest;
import com.idld.profservice.dtos.TeacherDtoResponse;
import com.idld.profservice.model.Teacher;
import com.idld.profservice.exception.EntityNotFoundException;
import com.idld.profservice.mapper.TeacherMapperInter;
import com.idld.profservice.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherServiceInter {
    private final TeacherRepository teacherRepository;
    private final TeacherMapperInter teacherMapperInter;

    public TeacherServiceImpl(TeacherRepository teacherRepository, TeacherMapperInter teacherMapperInter) {
        this.teacherRepository = teacherRepository;
        this.teacherMapperInter = teacherMapperInter;
    }

    @Override
    public List<TeacherDtoResponse> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(teacherMapperInter::ToTeacherDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDtoResponse getTeacherById(long id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            throw new EntityNotFoundException("Teacher not found with ID: " + id);
        }
        return teacherMapperInter.ToTeacherDto(teacher);
    }

    @Override
    public void addTeacher(TeacherDtoRequest teacherDtoRequest) {
        Teacher teacher = teacherMapperInter.ToTeacher(teacherDtoRequest);
        teacherRepository.save(teacher);
    }

    @Override
    public void updateTeacher(long id, TeacherDtoRequest teacherDtoRequest) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Teacher not found with ID: " + id));
        teacher.setFirstName(teacherDtoRequest.getFirstName());
        teacher.setLastName(teacherDtoRequest.getLastName());
        teacher.setEmail(teacherDtoRequest.getEmail());
        teacher.setPhone(teacherDtoRequest.getPhone());
        teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(long id) {
        if (!teacherRepository.existsById(id)) {
            throw new EntityNotFoundException("Teacher not found with ID: " + id);
        }
        teacherRepository.deleteById(id);
    }

    @Override
    public long getTotalTeachersCount(){
        return teacherRepository.count();
    }
}
