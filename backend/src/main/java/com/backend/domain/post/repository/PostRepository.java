package com.backend.domain.post.repository;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostCategoryType;
import com.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 사용자가 작성한 게시글을 최신순으로 조회
    List<Post> findByPostCategoryType(PostCategoryType categoryType);

    List<Post> findByUsersOrderByCreateDateDesc(User user);

    List<Post> findAllByOrderByCreateDateDesc();

    List<Post> findByPinStatusOrderByCreateDateDesc(PinStatus pinStatus);
}

