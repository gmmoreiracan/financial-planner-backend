package com.bisnagles.financial_planner_backend.plaid;

import com.plaid.client.ApiClient;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.LinkTokenTransactions;
import com.plaid.client.model.Products;
import com.plaid.client.request.PlaidApi;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class PlaidClientParameters {

    @Value("${plaid.env}")
    private String plaidEnvironment;

    @Value("${plaid.client-id}")
    private String plaidClientId;

    @Value("${plaid.client-secret}")
    private String plaidClientSecret;

    @Value("${plaid.client-name}")
    private String plaidClientName;

    @Value("${plaid.country-codes}")
    private String plaidCountryCodeString;

    @Value("${plaid.products}")
    private String plaidProductString;

    @Value("${plaid.redirect-uri}")
    private String redirectUri;

    @Value("${plaid.webhook}")
    private String plaidWebhook;

    private List<CountryCode> plaidCountryCodes;
    private List<Products> plaidProducts;
    private LinkTokenTransactions linkTokenTransactions;

    public String getPlaidEnvironment() {
        return Objects.equals(plaidEnvironment, "production") ? ApiClient.Production : ApiClient.Sandbox;
    }

    public PlaidApi getPlaidApi(){
        HashMap<String, String> apiKeys = new HashMap<>();
        apiKeys.put("clientId", this.getPlaidClientId());
        apiKeys.put("secret", this.getPlaidClientSecret());
        //apiKeys.put("plaidVersion", "2020-09-14");
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(this.getPlaidEnvironment());

        return apiClient.createService(PlaidApi.class);
    }
}
