package com.bisnagles.financial_planner_backend.plaid.webhook;

import com.bisnagles.financial_planner_backend.plaid.PlaidClientParameters;
import com.nimbusds.jose.JOSEException;
import com.plaid.client.model.WebhookVerificationKeyGetRequest;
import com.plaid.client.model.WebhookVerificationKeyGetResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nimbusds.jose.jwk.ECKey;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPublicKey;

@Setter
@Getter
@Service
public class PlaidWebhookVerificationService {
    @Autowired
    private PlaidClientParameters plaidClientParameters;

    public boolean verifyWebhook(PlaidWebhook body, String signedToken) throws IOException, ParseException, JOSEException {
        // Create ECPublicKey from the parsed verification key
        ECPublicKey publicKey = ECKey.parse(getKey(signedToken).toString()).toECPublicKey();
        try {
            // Set up the verifier with the ECDSA algorithm and a 5-minute leeway
            JWTVerifier verifier = JWT.require(Algorithm.ECDSA256(publicKey, null))
                    .acceptLeeway(300) // 5 minutes
                    .build();

            // Verify the token and extract the payload
            String payloadDecoded = new String(
                    Base64.getDecoder().decode(verifier.verify(signedToken).getPayload()),
                    StandardCharsets.UTF_8
            );

            // Extract the 'request_body_sha256' from the decoded payload
            String sha256InPayload = new JSONObject(payloadDecoded).getString("request_body_sha256");

            // Compute the SHA-256 hash of the webhook body
            String sha256OfWebhookBody = Hashing.sha256()
                    .hashString(body.toString(), StandardCharsets.UTF_8)
                    .toString();

            return sha256OfWebhookBody.equals(sha256InPayload);
        } catch (JWTVerificationException e) {
            // Return false if token verification fails
            return false;
        }
    }

    private JSONObject getKey(String signedToken) throws IOException {
        // Decode the token header and extract necessary fields
        JSONObject decodedTokenHeaderJson = new JSONObject(JWT.decode(signedToken).getHeader());
        String currentKid = decodedTokenHeaderJson.getString("kid");

        // Ensure the algorithm is not ES256
        if ("ES256".equals(decodedTokenHeaderJson.getString("alg"))) {
            throw new IllegalArgumentException("Algorithm ES256 is not supported");
        }

        // Fetch the verification key for the given key ID
        WebhookVerificationKeyGetRequest request = new WebhookVerificationKeyGetRequest().keyId(currentKid);
        Response<WebhookVerificationKeyGetResponse> response = plaidClientParameters.getPlaidApi().webhookVerificationKeyGet(request).execute();

        if (response.body() == null) {
            throw new IOException("Failed to retrieve verification key");
        }

        JSONObject key = new JSONObject(response.body().getKey().toString());

        // Ensure the key is not expired
        if (key.optString("expired_at") != null) {
            throw new IllegalStateException("Verification key is expired");
        }
        return key;
    }
}



