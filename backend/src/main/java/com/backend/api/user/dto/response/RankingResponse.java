package com.backend.api.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {

    @Schema(description = "내 닉네임")
    private String nickname;

    @Schema(description = "내 총 점수")
    private Integer totalScore;

    @Schema(description = "내가 푼 문제 수")
    private Integer solvedCount;

    @Schema(description = "내 랭킹")
    private Integer myRank;

    @Schema(description = "상위 10명 랭킹 목록")
    private List<RankingEntry> top10;

    @Getter
    @AllArgsConstructor
    public static class RankingEntry {
        @Schema(description = "닉네임")
        private String nickname;

        @Schema(description = "총 점수")
        private Integer totalScore;
    }
}