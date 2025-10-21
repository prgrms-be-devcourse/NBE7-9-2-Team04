package com.backend.api.question.dto.response;

import com.backend.domain.question.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AiQuestionResponse(
        @JsonProperty("title")
        String title,
        @JsonProperty("content")
        String content,
        @JsonProperty("score")
        Integer score
) {
        public static List<AiQuestionResponse> toDtoList(List<Question> questions){
                return questions.stream()
                        .map(AiQuestionResponse::toDto)
                        .toList();
        }

        public static AiQuestionResponse toDto(Question question) {
                return new AiQuestionResponse(
                        question.getTitle(),
                        question.getContent(),
                        question.getScore()
                );
        }
}
