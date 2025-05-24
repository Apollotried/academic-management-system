package com.idld.profservice.service;

import com.idld.profservice.dtos.TeacherDtoRequest;
import com.idld.profservice.dtos.TeacherDtoResponse;
import com.idld.profservice.model.Teacher;

import java.util.List;

public interface TeacherServiceInter {
    public List<TeacherDtoResponse> getAllTeachers();
    public TeacherDtoResponse getTeacherById(long id);
    public void addTeacher(TeacherDtoRequest teacherDtoRequest);
    public void updateTeacher(long id, TeacherDtoRequest teacherDtoRequest);
    public void deleteTeacher(long id);
    public long getTotalTeachersCount();
}
