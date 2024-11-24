package com.bisnagles.financial_planner_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// WishItem.java
@Setter
@Getter
@Entity
public class WishItem extends Auditable{

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    private String name;
    private double estimatedCost;
    private boolean isPurchased;

    @Override
    protected Long resolveOwnerId() {
        return getOwnerId();
    }

    // Getters and setters
}

