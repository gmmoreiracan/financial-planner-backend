package com.bisnagles.financial_planner_backend.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO{
    private Long id;
    private String description;
    private String merchant;
    private double amount;
    private LocalDate date;
    private String category; // income, expense, etc.
    private Long accountId;
    private Long ownerId;
}
