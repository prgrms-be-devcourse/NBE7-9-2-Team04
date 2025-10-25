package com.backend.api.question.dto.response;

public record PortfolioReadResponse(
        Long id,
        String title,
        String content
) {
}
