package com.idld.profservice;

import com.idld.profservice.model.Teacher;
import com.idld.profservice.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication

public class ProfServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProfServiceApplication.class, args);
    }

}
