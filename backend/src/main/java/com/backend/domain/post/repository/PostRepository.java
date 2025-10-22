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

    Page<Post> findByPostCategoryType(Pageable pageable, PostCategoryType categoryType);

    Page<Post> findByUsers(Pageable pageable, User user);

    List<Post> findByPinStatusOrderByCreateDateDesc(PinStatus pinStatus);

}

