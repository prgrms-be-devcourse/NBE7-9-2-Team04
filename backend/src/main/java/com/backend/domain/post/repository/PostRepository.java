package com.backend.domain.post.repository;

import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostCategoryType;
import com.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByPostCategoryTypeOrderByCreateDateDesc(Pageable pageable, PostCategoryType categoryType);

    Page<Post> findAllByOrderByCreateDateDesc(Pageable pageable);

    // 특정 사용자가 작성한 게시글을 최신순으로 조회
    Page<Post> findByUsersOrderByCreateDateDesc(Pageable pageable, User user);

    List<Post> findByPinStatusOrderByCreateDateDesc(PinStatus pinStatus);

}

