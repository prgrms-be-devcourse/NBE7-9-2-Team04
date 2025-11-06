package com.backend.api.ranking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

//현재 스케줄러는 보조로 사용한다. 
//답변 제출할때마다 실시간으로 재계산하도록 함
@Component
@RequiredArgsConstructor
@Slf4j
public class RankingScheduler {

    private final RankingService rankingService;

    //스케줄링 시간
    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    public void runAutoSubscriptionTask(){

        log.info("[스케줄러 시작] 랭킹 계산 시작 - {}", LocalDate.now());
        rankingService.recalculateAllRankings();

        log.info("[스케줄러 완료] 랭킹 계산 종료");
    }

}
