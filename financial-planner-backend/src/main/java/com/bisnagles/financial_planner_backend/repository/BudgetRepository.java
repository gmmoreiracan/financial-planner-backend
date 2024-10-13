package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT b FROM Budget b WHERE b.category.id = :categoryId AND :date BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findCurrentBudgetByCategoryId(Long categoryId, LocalDate date);

    // Find the most recent previous budget for a specific category by its ID
    @Query("SELECT b FROM Budget b WHERE b.category.id = :categoryId AND b.endDate < :date ORDER BY b.endDate DESC")
    Optional<Budget> findPreviousBudgetByCategoryId(Long categoryId, LocalDate date);
}
