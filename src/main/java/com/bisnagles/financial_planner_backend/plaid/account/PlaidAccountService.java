package com.bisnagles.financial_planner_backend.plaid.account;

import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Item;
import com.bisnagles.financial_planner_backend.plaid.PlaidClientParameters;
import com.bisnagles.financial_planner_backend.service.AccountService;
import com.bisnagles.financial_planner_backend.service.ItemService;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@Service
public class PlaidAccountService {
    @Autowired
    private PlaidClientParameters plaidClientParameters;

    @Autowired
    private ItemService itemService;

    @Autowired
    private AccountService accountService;

    private Logger logger;

    public Response<AccountsGetResponse> getAccountsForItem(String accessToken) throws IOException {
        return plaidClientParameters.getPlaidApi().accountsGet(new AccountsGetRequest().accessToken(accessToken)).execute();
    }

    @Async
    public void createAccountsForItemAsync(String itemId){
        try {
            Item item = itemService.getItemByPlaidID(itemId).orElseThrow();

            // Simulate long-running task
            logger.info("Accounts created: " + createAccountsForItem(item).toString());
        } catch (IOException e) {
            logger.info("createAccountsForItem Error: " + Arrays.toString(e.getStackTrace()));
            Thread.currentThread().interrupt();
        }
    }

    public List<Account> createAccountsForItem(Item item) throws IOException {
        AccountsGetResponse accountsGetResponse = plaidClientParameters.getPlaidApi()
                .accountsGet(new AccountsGetRequest().accessToken(item.getAccessToken()))
                .execute().body();

        assert accountsGetResponse != null;

        return accountsGetResponse.getAccounts().stream()
                .filter(plaidAccount -> accountService.getAccountByPlaidId(plaidAccount.getAccountId()).isEmpty())
                .map(plaidAccount -> {
                    Account account = new Account();
                    account.setBalance(Optional.ofNullable(plaidAccount.getBalances().getCurrent()).orElse(0D));
                    account.setName(Optional.ofNullable(plaidAccount.getOfficialName()).orElse(Optional.ofNullable(plaidAccount.getName()).orElse("")));
                    account.setType(Optional.ofNullable(Objects.requireNonNull(plaidAccount.getSubtype()).toString()).orElse(""));
                    account.setPlaidId(Optional.of(Objects.requireNonNull(plaidAccount.getAccountId())).orElse(""));
                    account.setItem(item);
                    return accountService.createAccount(account);
                })
                .toList();
    }
}


