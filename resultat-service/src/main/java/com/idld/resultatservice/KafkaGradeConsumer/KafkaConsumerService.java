package com.idld.resultatservice.KafkaGradeConsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idld.resultatservice.Dtos.ResultDto;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableKafka
public class KafkaConsumerService {

    // Initialize the list to avoid NullPointerException
    private List<ResultDto> resultDtos = new ArrayList<>(); // Initialized here

    @KafkaListener(topics = "grades-topic", groupId = "gradeGroup")
    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);

        // Check if the message is in the expected format
        if (message.startsWith("New Result:")) {
            try {
                ResultDto resultDto = parseMessageFromText(message);
                if (resultDto != null) {
                    resultDtos.add(resultDto);
                }
            } catch (Exception e) {
                System.err.println("Error processing message: " + message);
                e.printStackTrace();
            }
        } else {
            System.err.println("Invalid message format: " + message);
        }
    }

    private ResultDto parseMessageFromText(String message) {
        // Print the raw message for debugging
        System.out.println("Raw message: " + message);

        // Remove the "New Result: " part
        message = message.replace("New Result: ", "").trim();

        // Now split the message by commas
        String[] parts = message.split(", ");

        // Check for correct number of parts
        if (parts.length != 3) {
            throw new IllegalArgumentException("Message format is incorrect: " + message);
        }

        // Extract each field from the parts array
        try {
            Long studentId = Long.parseLong(parts[0].split(":")[1].trim());
            Long courseId = Long.parseLong(parts[1].split(":")[1].trim());
            double grade = Double.parseDouble(parts[2].split(":")[1].trim());

            // Print the parsed values for debugging
            System.out.println("Parsed values: Student ID = " + studentId + ", Course ID = " + courseId + ", Grade = " + grade);

            // Create and return the ResultDto object
            ResultDto resultDto = new ResultDto();
            resultDto.setStudentId(studentId);
            resultDto.setCourseId(courseId);
            resultDto.setGrade(grade);
            return resultDto;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing message values: " + message, e);
        }
    }



    public List<ResultDto> getResultDtos() {
        return resultDtos;
    }


}
