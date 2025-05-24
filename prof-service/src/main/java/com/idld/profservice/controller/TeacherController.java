package com.idld.profservice.controller;


import com.idld.profservice.service.TeacherServiceInter;

import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RestController
@RequestMapping("/api")
public class TeacherController {
    private final TeacherServiceInter teacherServiceInter;

    public TeacherController(TeacherServiceInter teacherServiceInter) {
        this.teacherServiceInter = teacherServiceInter;
    }



    @GetMapping("/teachers/count")
    public long getTeachersCount(@RequestHeader("X-Authenticated-Roles") String roles){

        validateAdminAccess(roles);
        return teacherServiceInter.getTotalTeachersCount();
    }

    //helper
    private void validateAdminAccess(String rolesHeader) {
        List<String> roles = Optional.ofNullable(rolesHeader)
                .map(header -> Arrays.asList(header.split(",")))
                .orElse(Collections.emptyList());

        if (!roles.contains("ADMIN")) {
            throw new InternalServerErrorException("no access");
        }
    }
}
