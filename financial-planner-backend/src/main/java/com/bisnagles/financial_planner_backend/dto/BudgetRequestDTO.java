package com.bisnagles.financial_planner_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetRequestDTO {
    private String category;
    private double allocatedAmount;
    private double spentAmount;
}
