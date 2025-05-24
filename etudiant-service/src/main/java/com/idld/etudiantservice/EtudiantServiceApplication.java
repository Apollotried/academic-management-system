package com.idld.etudiantservice;

import com.idld.etudiantservice.model.Student;
import com.idld.etudiantservice.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class EtudiantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtudiantServiceApplication.class, args);
    }

}
