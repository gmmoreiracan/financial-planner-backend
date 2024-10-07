package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}
