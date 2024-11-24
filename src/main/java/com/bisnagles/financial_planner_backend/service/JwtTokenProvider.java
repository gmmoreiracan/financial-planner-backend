package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    // Inject the JWT secret key from the application.properties or environment variable
    private final Key key;

    // Constructor with @Value annotation to load the secret
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        // Convert secretKey String into a Key
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(CustomUserDetails userDetails) {
        long validityInMs = 60*60*1000; //1 hour

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)  // Include roles in the JWT
                .addClaims(Map.of("userId",userDetails.getUserID()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }
}
