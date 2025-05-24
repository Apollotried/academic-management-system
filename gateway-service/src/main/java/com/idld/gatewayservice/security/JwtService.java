    package com.idld.gatewayservice.security;

    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;

    import javax.crypto.SecretKey;
    import java.util.List;

    @Service
    public class JwtService {

        @Value("${application.security.jwt.secret-key}")
        private String secretKey;


        public boolean isTokenValid(String token) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(getSignInKey())
                        .build()
                        .parseClaimsJws(token);
                return true;
            }catch (Exception e){
                System.err.println("JWT Validation Failed: " + e.getMessage());
                return false;
            }

        }

        public String extractUsername(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        public List<String> extractRoles(String token) {
            return parseClaims(token).get("authorities", List.class);
        }

        private Claims parseClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private SecretKey getSignInKey() {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }


