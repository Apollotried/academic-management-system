package com.idld.gatewayservice.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValidationRequest {
    private String token;
}
