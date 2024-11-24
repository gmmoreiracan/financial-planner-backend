package com.bisnagles.financial_planner_backend.repository.persistence;

import com.bisnagles.financial_planner_backend.dto.TransactionSummaryDTO;
import com.bisnagles.financial_planner_backend.model.Transaction;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    @Query("""
            SELECT new com.bisnagles.financial_planner_backend.dto.TransactionSummaryDTO(t.category.name, SUM(t.amount))
            FROM Transaction as t
            WHERE extract (month FROM t.date) = extract (month FROM CURRENT_DATE)
            AND extract (year FROM t.date) = extract (year FROM CURRENT_DATE)
            GROUP BY t.category
            """)
    List<TransactionSummaryDTO> getTransactionSummaryForCurrentMonth();


    @Query("""
            SELECT t FROM Transaction t JOIN t.category c
            WHERE c.name = :categoryName
            AND extract(month FROM t.date) = :month
            AND extract(year FROM t.date) = :year
            """
    )
    List<Transaction> findByCategoryAndMonth(@Param("categoryName") String categoryName,
                                             @Param("month") Integer month,
                                             @Param("year") Integer year
    );

    @Query("SELECT t FROM Transaction t WHERE t.date = :date AND t.amount = :amount")
    Optional<Transaction> findByDateAndAmount(@Param("date")LocalDate date, @Param("amount") Double amount);
}
