package com.bisnagles.financial_planner_backend.controller;

import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Transaction;
import com.bisnagles.financial_planner_backend.service.AccountService;
import com.bisnagles.financial_planner_backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @PostMapping
    public Transaction createTransaction(@org.jetbrains.annotations.NotNull @RequestBody TransactionRequestDTO transactionRequestDTO) {
        // Fetch the account using accountId from the DTO
        Account account = accountService.getAccountById(transactionRequestDTO.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Call the service to create a Transaction with the associated Account
        return transactionService.createTransaction(transactionRequestDTO, account);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Other account-related endpoints...
}

