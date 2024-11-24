package com.bisnagles.financial_planner_backend.repository.persistence;


import com.bisnagles.financial_planner_backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Conditional JPQL query to sum balances based on optional account IDs
    @Query("SELECT SUM(a.balance) FROM Account a WHERE (:accountIds IS NULL OR a.id IN :accountIds)")
    Double getTotalBalance(@Param("accountIds") List<Long> accountIds);

    @Query("SELECT a FROM Account a WHERE a.main = TRUE")
    Optional<Account> getMainAccount();

    Optional<Account> getAccountByPlaidId(String accountId);
}