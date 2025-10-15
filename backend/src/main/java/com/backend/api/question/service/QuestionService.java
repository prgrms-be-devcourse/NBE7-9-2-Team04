package com.backend.api.question.service;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.request.QuestionUpdateRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<QuestionResponse> getApprovedQuestions() {
        List<Question> questions = questionRepository.findByIsApprovedTrue();

        if (questions.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_QUESTION);
        }

        return questions.stream()
                .map(QuestionResponse::from)
                .toList();

    }

    @Transactional(readOnly = true)
    public QuestionResponse getApprovedQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        if (!Boolean.TRUE.equals(question.getIsApproved())) {
            throw new ErrorException(ErrorCode.QUESTION_NOT_APPROVED);
        }

        return QuestionResponse.from(question);
    }
}
