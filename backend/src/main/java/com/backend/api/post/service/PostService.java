package com.backend.api.post.service;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(PostAddRequest request, User user) {
        if (!user.validateActiveStatus()) {
            throw new ErrorException(ErrorCode.ACCOUNT_SUSPENDED);
        }

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
        Post post = findPostByIdOrThrow(postId);

        return PostResponse.from(post);
    }


    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest request, User user) {
        Post post = findPostByIdOrThrow(postId);
        validatePostOwner(post, user);

        post.updatePost(request.title(), request.content(), request.deadline(), request.status(), request.pinStatus());

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPostByIdOrThrow(postId);
        validatePostOwner(post, user);

        postRepository.delete(post);
    }


    public Post findPostByIdOrThrow(Long postId) { // 중복 로직 헬퍼 메서드1
        return postRepository.findById(postId)
                .orElseThrow(() -> new ErrorException(ErrorCode.POST_NOT_FOUND));
    }

    private void validatePostOwner(Post post, User user) { // 중복 로직 헬퍼 메서드2
        if (!post.getUsers().getId().equals(user.getId())) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }
    }
}
