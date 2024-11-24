package com.bisnagles.financial_planner_backend.plaid.link;

import com.bisnagles.financial_planner_backend.model.Item;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/plaid/link")
public class LinkTokenController {

    @Autowired
    private LinkTokenService linkTokenService;

    @PostMapping("/create")
    public ResponseEntity<LinkTokenEntity> createLinkToken(@RequestBody RequestClient client) throws IOException {
        LinkTokenEntity linkToken = linkTokenService.getLinkToken(client.clientId);

        return ResponseEntity.ok(linkToken);
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<Item> exchangeToken(@RequestBody TokenExchangeRequest tokenExchangeRequest) throws IOException {
        return ResponseEntity.ok(linkTokenService.exchangePublicToken(tokenExchangeRequest.userId, tokenExchangeRequest.linkToken));
    }

    public record RequestClient(Long clientId){}

    public record TokenExchangeRequest(Long userId,String linkToken){}

}
