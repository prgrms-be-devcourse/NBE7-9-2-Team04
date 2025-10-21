package com.backend.api.question.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChoiceResponse(
        @JsonProperty("message")
        MessageResponse message
) {
}
