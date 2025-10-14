package com.backend.api.question.service;

import com.backend.api.question.dto.request.AdminQuestionAddRequest;
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
public class AdminQuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponse addQuestion(AdminQuestionAddRequest request) {

        if(request.title() == null || request.title().isBlank()) {
            throw new ErrorException(ErrorCode.QUESTION_TITLE_NOT_BLANK);
        }

        if(request.content() == null || request.content().isBlank()) {
            throw new ErrorException(ErrorCode.QUESTION_CONTENT_NOT_BLANK);
        }

        if (request.score() != null && request.score() < 0) {
            throw new ErrorException(ErrorCode.INVALID_QUESTION_SCORE);
        }


        Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .build();

        if (request.isApproved() != null) {
            question.setApproved(request.isApproved());
        }

        if (request.score() != null) {
            question.setScore(request.score());
        }

        Question saved = questionRepository.save(question);
        return QuestionResponse.from(saved);
    }
}
