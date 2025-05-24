package com.marouane.authservice.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValidationRequest {
    String token;
}
