package com.bisnagles.financial_planner_backend.plaid.item;

import java.util.List;

public record PlaidItemEntity(List<String> availableProducts, List<String> billedProducts, String error, String institutionId, String itemId, String webhook){}
