package com.backend.api.payment.dto.request;

import com.backend.domain.payment.entity.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record PaymentRequest(

        @NotBlank(message = "결제 키 값은 필수입니다.")
        @Size(max = 200, message = "paymentKey는 최대 200자입니다.")
        @Schema(description = "결제 키 값", example = "5EnNZRJGvaBX7zk2yd8ydw26XvwXkLrx9POLqKQjmAw4b0e1")
        String paymentKey,

        @NotBlank(message = "주문번호는 필수입니다.")
        @Schema(description = "주문 번호", example = "a4CWyWY5m89PNh7xJwhk1")
        String orderId,

        @NotNull(message = "결제 금액은 필수입니다.")
        @Schema(description = "결제 금액", example = "3000")
        Long amount
) {
    public PaymentRequest(Payment payment){
        this(
                payment.getPaymentKey(),
                payment.getOrderId(),
                payment.getTotalAmount()
        );
    }
}

