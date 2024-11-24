package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.config.HibernateFilterConfigurer;
import com.bisnagles.financial_planner_backend.model.Merchant;
import com.bisnagles.financial_planner_backend.repository.persistence.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private final HibernateFilterConfigurer hibernateFilterConfigurer;

    public MerchantService(HibernateFilterConfigurer hibernateFilterConfigurer) {
        this.hibernateFilterConfigurer = hibernateFilterConfigurer;
    }

    public Merchant createMerchant(Merchant merchant) {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return merchantRepository.save(merchant);
    }

    public List<Merchant> getAllMerchants() {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return merchantRepository.findAll();
    }

    public Optional<Merchant> getMerchantById(Long id) {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return merchantRepository.findById(id);
    }

    public List<Merchant> getMerchantsBySimilarName(String name) {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        // Step 1: Initial search using full name
        List<Merchant> merchants = merchantRepository.findByNameContainingIgnoreCase(name);

        // Step 2: If no results, split the name and search for combinations
        if (merchants.isEmpty()) {
            String[] nameParts = name.split("\\s+"); // Splitting by spaces
            merchants = searchCombinations(nameParts, 0, new HashSet<>());
        }

        return merchants;
    }

    private List<Merchant> searchCombinations(String[] nameParts, int start, Set<String> seenCombinations) {
        // Step 3: Base case - If all parts have been combined, return an empty list
        if (start >= nameParts.length) {
            return Collections.emptyList();
        }

        // Step 4: Generate all possible combinations starting from index "start"
        StringBuilder combination = new StringBuilder();
        List<Merchant> result;

        for (int i = start; i < nameParts.length; i++) {
            // Build combination from parts (i.e., "No", then "No Frills", then "No Frills #1234")
            if (!combination.isEmpty()) {
                combination.append(" ");
            }
            combination.append(nameParts[i]);

            // Skip if combination was already checked
            String combinationStr = combination.toString();
            if (!seenCombinations.contains(combinationStr)) {
                seenCombinations.add(combinationStr);

                // Step 5: Query the database with the current combination
                result = merchantRepository.findByNameContainingIgnoreCase(combinationStr);
                if (!result.isEmpty()) {
                    // Stop searching if we find results
                    return result;
                }
            }
        }

        // Step 6: Recurse on the next start position (i.e., skip current first part)
        return searchCombinations(nameParts, start + 1, seenCombinations);
    }

    public Optional<Merchant> getOrCreateMerchantByNameWithOwner(String name, Long ownerId) {
        if(name == null || name.isEmpty()){
            return Optional.empty();
        }
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setOwnerId(ownerId);

        hibernateFilterConfigurer.applyItemOwnershipFilter();

        Optional<Merchant> optionalMerchant = merchantRepository.findByNameIgnoreCase(name);

        if(optionalMerchant.isEmpty()){
            optionalMerchant = Optional.of(createMerchant(merchant));
        }

        return optionalMerchant;
    }
}

