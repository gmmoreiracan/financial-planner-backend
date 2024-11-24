package com.bisnagles.financial_planner_backend.config;

import com.bisnagles.financial_planner_backend.model.CustomUserDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is null or not authenticated
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("system"); // Fallback to a default user like "system" or return Optional.empty()
        }

        return Optional.of(authentication.getName());
    }

    public Optional<Long> getCurrentAuditorId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .flatMap(principal ->
                        principal instanceof CustomUserDetails cud
                                ? cud.getUserID().describeConstable()
                                : principal instanceof Jwt jwt
                                ? Optional.ofNullable(jwt.getClaim("userId"))
                                : Optional.empty()
                );
    }
}