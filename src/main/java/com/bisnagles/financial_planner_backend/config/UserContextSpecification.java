package com.bisnagles.financial_planner_backend.config;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContextSpecification {

    public static <T> Specification<T> belongsToCurrentUser() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return (root, query, cb) -> cb.equal(root.get("createdBy"), currentUser);
    }
}