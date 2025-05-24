package com.idld.profservice.mapper;

import com.idld.profservice.dtos.TeacherDtoRequest;
import com.idld.profservice.dtos.TeacherDtoResponse;
import com.idld.profservice.model.Teacher;

public interface TeacherMapperInter {
    public Teacher ToTeacher(TeacherDtoRequest teacherDtoRequest);
    public TeacherDtoResponse ToTeacherDto(Teacher teacher);
}
