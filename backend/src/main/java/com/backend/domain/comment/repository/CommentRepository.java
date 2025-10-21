package com.backend.domain.comment.repository;

import com.backend.domain.comment.entity.Comment;
import com.backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdOrderByCreateDateDesc(Pageable pageable, Long postId);

    Page<Comment> findByAuthorIdOrderByCreateDateDesc(Pageable pageable, Long authorId);

}
