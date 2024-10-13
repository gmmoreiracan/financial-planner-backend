package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
