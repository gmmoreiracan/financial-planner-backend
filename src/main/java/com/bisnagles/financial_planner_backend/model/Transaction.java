package com.bisnagles.financial_planner_backend.model;

import  jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// Transaction.java
@Setter
@Getter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Double amount;
    private LocalDate date;
    private String type; // income, expense, etc.

    @ManyToOne
    @JoinColumn(name = "category_id") // This will create a foreign key in the Transaction table
    private Category category;

    @ManyToOne
    @JoinColumn(name = "merchant_id") // This will create a foreign key in the Transaction table
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
