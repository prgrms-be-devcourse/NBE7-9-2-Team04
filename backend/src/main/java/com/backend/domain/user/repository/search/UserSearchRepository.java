package com.backend.domain.user.repository.search;

import com.backend.domain.user.entity.search.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, String> {
    List<UserDocument> findByNameContainingOrNicknameContaining(String name, String nickname);
}
