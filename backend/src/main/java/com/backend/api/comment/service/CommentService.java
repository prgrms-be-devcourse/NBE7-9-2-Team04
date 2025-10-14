package com.backend.api.comment.service;

import com.backend.domain.comment.entity.Comment;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.Post;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment writeComment(User user, Post post, String content) {
        Comment comment = Comment.builder()
                .content(content)
                .author(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(User user, Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ErrorException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }

        comment.updateContent(newContent);

        return comment;
    }

}