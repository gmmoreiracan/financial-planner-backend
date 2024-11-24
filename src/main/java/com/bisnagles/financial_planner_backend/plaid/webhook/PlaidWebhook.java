package com.bisnagles.financial_planner_backend.plaid.webhook;

import com.plaid.client.model.WebhookType;

public record PlaidWebhook(String environment, String error, String item_id, Integer new_transactions, String webhook_code, WebhookType webhook_type){
}
