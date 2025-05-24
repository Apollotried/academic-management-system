package com.idld.resultatservice;

import com.idld.resultatservice.entities.Result;
import com.idld.resultatservice.repository.ResultRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableFeignClients
@SpringBootApplication
public class ResultatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResultatServiceApplication.class, args);
    }


}
