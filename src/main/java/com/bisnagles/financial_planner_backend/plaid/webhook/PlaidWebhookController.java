package com.bisnagles.financial_planner_backend.plaid.webhook;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/plaid/webhook")
public class PlaidWebhookController {

    @Autowired
    private PlaidWebhookVerificationService verificationService;

    @Autowired
    private WebhookHandlerService handlerService;

    @PostMapping("/receive")
    public ResponseEntity<String> receiveWebhook(@RequestBody PlaidWebhook body, @RequestHeader HashMap<String,String> headers) throws IOException, ParseException, JOSEException, InterruptedException {
        String signedToken = headers.get("plaid-verification");

        assert verificationService.verifyWebhook(body,signedToken);

        handlerService.handleWebhook(body.webhook_type(),body.item_id());

        return ResponseEntity.ok("received");
    }
}
