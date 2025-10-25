package com.backend.api.ranking.service;

import com.backend.api.question.service.QuestionService;
import com.backend.api.ranking.dto.response.RankingResponse;
import com.backend.api.ranking.dto.response.RankingSummaryResponse;
import com.backend.api.userQuestion.service.UserQuestionService;
import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.ranking.entity.Tier;
import com.backend.domain.ranking.repository.RankingRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserQuestionService userQuestionService;
    private final QuestionService questionService;

    @Transactional
    public Ranking createRanking(User user) {

        if (rankingRepository.existsByUser(user)) {
            throw new ErrorException(ErrorCode.RANKING_ALREADY_EXISTS);
        }

        Ranking ranking = Ranking.builder()
                .user(user)
                .totalScore(0)
                .tier(Tier.UNRATED)
                .rankValue(0)
                .build();

        return rankingRepository.save(ranking);
    }


    @Transactional
    public void updateUserRanking(User user){


        int totalScore = userQuestionService.getTotalUserQuestionScore(user);

        Ranking ranking = rankingRepository.findByUser(user)
                .orElseGet(() -> createRanking(user)); // 존재하지 않으면 생성

        // 점수 / 티어 업데이트
        ranking.updateTotalScore(totalScore);
        ranking.updateTier(Tier.fromScore(totalScore));

        // 현재 유저보다 점수 높은 사람 수 계산
        int higherRankCount = rankingRepository.countByTotalScoreGreaterThan(totalScore);

        // 현재 유저의 순위 = 점수 높은 사람 수 + 1
        ranking.updateRank(higherRankCount + 1);

        rankingRepository.save(ranking);
    }

    //마이페이지용
    @Transactional(readOnly = true)
    public RankingResponse getMyRanking(User user) {
        Ranking ranking = rankingRepository.findByUser(user)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));

        int solvedCount = userQuestionService.countSolvedQuestion(user);
        int questionCount = questionService.countByUser(user);

        return RankingResponse.from(ranking, solvedCount, questionCount);
    }


    //상위 10명
    @Transactional(readOnly = true)
    public List<RankingResponse> getTopRankings() {

        List<Ranking> top10 = rankingRepository.findTop10ByOrderByTotalScoreDescUser_NicknameAsc();

        return top10.stream()
                .map(r -> {
                    int solved = userQuestionService.countSolvedQuestion(r.getUser());
                    int submitted = questionService.countByUser(r.getUser());
                    return RankingResponse.from(r, solved, submitted);
                })
                .toList();
    }

    //상위 10명 + 내 랭킹
    @Transactional(readOnly = true)
    public RankingSummaryResponse getRankingSummary(User user) {

        RankingResponse myRanking = getMyRanking(user);
        List<RankingResponse> topRankings = getTopRankings();

        return RankingSummaryResponse.from(myRanking, topRankings);
    }


    //스케줄러 랭킹 재계산
    @Transactional
    public void recalculateAllRankings() {
        List<Ranking> rankings = rankingRepository.findAllByOrderByTotalScoreDesc();
        int ranks = 1;

        for (Ranking r : rankings) {
            r.updateRank(ranks++);
            r.updateTier(Tier.fromScore(r.getTotalScore()));
        }

        rankingRepository.saveAll(rankings);
    }


//
//    private int calculateUserRank(Ranking myRanking) {
//        List<Ranking> rankings = rankingRepository.findAllByOrderByTotalScoreDesc();
//        for (int i = 0; i < rankings.size(); i++) {
//            if (rankings.get(i).getUser().getId().equals(myRanking.getUser().getId())) {
//                return i + 1;
//            }
//        }
//        return rankings.size();
//    }
}
