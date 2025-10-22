package com.backend.api.subscription.controller;


import com.backend.api.subscription.dto.response.SubscriptionResponse;
import com.backend.api.subscription.service.SubscriptionService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;


//    @PostMapping
//    @Operation(summary = "내 구독 생성", description = "사용자의 구독 정보를 추가합니다.")
//    public ApiResponse<SubscriptionResponse> createSubscription(){
//        SubscriptionResponse response = subscriptionService.activatePremium();
//        return ApiResponse.created("구독이 생성되었습니다.", response);
//    }

    @GetMapping("/me")
    @Operation(summary = "내 구독 조회", description = "로그인된 사용자의 구독 정보를 조회합니다.")
    public ApiResponse<SubscriptionResponse> getMySubscription() {
        SubscriptionResponse response = subscriptionService.getMySubscription();
        return ApiResponse.ok("구독 정보를 불러왔습니다.", response);
    }


}
