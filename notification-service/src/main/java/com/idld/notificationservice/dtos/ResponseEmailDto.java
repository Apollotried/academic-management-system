package com.idld.notificationservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEmailDto {

    private Long id;
    private String recipient;
    private String message;
    private String object;
    private String status;
    private LocalDateTime timestamp;

}

