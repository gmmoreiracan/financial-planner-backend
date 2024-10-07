package com.bisnagles.financial_planner_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {
    private static final Logger logger = LoggerFactory.getLogger(CustomJwtAuthenticationConverter.class);

    protected static Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // Example: Extract "roles" claim from the JWT token
        List<String> roles = jwt.getClaimAsStringList("roles");  // Get the roles as a list

        logger.debug("Extracting roles from JWT: {}", roles);  // Log the extracted roles

        // Convert roles to a collection of GrantedAuthority
        return roles.stream()
                .map(SimpleGrantedAuthority::new)  // Convert to ROLE_<role>
                .collect(Collectors.toList());
    }
}
