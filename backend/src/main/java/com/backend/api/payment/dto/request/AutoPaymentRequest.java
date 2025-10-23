package com.backend.api.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AutoPaymentRequest(

        @Schema(description = "구매자 식별용 키", example = "8e92b22a-4a11-4b4a-bf1b-28b0ff812a6c")
        String customerKey,

        @Schema(description = "빌링키", example = "Z_t5vOvQxrj4499PeiJcjen28-V2RyqgYTwN44Rdzk0=")
        String billingKey,

        @Schema(description = "결제 금액", example = "9900")
        Long amount,

        @Schema(description = "주문 ID (고유값)", example = "b05c8d5b-7414-44af-9bcd-053e5eeec1e1")
        String orderId,

        @Schema(description = "주문명", example = "프리미엄 구독 자동결제")
        String orderName,

        @Schema(description = "구매자 이메일", example = "user@email.com")
        String customerEmail,

        @Schema(description = "구매자 이름", example = "홍길동")
        String customerName
) {
}

