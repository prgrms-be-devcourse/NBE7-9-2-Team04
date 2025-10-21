package com.backend.api.question.service;

import com.backend.api.question.dto.request.QuestionAddRequest;
import com.backend.api.question.dto.request.QuestionUpdateRequest;
import com.backend.api.question.dto.response.QuestionResponse;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.global.exception.ErrorCode;
import com.backend.domain.user.entity.User;
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

    private void checkAuthor(User user) {
        if (user == null) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED_USER);
        }
        if (user.getRole() != Role.USER) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }
    }

    //사용자용 질문 생성
    @Transactional
    public QuestionResponse addQuestion(QuestionAddRequest request, User user) {
        checkAuthor(user);
        Question question = Question.builder()
                .title(request.title())
                .content(request.content())
                .author(user)
                .build();

        Question saved = questionRepository.save(question);
        return QuestionResponse.from(saved);
    }

    //사용자용 질문 수정
    @Transactional
    public QuestionResponse updateQuestion(Long questionId, @Valid QuestionUpdateRequest request, User user) {
        checkAuthor(user);
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        if (!question.getAuthor().getId().equals(user.getId())) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }

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

        if (!question.getIsApproved()) {
            throw new ErrorException(ErrorCode.QUESTION_NOT_APPROVED);
        }

        return QuestionResponse.from(question);
    }

    @Transactional
    public void createListQuestion(List<Question> questions){
        questionRepository.saveAll(questions);
    }
}
