package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.WishItem;
import com.bisnagles.financial_planner_backend.repository.persistence.WishItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishItemService {
    @Autowired
    private WishItemRepository wishItemRepository;

    public WishItem createWishItem(WishItem wishItem) {
        return wishItemRepository.save(wishItem);
    }

    public List<WishItem> getAllWishItems() {
        return wishItemRepository.findAll();
    }

    public Optional<WishItem> getWishItemById(Long id) {
        return wishItemRepository.findById(id);
    }

    // Other account-related methods...
}

