package com.backend.api.question.service;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponse addQuestion(QuestionAddRequest request) {

        if(request.title() == null || request.title().isBlank()) {
            throw new ErrorException(ErrorCode.QUESTION_TITLE_NOT_BLANK);
        }

        if(request.content() == null || request.content().isBlank()) {
            throw new ErrorException(ErrorCode.QUESTION_CONTENT_NOT_BLANK);
        }

        Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .build();

        Question saved = questionRepository.save(question);
        return QuestionResponse.from(saved);
    }
}
