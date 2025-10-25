package com.backend.api.ranking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//현재 스케줄러는 보조로 사용한다.
@Component
@RequiredArgsConstructor
@Slf4j
public class RankingScheduler {

    private final RankingService rankingService;

    //스케줄링 시간
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void runAutoSubscriptionTask(){
        try {
            rankingService.recalculateAllRankings();
            log.info("랭킹 재계산 완료");
        } catch (Exception e) {
            log.error("랭킹 재계산 중 오류 발생 : {}", e.getMessage(), e);
        }
    }

}
