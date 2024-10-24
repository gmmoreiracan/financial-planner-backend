package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.JwtAuthenticationResponse;
import com.bisnagles.financial_planner_backend.dto.LoginRequest;
import com.bisnagles.financial_planner_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Generate JWT token
        String jwt = authService.authenticateLoginRequest(loginRequest.getUsername(),loginRequest.getPassword());

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
