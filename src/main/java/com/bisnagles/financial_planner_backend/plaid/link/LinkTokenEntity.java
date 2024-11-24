package com.bisnagles.financial_planner_backend.plaid.link;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LinkTokenEntity(@JsonProperty String requestId,@JsonProperty String linkToken, @JsonProperty String userId) {
}
