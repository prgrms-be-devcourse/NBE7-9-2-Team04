package com.backend.domain.ranking.repository;

import com.backend.domain.ranking.entity.Ranking;
import com.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking,Long> {
    Optional<Ranking> findByUser(User user);

    List<Ranking> findAllByOrderByTotalScoreDesc();
    List<Ranking> findTop10ByOrderByTotalScoreDesc(); //상위 10명만 추출
    List<Ranking> findTop10ByOrderByTotalScoreDescUser_NicknameAsc(); //상위 10명만 추출 + 점수 같으면 이름순
    boolean existsByUser(User user);

    int countByTotalScoreGreaterThan(int totalScore);
}
