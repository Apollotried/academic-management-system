package com.marouane.authservice.auth;

import com.marouane.authservice.role.RoleRepository;
import com.marouane.authservice.security.JwtService;
import com.marouane.authservice.security.TokenValidationRequest;
import com.marouane.authservice.security.TokenValidationResponse;
import com.marouane.authservice.user.User;
import com.marouane.authservice.user.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {



    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        var claims = new HashMap<String, Object>();
        var user = (User)auth.getPrincipal();

        claims.put("fullname", user.getFirstName() + " " + user.getLastName());
        claims.put("scope", scope);
        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void register(@Valid RegistrationRequest request) {
        var userRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("User role not found"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);

    }

    public TokenValidationResponse validateToken(TokenValidationRequest jwt){

        try {
            // 1. Validate token (throws exception if invalid)
            Claims claims = jwtService.extractAllClaims(jwt.getToken());

            // 2. Extract data from token
            String username = claims.getSubject();
            Integer id = userRepository.findByEmail(username).get().getId();
            List<String> authorities = claims.get("authorities", List.class);

            return TokenValidationResponse.builder()
                    .isValid(true)
                    .userId(id)
                    .authorities(authorities)
                    .build();
        } catch (Exception e) {
            return TokenValidationResponse.builder()
                    .isValid(false)
                    .error("Invalid token: " + e.getMessage())
                    .build();
        }


      }

    public void registerUser(@Valid RegistrationRequest request) {

        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("User role not found"));
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
    }
}
