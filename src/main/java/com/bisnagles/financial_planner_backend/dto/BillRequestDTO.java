package com.bisnagles.financial_planner_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BillRequestDTO {
    private String name;
    private double amount;
    private LocalDate dueDate;
    private boolean paid;
    private Long accountId;  // Only the account ID is provided
}
