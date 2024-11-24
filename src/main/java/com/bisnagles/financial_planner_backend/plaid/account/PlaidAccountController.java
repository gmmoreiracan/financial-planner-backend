package com.bisnagles.financial_planner_backend.plaid.account;

import com.bisnagles.financial_planner_backend.model.Account;
import com.bisnagles.financial_planner_backend.model.Item;
import com.bisnagles.financial_planner_backend.service.AccountService;
import com.bisnagles.financial_planner_backend.service.ItemService;
import com.plaid.client.model.AccountsGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/plaid/account")
public class PlaidAccountController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private PlaidAccountService plaidAccountService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/get")
    public ResponseEntity<List<AccountsGetResponse>> getAccounts(){
        return ResponseEntity.ok(
            itemService.getItemsForUser().stream()
                .filter(item -> !item.getAccessToken().isEmpty())
                .map(this::getAccountsGetResponse).toList()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<List<Account>> createAccounts(){
        List<Account> accountList = new ArrayList<>();
        itemService.getItemsForUser().stream()
                .filter(item -> !item.getAccessToken().isEmpty())
                .forEach(item -> {
                    try {
                        accountList.addAll(plaidAccountService.createAccountsForItem(item));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        return ResponseEntity.ok(accountList);
    }

    @PostMapping("/create/{plaidItem}")
    public ResponseEntity<List<Account>> createAccountsForItem(@PathVariable String plaidItem){
        return ResponseEntity.ok(itemService.getItemByPlaidID(plaidItem).map(item -> {
            try {
                return plaidAccountService.createAccountsForItem(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).orElse(null));
    }

    private @Nullable AccountsGetResponse getAccountsGetResponse(Item item) {
        try {
            Response<AccountsGetResponse> response = plaidAccountService.getAccountsForItem(item.getAccessToken());
            assert response.isSuccessful();
            return response.body();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}