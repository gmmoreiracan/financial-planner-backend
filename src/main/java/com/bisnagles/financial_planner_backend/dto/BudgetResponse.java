package com.bisnagles.financial_planner_backend.dto;

import com.bisnagles.financial_planner_backend.model.Budget;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class BudgetResponse {
    private Budget currentBudget;
    private Budget previousBudget;

    // Getters and setters

}
