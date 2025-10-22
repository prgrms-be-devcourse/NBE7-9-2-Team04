package com.backend.api.user.controller;

import com.backend.api.user.dto.response.RankingResponse;
import com.backend.api.user.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ranking", description = "유저 랭킹 API")
@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "랭킹")
    @GetMapping
    public ResponseEntity<RankingResponse> getMyRanking() {
        RankingResponse response = rankingService.getRankingForCurrentUser();
        return ResponseEntity.ok(response);
    }
}
