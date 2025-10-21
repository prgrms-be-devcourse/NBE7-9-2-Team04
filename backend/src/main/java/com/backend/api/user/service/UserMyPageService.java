package com.backend.api.user.service;

import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.entity.UserQuestion;
import com.backend.domain.user.repository.UserQuestionRepository;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class UserMyPageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Rq rq;
    private final UserQuestionRepository userQuestionRepository;


    public UserMyPageResponse modifyUser(Long userId, UserMyPageResponse.UserModify modify) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.updateUser(
                modify.getEmail(),
                passwordEncoder.encode(modify.getPassword()),
                modify.getName(),
                modify.getNickname(),
                modify.getAge(),
                modify.getGithub(),
                modify.getImage()
        );
        if (!rq.getUser().getId().equals(userId)) {
            throw new ErrorException(ErrorCode.SELF_INFORMATION);
        }

        User saved = userRepository.save(user);
        return UserMyPageResponse.fromEntity(saved);
    }

    public UserMyPageResponse getInformation(Long userId){
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));

        return UserMyPageResponse.fromEntity(users);
    }


    public List<UserMyPageResponse.SolvedProblem> getSolvedProblems(Long userId) {

        List<UserQuestion> solvedQuestions = userQuestionRepository.findByUserId(userId);

        return solvedQuestions.stream()
                .map(q -> UserMyPageResponse.SolvedProblem.builder()
                        .title(q.getQuestion().getTitle())
                        .modifyDate(q.getModifyDate())
                        .build())
                .toList();
    }
}
