package com.bisnagles.financial_planner_backend.plaid.transaction;

import com.bisnagles.financial_planner_backend.model.Item;
import com.bisnagles.financial_planner_backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/plaid/transaction")
public class PlaidTransactionController {
    @Autowired
    private PlaidTransactionService plaidTransactionService;

    @Autowired
    private ItemService itemService;

    @PostMapping("/sync")
    public ResponseEntity<String> syncTransactions() {
        List<Item> items = itemService.getItemsForUser();
        plaidTransactionService.syncTransactions(items);
        //TODO: Account id is not provided
        return ResponseEntity.ok("Transactions sync'd");

    }

}
