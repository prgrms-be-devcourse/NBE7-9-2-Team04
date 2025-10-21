package com.backend.api.comment.service;

import com.backend.api.comment.dto.response.CommentMypageResponse;
import com.backend.api.comment.dto.response.CommentResponse;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.post.service.PostService;
import com.backend.api.user.service.UserService;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.Post;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final Rq rq;

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

    public List<CommentResponse> getCommentsByPostId(int page, Long postId) {
        postService.findPostByIdOrThrow(postId);

        if (page < 1) page = 1;
        Pageable pageable = PageRequest.of(page - 1, 15);

        Page<Comment> commentPage  = commentRepository.findByPostIdOrderByCreateDateDesc(pageable, postId);

        return commentPage.getContent()
                .stream()
                .map(CommentResponse::new)
                .toList();
    }

    public List<CommentMypageResponse> getCommentsByUserId(int page, Long userId) {
        userService.getUser(userId);
        if(!rq.getUser().getId().equals(userId)) {
            throw new ErrorException(ErrorCode.COMMENT_INVALID_USER);
        }

        if (page < 1) page = 1;
        Pageable pageable = PageRequest.of(page - 1, 15);

        return commentRepository.findByAuthorIdOrderByCreateDateDesc(pageable, userId).getContent()
                .stream()
                .map(CommentMypageResponse::new)
                .toList();
    }
}