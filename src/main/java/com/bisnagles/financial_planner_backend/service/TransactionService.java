package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.config.HibernateFilterConfigurer;
import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.dto.TransactionSummaryDTO;
import com.bisnagles.financial_planner_backend.model.*;
import com.bisnagles.financial_planner_backend.repository.persistence.TransactionRepository;
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

    @Autowired
    private final HibernateFilterConfigurer hibernateFilterConfigurer;

    public TransactionService(HibernateFilterConfigurer hibernateFilterConfigurer) {
        this.hibernateFilterConfigurer = hibernateFilterConfigurer;
    }

    public void deleteTransaction(Long id){
        transactionRepository.deleteById(id);
    }

    public Transaction createTransaction(TransactionRequestDTO transactionRequestDTO) {
        Long accountId = transactionRequestDTO.getAccountId();
        Account account = accountService.getAccountById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

        String categoryInput = Optional.of(transactionRequestDTO.getCategory()).orElse("PENDING");

        Optional<Category> category = categoryService.getOrCreateCategoryByNameWithOwner(categoryInput, account.getOwnerId());

        Optional<Merchant> merchant = merchantService.getOrCreateMerchantByNameWithOwner(transactionRequestDTO.getMerchant(), account.getOwnerId());

        Transaction transaction = findByDateAndAmount(transactionRequestDTO.getDate(), transactionRequestDTO.getAmount()).orElseGet(Transaction::new);
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setDate(transactionRequestDTO.getDate());
        transaction.setAccount(account);
        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setName(transactionRequestDTO.getMerchant());
        category.ifPresent(transaction::setCategory);
        merchant.ifPresent(transaction::setMerchant);


        if(category.isPresent() && merchant.isPresent()){
            categoryService.addMerchant(category.get(),merchant.get());
            updateBudgetForTransaction(transaction);
        }

        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setAccount(account);  // Set the associated account

        Transaction savedTransaction = transactionRepository.save(transaction);

        updateBudgetForTransaction(savedTransaction);

        return savedTransaction;
    }

    public List<Transaction> getAllTransactions() {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return transactionRepository.findById(id);
    }

    public Transaction updateTransaction(Transaction transaction) {
        // Save the updated transactionService
        Transaction updatedTransaction = transactionRepository.save(transaction);

        Category category = updatedTransaction.getCategory();

        Optional<Budget> budget = Optional.empty();

        if(category != null && !category.getName().isEmpty()){
            budget = budgetService.getBudgetForDate(category.getName(), LocalDate.now());
        }


        if(budget.isEmpty()){
            return updatedTransaction;
        }

        // Update the corresponding budget's spentAmount
        Optional<Budget> updatedBudget = updateBudgetForTransaction(updatedTransaction);

        return updatedTransaction;
    }

    public List<TransactionSummaryDTO> getTransactionSummaryForCurrentMonth() {
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        List<TransactionSummaryDTO> transactionSummaryDTOList = transactionRepository.getTransactionSummaryForCurrentMonth();

        return transactionSummaryDTOList;
    }

    public List<Transaction> getTransactionSummaryForCurrentMonthAndCategory(String categoryName, Integer month, Integer year) {
        int finalYear = (year != null) ? year : LocalDate.now().getYear();
        int finalMonth = (month != null) ? month : LocalDate.now().getMonthValue();
        hibernateFilterConfigurer.applyItemOwnershipFilter();
        return transactionRepository.findByCategoryAndMonth(categoryName, finalMonth, finalYear);
    }

    // Method to update the budget when a transactionService is created/updated
    private Optional<Budget> updateBudgetForTransaction(Transaction transaction) {
        // Get the category for the transactionService
        Category category = transaction.getCategory();
        LocalDate date = transaction.getDate();

        // Find the budget for this category and the transactionService's timestamp
        Optional<Budget> budgetOpt = budgetService.getBudgetForDate(category.getName(), date);

        if (budgetOpt.isPresent()) {
            Budget budget = budgetOpt.get();

            // Deduct the transactionService amount from the budget's spentAmount
            budget.setSpentAmount(budget.getSpentAmount() - transaction.getAmount());

            // Save the updated budget
            return Optional.of(budgetService.updateBudget(budget));
        }

        return Optional.empty();
    }

    private Optional<Transaction> findByDateAndAmount(LocalDate date, Double amount){
        return transactionRepository.findByDateAndAmount(date,amount);
    }

    // Other account-related methods...
}

