package com.backend.api.post.service;

import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.user.service.AdminUserService;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPostService {

    private final PostRepository postRepository;
    private final PostService postService;
    private final AdminUserService adminUserService;

    public List<PostResponse> getAllPosts(User admin) {
        adminUserService.validateAdminAuthority(admin);
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse getPostById(Long postId, User user) {
        adminUserService.validateAdminAuthority(user);
        Post post = postService.findPostByIdOrThrow(postId);
        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, User admin) {
        adminUserService.validateAdminAuthority(admin);
        Post post = postService.findPostByIdOrThrow(postId);
        postRepository.delete(post);
    }

    @Transactional
    public PostResponse updatePinStatus(Long postId, User admin, PinStatus status) {
        adminUserService.validateAdminAuthority(admin);
        Post post = postService.findPostByIdOrThrow(postId);
        post.updatePinStatus(status);

        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePostStatus(Long postId, User admin, PostStatus status) {
        adminUserService.validateAdminAuthority(admin);
        Post post =postService.findPostByIdOrThrow(postId);
        post.updateStatus(status);

        return PostResponse.from(post);
    }
}
