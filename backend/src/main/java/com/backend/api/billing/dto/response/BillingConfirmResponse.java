package com.backend.api.billing.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record BillingConfirmResponse(

        @Schema(description = "상점 ID", example = "tosspayments")
        String mId,

        @Schema(description = "고객 키 (Toss customerKey)", example = "FTQglr7UO7lnFiwiWQ8Ud")
        String customerKey,

        @Schema(description = "결제 수단", example = "카드")
        String method,

        @Schema(description = "빌링키 (자동결제용 키)", example = "Z_t5vOvQxrj4499PeiJcjen28-V2RyqgYTwN44Rdzk0=")
        String billingKey,

        @Schema(description = "결제 인증 완료 시각", example = "2021-01-01T10:00:00+09:00")
        String authenticatedAt
) {}
