package com.idld.coursservice;

import com.idld.coursservice.Entity.Course;
import com.idld.coursservice.Repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableFeignClients
public class CoursServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursServiceApplication.class, args);
    }
}
