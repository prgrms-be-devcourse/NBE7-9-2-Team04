package com.backend.api.post.service;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.api.post.dto.response.PostPageResponse;
import com.backend.api.post.dto.response.PostResponse;
import com.backend.api.user.service.UserService;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostCategoryType;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.domain.subscription.repository.SubscriptionRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public PostResponse createPost(PostAddRequest request, User user) {
        if (!user.validateActiveStatus()) {
            throw new ErrorException(ErrorCode.ACCOUNT_SUSPENDED);
        }

        if(request.pinStatus() == PinStatus.PINNED && !subscriptionRepository.existsByUserAndIsActiveTrue(user)) {
            throw new ErrorException(ErrorCode.PIN_POST_FORBIDDEN);
        }

        Post post = Post.builder()
                .title(request.title())
                .introduction(request.introduction())
                .content(request.content())
                .deadline(request.deadline())
                .status(request.status())
                .pinStatus(request.pinStatus())
                .recruitCount(request.recruitCount())
                .users(user)
                .postCategoryType(request.categoryType())
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }

    public PostResponse getPost(Long postId) {
        Post post = findPostByIdOrThrow(postId);

        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostPageResponse<PostResponse> getAllPosts(int page) {
        if (page < 1) page = 1;
        Pageable pageable = PageRequest.of(page - 1, 9, Sort.by("createDate").descending());

        Page<Post> postsPage  = postRepository.findAll(pageable);

        List<PostResponse> posts = postsPage.getContent()
                .stream()
                .map(PostResponse::from)
                .toList();

        return PostPageResponse.from(postsPage, posts);
    }

    @Transactional(readOnly = true)
    public PostPageResponse<PostResponse> getPostsByUserId(int page, Long userId) {
        User user = userService.getUser(userId);

        if (page < 1) page = 1;
        Pageable pageable = PageRequest.of(page - 1, 15, Sort.by("createDate").descending());
        Page<Post> myPostsPage = postRepository.findByUsers(user, pageable);

        List<PostResponse> myPosts =  myPostsPage
                .getContent()
                .stream()
                .map(PostResponse::from)
                .toList();

        return PostPageResponse.from(myPostsPage, myPosts);
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostUpdateRequest request, User user) {
        Post post = findPostByIdOrThrow(postId);
        validatePostOwner(post, user);

        post.updatePost(request.title(), request.introduction(),request.content(), request.deadline(), request.status(), request.pinStatus(), request.recruitCount(), request.categoryType());

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

    @Transactional(readOnly = true)
    public PostPageResponse<PostResponse> getPostsByCategory(int page, PostCategoryType categoryType) {
        if (page < 1) page = 1;

        Pageable pageable = PageRequest.of(page -1, 9, Sort.by("createDate").descending());
        Page<Post> postsPage = postRepository.findByPostCategoryType(categoryType, pageable);

        if (postsPage.isEmpty()) {
            throw new ErrorException(ErrorCode.POST_NOT_FOUND);
        }

        List<PostResponse> posts =  postsPage.getContent()
                .stream()
                .map(PostResponse::from)
                .toList();

        return PostPageResponse.from(postsPage, posts);
      }
          
    public List<PostResponse> getPinnedPosts() {
        List<Post> pinnedPosts = postRepository.findByPinStatusOrderByCreateDateDesc(PinStatus.PINNED);

        Collections.shuffle(pinnedPosts);
        List<Post> limitedList = pinnedPosts.stream()
                .limit(5)
                .toList();

        return limitedList.stream()
                .map(PostResponse::from)
                .toList();
    }
}
