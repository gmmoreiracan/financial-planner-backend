package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {
}
