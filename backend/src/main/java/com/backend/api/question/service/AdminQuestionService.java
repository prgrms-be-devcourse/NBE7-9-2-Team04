package com.backend.api.question.service;

import com.backend.api.question.dto.request.AdminQuestionAddRequest;
import com.backend.api.question.dto.request.AdminQuestionUpdateRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
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

    private void checkAdmin(User user) {
        if (user == null) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED_USER);
        }
        if (user.getRole() != Role.ADMIN) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }
    }

    @Transactional
    public QuestionResponse addQuestion(AdminQuestionAddRequest request, User user) {
        checkAdmin(user);
        Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .author(user)
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
    public QuestionResponse updateQuestion(Long questionId, @Valid AdminQuestionUpdateRequest request, User user) {
        checkAdmin(user);
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
    public QuestionResponse approveQuestion(Long questionId, boolean isApproved, User user) {
        checkAdmin(user);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        question.setApproved(isApproved);

        return QuestionResponse.from(question);
    }

    @Transactional
    public QuestionResponse setQuestionScore(Long questionId, Integer score, User user) {
        checkAdmin(user);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        question.setScore(score);

        return QuestionResponse.from(question);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getAllQuestions(User user) {
        checkAdmin(user);
        List<Question> questions = questionRepository.findAll();

        if (questions.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_QUESTION);
        }

        return questions.stream()
                .map(QuestionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestionById(Long questionId, User user) {
        checkAdmin(user);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        return QuestionResponse.from(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId, User user) {
        checkAdmin(user);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        questionRepository.delete(question);
    }
}
