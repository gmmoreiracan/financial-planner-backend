package com.bisnagles.financial_planner_backend.dto;

import lombok.Getter;
import lombok.Setter;

// BudgetDTO.java
@Getter
@Setter
public class BudgetSimplifiedResponseDTO {
    // Getters and setters (if needed)
    private String name;    // This will map to "category"
    private double budget;  // This will map to "allocatedAmount"

    // Constructor to map entity fields to the desired response fields
    public BudgetSimplifiedResponseDTO(String category, double allocatedAmount) {
        this.name = category;
        this.budget = allocatedAmount;
    }

}
