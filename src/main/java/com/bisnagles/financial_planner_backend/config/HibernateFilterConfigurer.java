package com.bisnagles.financial_planner_backend.config;

import jakarta.transaction.Transactional;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.annotation.PostConstruct;

@Component
public class HibernateFilterConfigurer {

    @PersistenceContext
    private EntityManager entityManager;

//    @PostConstruct
//    public void applyUserFilter() {
//        Session session = entityManager.unwrap(Session.class);
//
//        Filter filter = session.enableFilter("userFilter");
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            filter.setParameter("username", authentication.getName());
//        } else {
//            // Handle cases where authentication is null or not authenticated
//            filter.setParameter("username", "system"); // or disable filter
//        }
//    }

    @Transactional
    @PostConstruct
    public void applyItemOwnershipFilter() {
        Session session = entityManager.unwrap(Session.class);

        Filter filter = session.enableFilter("itemOwnershipFilter");

        // Get the current authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwt) {
                // Assuming you have a claim "userId" in your JWT
                Long userId = jwt.getClaim("userId");
                filter.setParameter("userId", userId);
            } else {
                filter.setParameter("userId", -1L); // Invalid userId for unauthenticated context
            }
        } else {
            filter.setParameter("userId", -1L); // Unauthenticated user
        }
    }
}
