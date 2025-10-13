package com.backend.domain.question.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuestionApproveRequest(
        @Schema(description = "승인 여부", example = "true")
        Boolean isApproved
) {}
