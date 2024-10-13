package com.bisnagles.financial_planner_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// Budget.java
@Setter
@Getter
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id") // This will create a foreign key in the Transaction table
    @JsonIgnore
    private Category category;

    private Double allocatedAmount;
    private Double spentAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    // Getters and setters
}

