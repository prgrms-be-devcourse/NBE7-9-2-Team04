package com.backend.api.question.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageResponse(
        @JsonProperty("role")
        String role,
        @JsonProperty("content")
        String content
) {
}
