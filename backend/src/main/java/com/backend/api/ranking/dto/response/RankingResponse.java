package com.backend.api.ranking.dto.response;

import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.ranking.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;

public record RankingResponse (
    @Schema(description = "사용자 ID", example = "12")
    Long userId,

    @Schema(description = "닉네임", example = "김개발")
    String nickName,

    @Schema(description = "총 점수", example = "500")
    int totalScore,

    @Schema(description = "티어", example = "GOLD")
    Tier tier,

    @Schema(description = "순위", example = "15")
    int rank
)
{
    public static RankingResponse from(Ranking ranking) {
        return new RankingResponse(
                ranking.getUser().getId(),
                ranking.getUser().getNickname(),
                ranking.getTotalScore(),
                ranking.getTier(),
                ranking.getRank()
        );
    }
}
