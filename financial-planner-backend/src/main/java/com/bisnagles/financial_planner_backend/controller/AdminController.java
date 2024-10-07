package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.model.User;
import com.bisnagles.financial_planner_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Expose the create user endpoint
    @PostMapping("/create-admin-user")
    public ResponseEntity<String> createAdminUser(@RequestBody User user) {
        try {
            userService.createUser(user); // Only allow creating the first admin user
            return ResponseEntity.ok("Admin user created successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage()); // If admin exists, return a forbidden status
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.createUser(user); // Only allow creating the first admin user
            return ResponseEntity.ok("User created successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage()); // If admin exists, return a forbidden status
        }
    }
}
