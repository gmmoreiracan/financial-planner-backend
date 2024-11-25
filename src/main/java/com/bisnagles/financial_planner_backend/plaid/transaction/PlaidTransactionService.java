package com.bisnagles.financial_planner_backend.plaid.transaction;

import com.bisnagles.financial_planner_backend.dto.TransactionRequestDTO;
import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Item;
import com.bisnagles.financial_planner_backend.plaid.PlaidClientParameters;
import com.bisnagles.financial_planner_backend.plaid.account.PlaidAccountService;
import com.bisnagles.financial_planner_backend.service.AccountService;
import com.bisnagles.financial_planner_backend.service.ItemService;
import com.bisnagles.financial_planner_backend.service.TransactionService;
import com.plaid.client.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@AllArgsConstructor
@Slf4j
@Service
public class PlaidTransactionService {
    @Autowired
    private PlaidClientParameters plaidClientParameters;

    @Autowired
    private ItemService itemService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PlaidAccountService plaidAccountService;

    @Autowired
    private AccountService accountService;

    @Async
    public void syncTransactionsForItemId(String itemId){
        syncTransactionsForItemAsync(itemService.getItemByPlaidID(itemId).orElseThrow());
    }

    public void syncTransactionsForItem(Item item) throws IOException {
        assert item != null && item.getAccessToken() != null;

        String accessToken = item.getAccessToken();
        String cursor = item.getTransactionCursor();

        boolean hasMore = false;
        do {
            TransactionsSyncResponse response = fetchTransactions(accessToken, cursor);
            if (response == null) return; // Exit if response is null

            // If cursor is empty, skip processing and retry after a delay
            if (response.getNextCursor().isEmpty()) {
                sleepWithExponentialBackoff();
                continue;
            }

            saveNewTransactions(response, item.getOwnerId());
            cursor = response.getNextCursor();
            item.setTransactionCursor(cursor);
            hasMore = response.getHasMore();

        } while (hasMore);

        itemService.updateItem(item);
    }

    @Async
    public void syncTransactions(List<Item> items) {items.forEach(this::syncTransactionsForItemAsync);}

    @Async
    public void syncTransactionsForItemAsync(Item item){
            try {
                plaidAccountService.createAccountsForItem(item);
                syncTransactionsForItem(item);
                log.info("Transactions syncd for {}", item);
            } catch (IOException e) {
                log.error("syncTransactions error: {}",e.getMessage());
                throw new RuntimeException(e);
            }
    }


    private TransactionsSyncResponse fetchTransactions(String accessToken, String cursor) throws IOException {
        TransactionsSyncRequest request = new TransactionsSyncRequest()
                .accessToken(accessToken)
                .options(new TransactionsSyncRequestOptions().includeOriginalDescription(true).includePersonalFinanceCategory(true))
                .cursor(cursor);
        Response<TransactionsSyncResponse> response = plaidClientParameters.getPlaidApi().transactionsSync(request).execute();
        return response.body();
    }

    private void saveNewTransactions(TransactionsSyncResponse response, Long ownerId) {
        response.getAdded().forEach(transaction -> {
            Account account = accountService.getAccountByPlaidId(transaction.getAccountId()).orElseThrow(() -> new RuntimeException("Account " + transaction.getAccountId() + " not found"));
            try {
                transactionService.createTransaction(
                        TransactionRequestDTO.builder()
                                .date(transaction.getDate())
                                .description(transaction.getOriginalDescription())
                                .amount(transaction.getAmount())
                                .category(
                                        (
                                                transaction.getPersonalFinanceCategory() != null &&
                                                transaction.getPersonalFinanceCategory().getDetailed() != null
                                        )?
                                                transaction.getPersonalFinanceCategory().getDetailed():(
                                                    transaction.getPersonalFinanceCategory() != null &&
                                                    transaction.getPersonalFinanceCategory().getPrimary() != null
                                                )?transaction.getPersonalFinanceCategory().getPrimary():"PENDING"
                                )
                                .merchant(Optional.ofNullable(
                                        transaction.getMerchantName())
                                        .filter(merchName -> !merchName.isBlank())
                                        .orElse("NONE")
                                )
                                .ownerId(ownerId)
                                .accountId(account.getId())
                                .build()
                );
            } catch (Exception e) {
                log.error("Failed creating transaction:\n{}\n\nStacktrace:\n{}",transaction,e.getStackTrace());
            }
        });
    }

    private void sleepWithExponentialBackoff() {
        int attempt = 0;
        int maxRetries = 3;
        long delay = 2000; // Initial delay of 2 seconds
        while (attempt < maxRetries) {
            try {
                Thread.sleep(delay);
                break; // Successful delay, exit loop
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve interrupt status
            }
            delay *= 2; // Double the delay for exponential backoff
            attempt++;
        }
    }


}
