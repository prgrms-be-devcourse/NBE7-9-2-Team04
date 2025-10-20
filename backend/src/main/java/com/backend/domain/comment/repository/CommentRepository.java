package com.backend.domain.comment.repository;

import com.backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment>  findByPostIdOrderByCreateDateDesc(Long postId);
    List<Comment>  findByAuthorIdOrderByCreateDateDesc(Long authorId);
}
