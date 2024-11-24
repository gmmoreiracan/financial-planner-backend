package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public String authenticateLoginRequest(String username, String password) {
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return tokenProvider.generateToken(userDetails);
    }
}
