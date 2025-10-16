package com.backend.api.payment.controller;

import com.backend.api.payment.dto.reponse.PaymentResponse;
import com.backend.api.payment.dto.request.PaymentRequest;
import com.backend.api.payment.service.PaymentService;
import com.backend.domain.payment.entity.Payment;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name ="Payments", description = "결제 관련 API")
public class PaymentController {

    private final PaymentService paymentService;

    //결제 승인
    @PostMapping("/confirm")
    public ApiResponse<PaymentResponse> confirm(
            @RequestBody PaymentRequest request
    ){
        Payment payment = paymentService.confirmPayment(request);
        PaymentResponse response = PaymentResponse.from(payment);


        return ApiResponse.ok("결제가 승인되었습니다.", response);

    }

}
