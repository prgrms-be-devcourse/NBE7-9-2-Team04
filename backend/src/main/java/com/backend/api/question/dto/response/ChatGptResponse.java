package com.backend.api.question.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatGptResponse(
        @JsonProperty("choiceResponses")
        List<ChoiceResponse> choiceResponses
) {
}

