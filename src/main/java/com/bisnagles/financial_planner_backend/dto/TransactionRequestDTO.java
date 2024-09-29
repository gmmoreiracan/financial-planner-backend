package com.bisnagles.financial_planner_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequestDTO {
    private String description;
    private double amount;
    private LocalDate date;
    private String type; // income, expense, etc.
    private Long accountId;
}
