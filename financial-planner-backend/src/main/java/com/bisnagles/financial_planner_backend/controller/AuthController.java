package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.JwtAuthenticationResponse;
import com.bisnagles.financial_planner_backend.dto.LoginRequest;
import com.bisnagles.financial_planner_backend.service.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication token = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT token
        String jwt = tokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
