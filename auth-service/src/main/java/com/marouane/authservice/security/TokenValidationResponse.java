package com.marouane.authservice.security;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TokenValidationResponse {
    private boolean isValid;
    private Integer userId;
    private List<String> authorities;
    private String error;
}
