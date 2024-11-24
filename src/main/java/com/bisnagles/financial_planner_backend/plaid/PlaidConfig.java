package com.bisnagles.financial_planner_backend.plaid;

import com.plaid.client.model.CountryCode;
import com.plaid.client.model.LinkTokenTransactions;
import com.plaid.client.model.Products;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Optional;

@Configuration
public class PlaidConfig {

    @Bean
    public PlaidClientParameters plaidClientConfig() {
        PlaidClientParameters params = new PlaidClientParameters();
        params.setPlaidCountryCodes(Arrays.stream(Optional.ofNullable(params.getPlaidCountryCodeString()).orElse("CA").split(","))
                .map(CountryCode::fromValue)
                .toList());

        params.setPlaidProducts(Arrays.stream(Optional.ofNullable(params.getPlaidProductString()).orElse("auth,transactions").split(","))
                .map(Products::fromValue)
                .toList());

        params.setLinkTokenTransactions(new LinkTokenTransactions().daysRequested(730));

        return params;
    }
}
