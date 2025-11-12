package com.backend.api.user.service;

import com.backend.domain.user.entity.search.UserDocument;
import com.backend.domain.user.repository.search.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserSearchRepository userSearchRepository;

    public List<UserDocument> search(String keyword) {
        return userSearchRepository.findByNameContainingOrNicknameContaining(keyword, keyword);
    }
}
