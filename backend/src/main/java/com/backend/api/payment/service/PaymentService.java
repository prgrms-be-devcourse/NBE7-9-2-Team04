package com.backend.api.payment.service;


import com.backend.api.payment.dto.response.PaymentConfirmResponse;
import com.backend.api.payment.dto.response.PaymentResponse;
import com.backend.api.payment.dto.request.PaymentRequest;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentStatus;
import com.backend.domain.payment.repository.PaymentRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository; //삭제 예정
    private final WebClient webClient;
    private final Rq rq;


    //결제 승인
    @Transactional
    public PaymentResponse confirmPayment(PaymentRequest request){
        User user = getOrCreateUser(); // 잠시 임시 유저로 테스트
        PaymentConfirmResponse response = webClient.post()
                .uri("/confirm") // 결제 승인 API 호출
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody ->
                                        Mono.error(new ErrorException(errorBody, ErrorCode.PAYMENT_APPROVE_FAILED))
                                )
                )
                .bodyToMono(PaymentConfirmResponse.class)
                .block();


        Payment payment = Payment.builder()
                .orderId(response.orderId())
                .paymentKey(response.paymentKey())
                .orderName(response.orderName())
                .method(response.method())
                .totalAmount(response.totalAmount())
                .status(PaymentStatus.DONE)
                .approvedAt(LocalDateTime.now())
                .user(user)
                .build();
        paymentRepository.save(payment);

        return PaymentResponse.from(payment);

    }


    //임시 유저. 삭제 예정
    private User getOrCreateUser() {
        try {
            return rq.getUser();
        } catch (Exception e) {

            return userRepository.findByEmail("test@local.com")
                    .orElseGet(() -> {
                        User tempUser = User.builder()
                                .email("test@local.com")
                                .password("test1234")
                                .name("테스트유저")
                                .nickname("tester")
                                .age(25)
                                .github("https://github.com/tester")
                                .role(Role.USER)
                                .build();
                        return userRepository.save(tempUser);
                    });
        }
    }

    public PaymentResponse geyPaymentByKey(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new ErrorException(ErrorCode.PAYMENT_NOT_FOUND));

        return PaymentResponse.from(payment);
    }

    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ErrorException(ErrorCode.PAYMENT_NOT_FOUND));

        return PaymentResponse.from(payment);
    }

    //결제 취소 로직 추가해야함

}
