package com.backend.api.billing.dto.request;

public record BillingRequest(
        String customerKey,
        String authKey
) {
}
