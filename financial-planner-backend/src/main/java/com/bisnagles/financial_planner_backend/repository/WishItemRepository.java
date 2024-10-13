package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {
}
