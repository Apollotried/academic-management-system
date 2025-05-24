package com.idld.gatewayservice.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    ServerHttpRequest mutatedRequest = null;

    private final JwtService jwtService;
    private final WebClient.Builder webClientBuilder;
    private static final String AUTH_SERVICE_URL = "lb://AUTH-SERVICE";


    public AuthenticationFilter(JwtService jwtService, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.jwtService = jwtService;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {


        return (exchange, chain) -> {
            if (!config.enabled) {
                return chain.filter(exchange);
            }

            log.info("Authenticating request to {}", exchange.getRequest().getPath());


            if (exchange.getRequest().getMethod().name().equalsIgnoreCase("OPTIONS")) {
                log.info("Skipping authentication for preflight request");
                return chain.filter(exchange);
            }


            // Skip authentication for whitelisted paths
            if (shouldSkipAuth(exchange.getRequest().getPath().toString())) {
                return chain.filter(exchange);
            }

            // Validate Authorization header
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or malformed Authorization header");
                return unauthorizedResponse(exchange.getResponse());
            }

            // Extract and validate token
            String token = authHeader.substring(7);
            return webClientBuilder.build()
                    .post()
                    .uri(AUTH_SERVICE_URL + "/auth/validate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new TokenValidationRequest(token))  // Clean JSON syntax
                    .retrieve()
                    .bodyToMono(TokenValidationResponse.class)
                    .flatMap(response -> {
                        if (response.isValid()) {
                            mutatedRequest = exchange.getRequest().mutate()
                                    .header("X-Authenticated-User", jwtService.extractUsername(token))
                                    .header("X-Authenticated-Roles", String.join(",", jwtService.extractRoles(token)))
                                    .header("X-Authenticated-UserId", response.getUserId().toString())
                                    .build();

                            ServerWebExchange mutatedExchange = exchange.mutate()
                                    .request(mutatedRequest)
                                    .build();

                            return chain.filter(mutatedExchange);
                        } else {
                            return unauthorizedResponse(exchange.getResponse());
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("Token validation failed: {}", e.getMessage()); // Log the error

                        // Return a generic 401 response with a simple message
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                        String errorResponse = "{\"error\": \"Authentication failed\", \"message\": \"Invalid or expired token\"}";

                        DataBuffer buffer = exchange.getResponse()
                                .bufferFactory()
                                .wrap(errorResponse.getBytes(StandardCharsets.UTF_8));

                        return exchange.getResponse().writeWith(Mono.just(buffer));
                    });


        };
    }

    private boolean shouldSkipAuth(String path) {
        return path.startsWith("/auth") ||
                path.startsWith("/actuator") ||
                path.startsWith("/eureka") ||
                path.startsWith("/discovery");
    }

    private Mono<Void> unauthorizedResponse(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }


    public static class Config {
        private boolean enabled = true;


    }




}