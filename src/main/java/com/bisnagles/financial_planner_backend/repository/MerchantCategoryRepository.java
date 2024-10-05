package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Category;
import com.bisnagles.financial_planner_backend.model.Merchant;
import com.bisnagles.financial_planner_backend.model.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {
}
