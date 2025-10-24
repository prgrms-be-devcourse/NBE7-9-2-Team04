package com.backend.api.userQuestion.service;

import com.backend.domain.question.entity.Question;
import com.backend.domain.user.entity.User;
import com.backend.domain.userQuestion.entity.UserQuestion;
import com.backend.domain.userQuestion.repository.UserQuestionRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQuestionService {

    private final UserQuestionRepository userQuestionRepository;

    @Transactional
    public UserQuestion createUserQuestion(User user, Question question) {

        if (userQuestionRepository.existsByUserAndQuestion(user, question)) {
            throw new ErrorException(ErrorCode.USER_QUESTION_ALREADY_EXISTS);
        }

        UserQuestion userQuestion = UserQuestion.builder()
                .user(user)
                .question(question)
                .build();

        return userQuestionRepository.save(userQuestion);
    }

    @Transactional
    public void updateUserQuestionScore(User user, Question question, Integer aiScore) {

        if (aiScore == null) return;

        UserQuestion userQuestion = createUserQuestion(user, question); //업데이트 할 때 존재하지 않으면 생성
        userQuestion.updateAiScoreIfHigher(aiScore);

        userQuestionRepository.save(userQuestion);
    }

    @Transactional(readOnly = true)
    public Integer getTotalUserQuestionScore(User user) {
        return userQuestionRepository.sumAiScoreByUser(user).orElse(0);
    }
}
