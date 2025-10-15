package com.backend.api.question.service;

import com.backend.api.question.dto.request.AdminQuestionAddRequest;
import com.backend.api.question.dto.request.AdminQuestionUpdateRequest;
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
public class AdminQuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public QuestionResponse addQuestion(AdminQuestionAddRequest request) {

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

    @Transactional
    public QuestionResponse updateQuestion(Long questionId, @Valid AdminQuestionUpdateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        question.updateAdminQuestion(
                request.title(),
                request.content(),
                request.isApproved(),
                request.score()
        );

        return QuestionResponse.from(question);
    }

    @Transactional
    public QuestionResponse approveQuestion(Long questionId, boolean isApproved) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        question.setApproved(isApproved);

        return QuestionResponse.from(question);
    }

    @Transactional
    public QuestionResponse setQuestionScore(Long questionId, Integer score) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        question.setScore(score);

        return QuestionResponse.from(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();

        if (questions.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_QUESTION);
        }

        return questions.stream()
                .map(QuestionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        return QuestionResponse.from(question);
    }
}
