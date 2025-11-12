package com.backend.api.search.service;


import com.backend.api.search.dto.SearchResultDto;
import com.backend.domain.user.repository.search.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GlobalSearchService {

    private final UserSearchRepository userSearchRepository;

    public List<SearchResultDto> searchAll(String keyword) {
        List<SearchResultDto> results = new ArrayList<>();

        // 유저 검색
        userSearchRepository.findByNameContainingOrNicknameContaining(keyword, keyword)
                .forEach(u -> results.add(SearchResultDto.builder()
                        .type("user")
                        .id(u.getId())
                        .title(u.getName())
                        .snippet(u.getNickname())
                        .build()));

        return results;
    }
}
