package com.backend.api.answer.service;

import com.backend.api.answer.dto.request.AnswerRequest;
import com.backend.api.answer.dto.response.AnswerReadResponse;
import com.backend.api.question.service.QuestionService;
import com.backend.domain.answer.entity.Answer;
import com.backend.domain.answer.repository.AnswerRepository;
import com.backend.domain.question.entity.Question;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    public Answer findByIdOrThrow(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.ANSWER_NOT_FOUND));
    }

    @Transactional
    public Answer writeAnswer(User author, Long questionId, AnswerRequest reqBody) {

        String content = reqBody.content();

        Question question = questionService.findByIdOrThrow(questionId);
        Boolean isPublic = reqBody.isPublic();

        Answer answer = Answer.builder()
                .content(content)
                .isPublic(isPublic)
                .author(author)
                .question(question)
                .build();

        return answerRepository.save(answer);
    }

    @Transactional
    public Answer updateAnswer(User author, Long answerId, AnswerRequest reqBody) {
        Answer answer = this.findByIdOrThrow(answerId);

        if (!answer.getAuthor().getId().equals(author.getId())) {
            throw new ErrorException(ErrorCode.ANSWER_INVALID_USER);
        }

        answer.update(reqBody.content(), reqBody.isPublic());

        return answer;
    }

    @Transactional
    public void deleteAnswer(User author, Long answerId) {
        Answer answer = this.findByIdOrThrow(answerId);

        if (!answer.getAuthor().getId().equals(author.getId())) {
            throw new ErrorException(ErrorCode.ANSWER_INVALID_USER);
        }

        answerRepository.delete(answer);
    }

    public List<AnswerReadResponse> findAnswers(Long questionId) {
        Question question = questionService.findByIdOrThrow(questionId);

        return question.getAnswers().reversed().stream()
                .filter(Answer::isPublic)
                .map(AnswerReadResponse::new)
                .toList();
    }

    public Answer findAnswer (Long questionId, Long answerId) {
        Question question = questionService.findByIdOrThrow(questionId);
        Answer answer = question.getAnswerByIdOrThrow(answerId);
        if(!answer.isPublic()) {
            throw new ErrorException(ErrorCode.ANSWER_NOT_PUBLIC);
        }

        return answer;
    }

}
