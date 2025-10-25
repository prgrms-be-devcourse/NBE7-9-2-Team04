package com.backend.api.question.dto.response;

import java.util.List;


public record PortfolioListReadResponse(
        Long count,
        List<PortfolioReadResponse> questions
) {
    public static PortfolioListReadResponse from(Long count,List<PortfolioReadResponse> questions) {
        return new PortfolioListReadResponse(count, questions);
    }
}
