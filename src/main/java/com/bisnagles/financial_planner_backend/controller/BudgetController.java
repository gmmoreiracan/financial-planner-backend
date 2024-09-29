package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.BudgetSimplifiedResponseDTO;
import com.bisnagles.financial_planner_backend.model.Budget;
import com.bisnagles.financial_planner_backend.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public Budget createBudget(@RequestBody Budget budget) {
        return budgetService.createBudget(budget);
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @GetMapping("/simplified")
    public  List<BudgetSimplifiedResponseDTO> getSimplifiedBudgets(){
        return budgetService.getAllBudgets().stream()
                .map(budget -> new BudgetSimplifiedResponseDTO(budget.getCategory(), budget.getAllocatedAmount()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable Long id) {
        return budgetService.getBudgetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Other account-related endpoints...
}

