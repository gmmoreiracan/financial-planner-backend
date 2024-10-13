package com.bisnagles.financial_planner_backend;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.bisnagles.financial_planner_backend.model.Transaction;
import com.bisnagles.financial_planner_backend.repository.TransactionRepository;
import com.bisnagles.financial_planner_backend.service.TransactionService;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    public TransactionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);

        // Assert
        assertNotNull(updatedTransaction);
        assertEquals(1L, updatedTransaction.getId());
        assertEquals(100.0, updatedTransaction.getAmount());
        verify(transactionRepository, times(1)).save(transaction);
    }
}