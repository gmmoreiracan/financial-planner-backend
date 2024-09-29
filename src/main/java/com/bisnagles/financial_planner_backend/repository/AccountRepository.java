package com.bisnagles.financial_planner_backend.repository;


import com.bisnagles.financial_planner_backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

