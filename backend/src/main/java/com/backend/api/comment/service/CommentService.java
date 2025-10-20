package com.backend.api.comment.service;

import com.backend.api.comment.dto.response.CommentResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public Comment findByIdOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public Comment writeComment(User currentUser, Long postId, String content) {

        Post post = postService.findPostByIdOrThrow(postId);

        Comment comment = Comment.builder()
                .content(content)
                .author(currentUser)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(User currentUser, Long commentId, String newContent) {
        Comment comment = this.findByIdOrThrow(commentId);

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ErrorException(ErrorCode.COMMENT_INVALID_USER);
        }

        comment.updateContent(newContent);

        return comment;
    }

    @Transactional
    public void deleteComment(User currentUser, Long commentId) {
        Comment comment = this.findByIdOrThrow(commentId);

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new ErrorException(ErrorCode.COMMENT_INVALID_USER);
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = postService.findPostByIdOrThrow(postId);

        return post.getComments().reversed().stream()
                .map(CommentResponse::new)
                .toList();
    }

}