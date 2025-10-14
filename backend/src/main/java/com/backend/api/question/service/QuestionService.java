package com.backend.api.question.service;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.request.QuestionUpdateRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;

    //사용자용 질문 생성
    @Transactional
    public QuestionResponse addQuestion(QuestionAddRequest request) {

        Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .build();

        Question saved = questionRepository.save(question);
        return QuestionResponse.from(saved);
    }

    //사용자용 질문 수정
    @Transactional
    public QuestionResponse updateQuestion(Long questionId, @Valid QuestionUpdateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        question.updateUserQuestion(request.title(), request.content());
        return QuestionResponse.from(question);
    }
}
