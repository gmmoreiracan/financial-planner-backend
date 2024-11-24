package com.bisnagles.financial_planner_backend.model;

import  jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// Transaction.java
@Setter
@Getter
@Entity
public class Transaction extends Auditable{
    private String description;
    private Double amount;
    private LocalDate date;
    private String type; // income, expense, etc.

    private String name;

    private Boolean removed;

    @ManyToOne
    @JoinColumn(name = "category_id") // This will create a foreign key in the Transaction table
    private Category category;

    @ManyToOne
    @JoinColumn(name = "merchant_id") // This will create a foreign key in the Transaction table
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    protected Long resolveOwnerId() {
        // Fallback to the ownerId of the associated Account if not already set
        return (account != null) ? account.getOwnerId() : null;
    }
}
