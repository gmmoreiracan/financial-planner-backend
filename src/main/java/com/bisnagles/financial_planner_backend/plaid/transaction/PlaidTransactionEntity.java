package com.bisnagles.financial_planner_backend.plaid.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlaidTransactionEntity(@JsonProperty String linkToken) {
}
