package com.backend.api.post.service;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.user.service.UserService;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public PostResponse createPost(PostAddRequest request, Long userId) {

        if (userId == null) {
            throw new ErrorException(ErrorCode.NOT_FOUND_USER);
        }

        User user = userService.getUser(userId);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .deadline(request.deadline())
                .status(request.status())
                .pinStatus(request.pinStatus())
                .users(user)
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }

    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest request, Long userId) {

        if (userId == null) {
            throw new ErrorException(ErrorCode.NOT_FOUND_USER);
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUsers().getId().equals(userId)) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }
        post.updatePost(request.title(), request.content(), request.deadline(), request.status(), request.pinStatus());

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUsers().getId().equals(userId)) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }

        postRepository.delete(post);
    }
}
