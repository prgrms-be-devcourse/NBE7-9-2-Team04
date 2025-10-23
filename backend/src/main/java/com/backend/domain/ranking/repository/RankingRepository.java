package com.backend.domain.ranking.repository;

import com.backend.domain.ranking.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking,Long> {
}
