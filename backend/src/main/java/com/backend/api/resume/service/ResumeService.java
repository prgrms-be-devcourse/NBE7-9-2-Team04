package com.backend.api.resume.service;

import com.backend.api.resume.dto.response.ResumeCreateResponse;
import com.backend.domain.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeCreateResponse createResume(Long userId) {

    }
}
