package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.Category;
import com.bisnagles.financial_planner_backend.model.Merchant;
import com.bisnagles.financial_planner_backend.model.MerchantCategory;
import com.bisnagles.financial_planner_backend.repository.CategoryRepository;
import com.bisnagles.financial_planner_backend.repository.MerchantCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    MerchantCategoryRepository merchantCategoryRepository;

    public Category createCategory(Category category) { return categoryRepository.save(category); }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryByName(String name) {
        if(name.trim().isEmpty()) {
            return Optional.empty();
        }

        // Proceed with querying the database and mapping the result
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .flatMap(categories -> categories.isEmpty() ? Optional.empty() : Optional.of(categories.getFirst()));
    }

    public Optional<Category> getOrCreateCategoryByName(String name){
        if(name.isEmpty()){
            return Optional.empty();
        }

        Optional<Category> optionalCategory = getCategoryByName(name);

        if(optionalCategory.isPresent()){
            return optionalCategory;
        }

        Category category = new Category();
        category.setName(name);

        return Optional.of(createCategory(category));
    }

    public MerchantCategory addMerchant(Category category, Merchant merchant){
        if(category == null || merchant == null){
            return null;
        }

        MerchantCategory merchantCategory = new MerchantCategory();

        merchantCategory.setCategory(category);
        merchantCategory.setMerchant(merchant);

        return merchantCategoryRepository.save(merchantCategory);
    }

    // Other account-related methods...
}

