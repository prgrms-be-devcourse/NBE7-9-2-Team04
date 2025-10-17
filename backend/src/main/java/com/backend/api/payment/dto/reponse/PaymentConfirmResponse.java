package com.backend.api.payment.dto.reponse;

import com.backend.domain.payment.entity.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

//Toss Payments API가 정한 필드와 형식을 그대로 매핑
@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentConfirmResponse(

        @Schema(description = "토스에 전달되는 주문 ID", example = "1")
        String orderId,

        @Schema(description = "결제 키 값", example = "1111")
        String paymentKey,

        @Schema(description = "주문 상품명", example = "PREMIUM")
        String orderName,

        @Schema(description = "총 결제 금액", example = "9900")
        Long totalAmount,

        @Schema(description = "결제 수단", example = "카드")
        String method,

        @Schema(description = "결제 상태", example = "DONE")
        PaymentStatus status,

        @Schema(description = "결제 요청 시간", example = "2025-10-13T11:00:00")
        LocalDateTime requestedAt,

        @Schema(description = "결제 수정 시간", example = "2025-10-13T12:00:00")
        LocalDateTime approvedAt
) {

}
