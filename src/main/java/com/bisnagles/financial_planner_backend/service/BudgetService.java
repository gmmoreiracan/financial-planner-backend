package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.config.HibernateFilterConfigurer;
import com.bisnagles.financial_planner_backend.dto.BudgetRequestDTO;
import com.bisnagles.financial_planner_backend.dto.BudgetResponse;
import com.bisnagles.financial_planner_backend.model.Budget;
import com.bisnagles.financial_planner_backend.model.Category;
import com.bisnagles.financial_planner_backend.repository.persistence.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HibernateFilterConfigurer hibernateFilterConfigurer;

    public Budget createBudget(BudgetRequestDTO budgetRequestDTO) {
        String requestCategory = budgetRequestDTO.getCategory();
        if(requestCategory.isEmpty()){
            requestCategory = "PENDING";
        }

        Optional<Category> category = categoryService.getOrCreateCategoryByNameWithOwner(requestCategory, budgetRequestDTO.getOwnerId());

        if(category.isEmpty()){
            throw new DataRetrievalFailureException("No categories exist in the database");
        }

        Budget budget = new Budget();
        budget.setCategory(category.get());
        budget.setAllocatedAmount(budgetRequestDTO.getAllocatedAmount());
        budget.setSpentAmount(budgetRequestDTO.getSpentAmount());

        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Budget budget){
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return budgetRepository.save(budget);
    }

    public Optional<Budget> getBudgetForDate(String categoryName, LocalDate date) {
        Optional<Category> categoryOpt = categoryService.getCategoryByName(categoryName);

        if (categoryOpt.isEmpty()) {
            // Handle case when category is not found (return an error or empty response)
            return Optional.empty();
        }
        hibernateFilterConfigurer.applyItemOwnershipFilter();

        return budgetRepository.findCurrentBudgetByCategoryId(categoryOpt.get().getId(), date);
    }

    public Optional<Budget> getCurrentBudget(String categoryName) {
        LocalDate now = LocalDate.now();
        return getBudgetForDate(categoryName, now);
    }

    // Get the previous budget for a category
    public Optional<Budget> getPreviousBudget(String categoryName) {
        Optional<Category> categoryOpt = categoryService.getCategoryByName(categoryName);

        if (categoryOpt.isEmpty()) {
            // Handle case when category is not found (return an error or empty response)
            return Optional.empty();
        }

        hibernateFilterConfigurer.applyItemOwnershipFilter();

        LocalDate now = LocalDate.now();
        return budgetRepository.findPreviousBudgetByCategoryId(categoryOpt.get().getId(), now);
    }

    // Fetch budgets for a specific category by its ID or name
    public BudgetResponse getBudgetsForCategory(String categoryName) {
        Optional<Budget> currentBudget = getCurrentBudget(categoryName);
        Optional<Budget> previousBudget = getPreviousBudget(categoryName);

        BudgetResponse response = new BudgetResponse();
        currentBudget.ifPresent(response::setCurrentBudget);
        previousBudget.ifPresent(response::setPreviousBudget);

        return response;
    }

    public List<Budget> getAllBudgets() {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return budgetRepository.findAll();
    }

    public Optional<Budget> getBudgetById(Long id) {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return budgetRepository.findById(id);
    }

    // Other account-related methods...
}

