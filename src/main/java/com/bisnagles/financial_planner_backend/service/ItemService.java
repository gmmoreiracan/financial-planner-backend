package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.config.AuditorAwareImpl;
import com.bisnagles.financial_planner_backend.config.HibernateFilterConfigurer;
import com.bisnagles.financial_planner_backend.model.Item;
import com.bisnagles.financial_planner_backend.repository.persistence.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ItemService {

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private final HibernateFilterConfigurer hibernateFilterConfigurer;
    @Autowired
    private final AuditorAwareImpl auditorAware;

    public List<Item> getItemsForUser(){
        Long userId = auditorAware.getCurrentAuditorId().orElse(-1L);
        return itemRepository.findByOwnerId(userId);
    }

    public Optional<Item> getItemByPlaidID(String plaidID){
        return itemRepository.findByPlaidID(plaidID);
    }

    public Item getByPlaidIDOrCreate(Long userId,String plaidId){
        Optional<Item> optionalItem = getItemByPlaidID(plaidId);

        if(optionalItem.isPresent()){
            return optionalItem.get();
        }

        Item item = Item.builder().plaidID(plaidId).build();
        item.setOwnerId(userId);

        return itemRepository.save(item);
    }

    public Item updateItem(Item item){
        return itemRepository.save(item);
    }

}
