package com.backend.api.user.dto.request;

import com.backend.domain.user.entity.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AdminUserStatusUpdateRequest(
        @NotNull(message = "계정 상태는 필수입니다.")
        @Schema(description = "변경할 계정 상태", example = "SUSPENDED")
        AccountStatus status
) {}
