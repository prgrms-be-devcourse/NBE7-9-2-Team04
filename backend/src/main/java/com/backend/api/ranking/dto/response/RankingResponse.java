package com.backend.api.ranking.dto.response;

import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.ranking.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;

public record RankingResponse (
        @Schema(description = "사용자 ID", example = "12")
        Long userId,

        @Schema(description = "닉네임", example = "김개발")
        String nickName,

        @Schema(description = "총 점수", example = "870")
        int totalScore,

        @Schema(description = "현재 티어", example = "GOLD")
        Tier currentTier,

        @Schema(description = "순위", example = "15")
        int rank,

        @Schema(description = "다음 티어", example = "PLATINUM")
        Tier nextTier,

        @Schema(description = "다음 티어까지 남은 점수", example = "130")
        int scoreToNextTier
)
{
    public static RankingResponse from(Ranking ranking) {
        int totalScore = ranking.getTotalScore();
        Tier currentTier = ranking.getTier();
        Tier nextTier = currentTier.nextTier();
        int scoreToNextTier;

        if (currentTier == Tier.MASTER) {
            scoreToNextTier = 0; // 마스터면 더 이상 없음
        } else {
            scoreToNextTier = Math.max(nextTier.getMinScore() - totalScore, 0);
        }

        return new RankingResponse(
                ranking.getUser().getId(),
                ranking.getUser().getNickname(),
                totalScore,
                currentTier,
                ranking.getRank(),
                nextTier,
                scoreToNextTier
        );
    }
}
