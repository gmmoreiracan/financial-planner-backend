package com.bisnagles.financial_planner_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// Bill.java
@Setter
@Getter
@Builder
@Entity
@Table(name = "items")
@NoArgsConstructor
@AllArgsConstructor
public class Item extends Auditable{
    private String name;
    private String bankName;
    private String plaidID;

    private String transactionCursor;
    private String accessToken;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Override
    protected Long resolveOwnerId() {
        // For Item, the ownerId is directly its own value
        return getOwnerId();
    }
    // Getters and setters
}

