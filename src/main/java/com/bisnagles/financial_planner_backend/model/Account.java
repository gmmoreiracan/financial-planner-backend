package com.bisnagles.financial_planner_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Account.java
@Getter
@Setter
@Entity
public class Account extends Auditable {

    private String name;

    private String type; // savings, investments, checking, credit

    private String plaidId;

    private double balance;

    private boolean main;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

    @Override
    protected Long resolveOwnerId() {
        // Fallback to the ownerId of the associated Item if not already set
        return (item != null) ? item.getOwnerId() : null;
    }

    // Getters and setters
}
