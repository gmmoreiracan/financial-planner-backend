package com.bisnagles.financial_planner_backend.repository.entity_listeners;

import com.bisnagles.financial_planner_backend.config.AuditorAwareImpl;
import com.bisnagles.financial_planner_backend.model.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomAuditingEntityListener {
    @Autowired
    private AuditorAwareImpl auditorAware;

    @PrePersist
    @PreUpdate
    public void setOwnerId(Auditable auditable) {
        auditable.assignOwnerId();
    }
}

