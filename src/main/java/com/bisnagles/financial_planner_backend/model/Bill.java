package com.bisnagles.financial_planner_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// Bill.java
@Setter
@Getter
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double amount;
    private LocalDate dueDate;
    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Getters and setters
}

