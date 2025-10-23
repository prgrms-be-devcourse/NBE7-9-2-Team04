package com.backend.api.ranking.service;

import com.backend.api.userQuestion.service.UserQuestionService;
import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.ranking.entity.Tier;
import com.backend.domain.ranking.repository.RankingRepository;
import com.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserQuestionService userQuestionService;

    @Transactional
    public void updateUserRanking(User user){

        int totalScore = userQuestionService.getTotalUserQuestionScore(user);

        Ranking ranking = rankingRepository.findByUser(user)
                .orElseGet(() -> Ranking.builder()
                        .user(user)
                        .totalScore(0)
                        .tier(Tier.UNRATED)
                        .rank(null)
                        .build());

        ranking.updateTotalScore(totalScore);
        ranking.updateTier(Tier.fromScore(totalScore));

        rankingRepository.save(ranking);
    }

    @Transactional
    public void recalculateAllRankings() {
        List<Ranking> rankings = rankingRepository.findAllByOrderByTotalScoreDesc();
        int rank = 1;

        for (Ranking r : rankings) {
            r.updateRank(rank++);
            r.updateTier(Tier.fromScore(r.getTotalScore()));
        }

        rankingRepository.saveAll(rankings);
    }
}
