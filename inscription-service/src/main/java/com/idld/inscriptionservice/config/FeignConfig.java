package com.idld.inscriptionservice.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Propagate headers from current request context
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                requestTemplate.header(
                        "X-Authenticated-Roles",
                        request.getHeader("X-Authenticated-Roles")
                );
                requestTemplate.header(
                        "X-Authenticated-User",
                        request.getHeader("X-Authenticated-User")
                );

                requestTemplate.header(
                        "X-Authenticated-UserId",
                        request.getHeader("X-Authenticated-UserId")
                );
            }
        };
    }
}

