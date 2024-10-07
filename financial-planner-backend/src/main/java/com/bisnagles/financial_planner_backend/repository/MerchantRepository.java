package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    List<Merchant> findByNameContainingIgnoreCase(String name);
}
