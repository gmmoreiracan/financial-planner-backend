package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
