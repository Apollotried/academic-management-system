package com.idld.notificationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestEmailDto {

    private String recipient;
    private String message;
    private String object;


}

