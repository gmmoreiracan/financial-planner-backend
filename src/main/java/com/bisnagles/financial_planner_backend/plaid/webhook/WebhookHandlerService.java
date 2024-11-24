package com.bisnagles.financial_planner_backend.plaid.webhook;

import com.bisnagles.financial_planner_backend.plaid.transaction.PlaidTransactionService;
import com.plaid.client.model.WebhookType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class WebhookHandlerService {

    @Autowired
    private PlaidTransactionService transactionService;

    public void handleWebhook(WebhookType type, String itemId) throws IOException {
        assert type.equals(WebhookType.TRANSACTIONS);
        transactionService.syncTransactionsForItemId(itemId);
    }
}
