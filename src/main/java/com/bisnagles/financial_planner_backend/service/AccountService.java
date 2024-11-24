package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.repository.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @SuppressWarnings("unused")
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getMainAccount(){
        return accountRepository.getMainAccount();
    }

    @SuppressWarnings("unused")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @SuppressWarnings("unused")
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @SuppressWarnings("unused")
    public Double getTotalBalance(List<Long> accountIds) {
        if (accountIds == null || accountIds.isEmpty()) {
            return accountRepository.getTotalBalance(null);  // Sum all accounts
        } else {
            return accountRepository.getTotalBalance(accountIds);  // Sum specific accounts
        }
    }

    public Optional<Account> getAccountByPlaidId(String accountId) {
        return accountRepository.getAccountByPlaidId(accountId);
    }

    // Other account-related methods...
}

