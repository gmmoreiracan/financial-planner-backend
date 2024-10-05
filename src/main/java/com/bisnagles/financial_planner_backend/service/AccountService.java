package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getMainAccount(){
        return accountRepository.getMainAccount();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Double getTotalBalance(List<Long> accountIds) {
        if (accountIds == null || accountIds.isEmpty()) {
            return accountRepository.getTotalBalance(null);  // Sum all accounts
        } else {
            return accountRepository.getTotalBalance(accountIds);  // Sum specific accounts
        }
    }

    // Other account-related methods...
}

