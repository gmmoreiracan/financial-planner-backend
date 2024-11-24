package com.bisnagles.financial_planner_backend.repository.persistence;

import com.bisnagles.financial_planner_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByPlaidID(String plaidId);
    List<Item> findByOwnerId(Long ownerId);
}