package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.config.HibernateFilterConfigurer;
import com.bisnagles.financial_planner_backend.model.Category;
import com.bisnagles.financial_planner_backend.model.Merchant;
import com.bisnagles.financial_planner_backend.model.MerchantCategory;
import com.bisnagles.financial_planner_backend.repository.persistence.CategoryRepository;
import com.bisnagles.financial_planner_backend.repository.persistence.MerchantCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    MerchantCategoryRepository merchantCategoryRepository;

    @Autowired
    private final HibernateFilterConfigurer hibernateFilterConfigurer;

    public CategoryService(HibernateFilterConfigurer hibernateFilterConfigurer) {
        this.hibernateFilterConfigurer = hibernateFilterConfigurer;
    }


    public Category createCategory(Category category) { return categoryRepository.save(category); }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryByName(String name) {
        if(name.trim().isEmpty()) {
            return Optional.empty();
        }

        log.info("getCategoryByName name: {}",name);

        hibernateFilterConfigurer.applyItemOwnershipFilter();
        // Proceed with querying the database and mapping the result
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public Optional<Category> getOrCreateCategoryByNameWithOwner(String name, Long ownerId){
        if(name.isEmpty() || ownerId == null){
            return Optional.empty();
        }

        Category category = new Category();
        category.setName(name);
        category.setOwnerId(ownerId);

        hibernateFilterConfigurer.applyItemOwnershipFilter();

        Optional<Category> optionalCategory = getCategoryByName(name);

        if(optionalCategory.isEmpty()){
            optionalCategory = Optional.of(createCategory(category));
        }
        return optionalCategory;
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

