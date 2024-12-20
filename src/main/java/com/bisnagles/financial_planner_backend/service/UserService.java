package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.User;
import com.bisnagles.financial_planner_backend.repository.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public Optional<User> getUserByID(Long userId){
        return userRepository.findById(userId.toString());
    }

    public boolean hasUsers() {
        return userRepository.count() > 0;
    }

    public User createAdminUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        if (!hasUsers()) { // Allow creation of the first admin user only if no users exist
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(new HashSet<>(List.of("ROLE_ADMIN","ROLE_USER")));
            return userRepository.save(user);
        } else {
            throw new IllegalStateException("Admin user already exists");
        }
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(List.of("ROLE_USER"))); // Assign the default user role
        return userRepository.save(user);
    }
}

