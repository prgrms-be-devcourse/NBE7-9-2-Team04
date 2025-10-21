package com.backend.api.question.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiQuestionResponse(
        @JsonProperty("title")
        String title,
        @JsonProperty("content")
        String content,
        @JsonProperty("score")
        Integer score
) {
}
