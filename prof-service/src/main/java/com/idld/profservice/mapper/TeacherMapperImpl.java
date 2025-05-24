package com.idld.profservice.mapper;

import com.idld.profservice.dtos.TeacherDtoRequest;
import com.idld.profservice.dtos.TeacherDtoResponse;
import com.idld.profservice.model.Teacher;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapperImpl implements TeacherMapperInter{

    @Override
    public Teacher ToTeacher(TeacherDtoRequest teacherDtoRequest) {
        if (teacherDtoRequest == null) {
            return null;
        }

        Teacher t = new Teacher();
        BeanUtils.copyProperties(teacherDtoRequest, t);
        return t;
    }

    @Override
    public TeacherDtoResponse ToTeacherDto(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        TeacherDtoResponse dto = new TeacherDtoResponse();
        BeanUtils.copyProperties(teacher, dto);
        return dto;
    }

}

