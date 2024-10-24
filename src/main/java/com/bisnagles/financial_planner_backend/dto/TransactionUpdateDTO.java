package com.bisnagles.financial_planner_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionUpdateDTO {
    private Long id;
    private String description;
    private String merchant;
    private double amount;
    private LocalDate date;
    private String category; // income, expense, etc.
    private Long accountId;
}
