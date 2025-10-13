package com.backend.domain.resume.repository;

import com.backend.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Boolean existsByUserId(Long userId);
}
