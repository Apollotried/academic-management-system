package com.idld.gatewayservice.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TokenValidationResponse {
    private boolean isValid;
    private Integer userId;
    private List<String> authorities;
    private String error;
}
