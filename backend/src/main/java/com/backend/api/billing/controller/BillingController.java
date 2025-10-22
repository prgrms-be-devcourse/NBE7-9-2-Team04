package com.backend.api.billing.controller;

import com.backend.api.billing.dto.request.BillingRequest;
import com.backend.api.billing.dto.response.BillingResponse;
import com.backend.api.billing.service.BillingService;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
@Tag(name = "Billing", description = "자동결제 관련 API")
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/issue")
    @Operation(summary = "빌링키 발급", description = "Toss 인증 완료 후 빌링키를 발급받습니다.")
    public ApiResponse<BillingResponse> issue(
            @RequestBody BillingRequest request)
    {
        BillingResponse response = billingService.issueBillingKey(request);
        return ApiResponse.created("빌링키를 발급하였습니다",  response);
    }

}
