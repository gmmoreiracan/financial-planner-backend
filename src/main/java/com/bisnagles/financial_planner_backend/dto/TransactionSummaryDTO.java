package com.bisnagles.financial_planner_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionSummaryDTO {
    private String category;
    private Double totalAmount;

    public TransactionSummaryDTO(String category,Double totalAmount){
        this.category = category;
        this.totalAmount = totalAmount;
    }
}
