package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.model.*;
import com.bisnagles.financial_planner_backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private BudgetService budgetService;

    public Transaction createTransaction(TransactionRequestDTO transactionRequestDTO) {

        Long accountId = transactionRequestDTO.getAccountId();
        Account account;
        if(accountId != null){
            account = accountService.getAccountById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        } else{
            account = accountService.getMainAccount().orElseThrow(() -> new RuntimeException("No account exists"));
        }

        if(account == null){
            throw new RuntimeException("Account not found");
        }

        Optional<Category> category = categoryService.getOrCreateCategoryByName(transactionRequestDTO.getCategory());

        Optional<Merchant> merchant = merchantService.getOrCreateMerchantByName(transactionRequestDTO.getMerchant());

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setDate(transactionRequestDTO.getDate());
        category.ifPresent(transaction::setCategory);
        merchant.ifPresent(transaction::setMerchant);

        if(category.isPresent() && merchant.isPresent()){
            categoryService.addMerchant(category.get(),merchant.get());
        }

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

    // Method to update the budget when a transaction is created/updated
    private void updateBudgetForTransaction(Transaction transaction) {
        // Get the category for the transaction
        Category category = transaction.getCategory();
        LocalDate date = transaction.getDate();

        // Find the budget for this category and the transaction's timestamp
        Optional<Budget> budgetOpt = budgetService.getBudgetForDate(category.getName(), date);

        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();

            // Deduct the transaction amount from the budget's spentAmount
            budget.setSpentAmount(budget.getSpentAmount() - transaction.getAmount());

            // Save the updated budget
            budgetService.updateBudget(budget);
        }
    }

    // Other account-related methods...
}

