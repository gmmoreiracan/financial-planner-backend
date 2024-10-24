package com.bisnagles.financial_planner_backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JwtCookieFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "auth_token"; // The name of the cookie that stores the JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        // Get the token from the cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie authCookie = Arrays.stream(cookies)
                    .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            if (authCookie != null) {
                // Set the token in the Authorization header as expected by the oauth2ResourceServer
                String token = authCookie.getValue();
                String bearerToken = "Bearer " + token;

                // Add the token to the Authorization header
                request = new CustomHttpServletRequestWrapper(request, bearerToken);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    // Wrapper class to modify the Authorization header in the request
    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final String bearerToken;

        public CustomHttpServletRequestWrapper(HttpServletRequest request, String bearerToken) {
            super(request);
            this.bearerToken = bearerToken;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return bearerToken;
            }
            return super.getHeader(name);
        }
    }
}