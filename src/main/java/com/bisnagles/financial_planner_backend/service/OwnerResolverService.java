package com.bisnagles.financial_planner_backend.service;

import com.bisnagles.financial_planner_backend.model.*;
import org.springframework.stereotype.Service;

@Service
public class OwnerResolverService {

    public Long resolveOwnerId(Object entity) {
        if (entity instanceof Account account) {
            return account.getItem() != null ? account.getItem().getOwnerId() : null;
        } else if (entity instanceof Transaction transaction) {
            return transaction.getAccount() != null ? transaction.getAccount().getOwnerId() : null;
        } else if (entity instanceof Budget budget) {
            return budget.getCategory() != null ? budget.getCategory().getOwnerId() : null;
        } else if (entity instanceof Category category) {
            return category.getMerchants().stream()
                    .map(MerchantCategory::getOwnerId)
                    .findFirst().orElse(null);
        } else if (entity instanceof Merchant merchant) {
            return merchant.getCategories().stream()
                    .map(MerchantCategory::getOwnerId)
                    .findFirst().orElse(null);
        }
        return null;
    }
}