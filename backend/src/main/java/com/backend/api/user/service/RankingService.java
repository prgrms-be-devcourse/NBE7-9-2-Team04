package com.backend.api.user.service;

import com.backend.api.user.dto.response.RankingResponse;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.entity.UserQuestion;
import com.backend.domain.user.repository.UserQuestionRepository;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final UserQuestionRepository userQuestionRepository;
    private final Rq rq;


    @Transactional
    public void saveOrUpdateUserScore(Long userId, Long questionId, Integer aiScore) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_QUESTION));

        UserQuestion existing = userQuestionRepository
                .findByUser_IdAndQuestion_Id(userId, questionId)
                .orElse(null);

        if (existing != null) {
            existing.updateScore(aiScore);
        } else {
            UserQuestion userQuestion = UserQuestion.builder()
                    .user(user)
                    .question(question)
                    .aiScore(aiScore)
                    .build();
            userQuestionRepository.save(userQuestion);
        }
    }

    /**
     * ✅ 내 랭킹 + 상위 10명 조회
     */
    public RankingResponse getRankingForCurrentUser() {
        Long userId = rq.getUser().getId();
        String nickname = rq.getUser().getNickname();

        List<UserQuestion> all = userQuestionRepository.findAll();

        Map<Long, List<UserQuestion>> groupedByUser = all.stream()
                .collect(Collectors.groupingBy(uq -> uq.getUser().getId()));

        List<Map.Entry<Long, Integer>> totalScores = groupedByUser.entrySet().stream()
                .map(entry -> Map.entry(
                        entry.getKey(),
                        entry.getValue().stream().mapToInt(UserQuestion::getAiScore).sum()
                ))
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .toList();

        List<RankingResponse.RankingEntry> top10 = totalScores.stream()
                .limit(10)
                .map(entry -> new RankingResponse.RankingEntry(
                        groupedByUser.get(entry.getKey()).get(0).getUser().getNickname(),
                        entry.getValue()
                ))
                .toList();

        int myTotalScore = groupedByUser.getOrDefault(userId, List.of())
                .stream().mapToInt(UserQuestion::getAiScore).sum();

        int mySolvedCount = groupedByUser.getOrDefault(userId, List.of()).size();

        int myRank = totalScores.stream()
                .map(Map.Entry::getKey)
                .toList()
                .indexOf(userId) + 1;

        return new RankingResponse(nickname, myTotalScore, mySolvedCount, myRank, top10);
    }
}