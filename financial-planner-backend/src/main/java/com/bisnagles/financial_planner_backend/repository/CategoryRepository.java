package com.bisnagles.financial_planner_backend.repository;

import com.bisnagles.financial_planner_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<List<Category>> findByNameContainingIgnoreCase(String name);
}
