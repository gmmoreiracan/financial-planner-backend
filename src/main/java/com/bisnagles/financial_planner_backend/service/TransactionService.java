package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Transaction;
import com.bisnagles.financial_planner_backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(TransactionRequestDTO transactionRequestDTO, Account account) {
        // Map the DTO to a Bill entity
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setDate(transactionRequestDTO.getDate());
        transaction.setType(transactionRequestDTO.getType());
        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setAccount(account);  // Set the associated account

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    // Other account-related methods...
}

