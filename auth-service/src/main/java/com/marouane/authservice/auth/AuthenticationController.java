package com.marouane.authservice.auth;

import com.marouane.authservice.security.TokenValidationRequest;
import com.marouane.authservice.security.TokenValidationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request) {
     return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ){
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid RegistrationRequest request
    ){
        authenticationService.registerUser(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/validate")
    public TokenValidationResponse validateToken(
            @RequestBody TokenValidationRequest token
    ){
        return authenticationService.validateToken(token);
    }

}
