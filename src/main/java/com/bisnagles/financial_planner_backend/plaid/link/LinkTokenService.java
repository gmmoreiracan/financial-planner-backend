package com.bisnagles.financial_planner_backend.plaid.link;

import com.bisnagles.financial_planner_backend.model.Item;
import com.bisnagles.financial_planner_backend.model.User;
import com.bisnagles.financial_planner_backend.plaid.PlaidClientParameters;
import com.bisnagles.financial_planner_backend.repository.persistence.ItemRepository;
import com.bisnagles.financial_planner_backend.service.ItemService;
import com.bisnagles.financial_planner_backend.service.UserService;
import com.plaid.client.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@Service
public class LinkTokenService {
    @Autowired
    private PlaidClientParameters plaidClientParameters;

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    public LinkTokenEntity getLinkToken(Long clientUserId) throws IOException {
        User user  = userService.getUserByID(clientUserId).orElseThrow();

        LinkTokenCreateRequestUser requestUser = new LinkTokenCreateRequestUser()
                .clientUserId(user.getId().toString())
                .emailAddress(user.getEmail())
                .legalName(user.getName());

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(requestUser)
//                .clientId(plaidClientParameters.getPlaidClientId())
//                .secret(plaidClientParameters.getPlaidClientSecret())
                .clientName(plaidClientParameters.getPlaidClientName())
                .products(plaidClientParameters.getPlaidProducts())
                .countryCodes(plaidClientParameters.getPlaidCountryCodes())
                .transactions(plaidClientParameters.getLinkTokenTransactions())
                .language("en")
                .redirectUri(plaidClientParameters.getRedirectUri())
                .webhook(plaidClientParameters.getPlaidWebhook());

        Response<LinkTokenCreateResponse> response = plaidClientParameters.getPlaidApi()
                .linkTokenCreate(request)
                .execute();

        LinkTokenCreateResponse linkTokenCreateResponse = response.body();

        assert response.isSuccessful() && linkTokenCreateResponse != null;

        return new LinkTokenEntity(linkTokenCreateResponse.getRequestId(),linkTokenCreateResponse.getLinkToken(), user.getId().toString());
    }

    public Item exchangePublicToken(Long userId, String publicToken) throws IOException {
        // Exchange the public token for an access token
        ItemRecord itemRecord = getPublicTokenExchangeResult(publicToken);

        assert itemRecord.plaidItemId != null && itemRecord.itemGetResponse != null;

        String institution = itemRecord.itemGetResponse.getItem().getInstitutionId();

        InstitutionsGetByIdResponse institutionsGetResponse = getInstitutionsGetByIdResponse(institution);

        Item item = itemService.getItemByPlaidID(itemRecord.plaidItemId).orElse(Item.builder().plaidID(itemRecord.plaidItemId).build());
        item.setOwnerId(userId);
        item.setBankName(Objects.requireNonNull(institutionsGetResponse).getInstitution().getName());
        item.setAccessToken(itemRecord.accessToken);
        item.setActive(true);
        return itemService.updateItem(item);
    }

    private @Nullable InstitutionsGetByIdResponse getInstitutionsGetByIdResponse(String institution) throws IOException {
        return plaidClientParameters.getPlaidApi()
                .institutionsGetById(new InstitutionsGetByIdRequest().institutionId(institution).countryCodes(plaidClientParameters.getPlaidCountryCodes()))
                .execute().body();
    }

    private @NotNull ItemRecord getPublicTokenExchangeResult(String publicToken) throws IOException {
        ItemPublicTokenExchangeResponse itemPublicTokenExchangeResponse = plaidClientParameters.getPlaidApi()
                .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest().publicToken(publicToken))
                .execute().body();
        assert itemPublicTokenExchangeResponse != null;
        String accessToken = itemPublicTokenExchangeResponse.getAccessToken();
        String plaidItemId = itemPublicTokenExchangeResponse.getItemId();

        return new ItemRecord(accessToken,plaidItemId,Objects.requireNonNull(plaidClientParameters.getPlaidApi()
                .itemGet(new ItemGetRequest().accessToken(accessToken))
                .execute().body()));
    }


    private record ItemRecord(String accessToken,String plaidItemId, ItemGetResponse itemGetResponse){}
}
