package com.backend.api.billing.dto.response;

import com.backend.domain.subscription.entity.Subscription;

public record BillingResponse(
        String billingKey,
        String customerKey
) {
    public static BillingResponse from(Subscription subscription) {
        return new BillingResponse(
                subscription.getBillingKey(),
                subscription.getCustomerKey()

        );
    }
}
