package com.backend.api.comment.service;

import com.backend.api.post.service.PostService;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.Post;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public Comment writeComment(User user, Long postId, String content) {

        if (!user.validateActiveStatus()) {
            throw new ErrorException(ErrorCode.ACCOUNT_SUSPENDED);
        }

        Post post = postService.findPostByIdOrThrow(postId);

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
            throw new ErrorException(ErrorCode.COMMENT_INVALID_USER);
        }

        comment.updateContent(newContent);

        return comment;
    }

    @Transactional
    public void deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ErrorException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ErrorException(ErrorCode.COMMENT_INVALID_USER);
        }

        commentRepository.delete(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

}