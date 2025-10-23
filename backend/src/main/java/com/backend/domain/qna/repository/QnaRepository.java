package com.backend.domain.qna.repository;

import com.backend.domain.qna.entity.Qna;
import com.backend.domain.qna.entity.QnaCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {
    List<Qna> findByCategoryType(QnaCategoryType categoryType);
}
