package com.backend.api.billing.dto.response;

import com.backend.domain.subscription.entity.Subscription;
import io.swagger.v3.oas.annotations.media.Schema;

public record BillingResponse(
        @Schema(description = "빌링키 (자동결제용 키)", example = "Z_t5vOvQxrj4499PeiJcjen28-V2RyqgYTwN44Rdzk0=")
        String billingKey,

        @Schema(description = "고객 키 (Toss customerKey)", example = "FTQglr7UO7lnFiwiWQ8Ud")
        String customerKey
) {
    public static BillingResponse from(Subscription subscription) {
        return new BillingResponse(
                subscription.getBillingKey(),
                subscription.getCustomerKey()

        );
    }
}
