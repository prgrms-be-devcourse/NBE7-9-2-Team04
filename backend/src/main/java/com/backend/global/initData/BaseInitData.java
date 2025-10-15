package com.backend.global.initData;

import com.backend.api.comment.service.CommentService;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;


@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    private final JdbcTemplate jdbcTemplate;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.userInitData();  // 회원 초기 데이터 등록
            self.postInitData();  // 게시글 초기 데이터 등록
            self.commentInitData();  // 댓글 초기 데이터 등록
        };
    }

    @Transactional
    public void userInitData() {
        if(userRepository.count() > 0) {
            return;
        }
        User generalUser = User.builder()
                .email("general@user.com")
                .password("asdf1234!")
                .name("홍길동")
                .nickname("gildong")
                .age(20)
                .github("abc123")
                .image("imageurl")
                .role(User.Role.USER)
                .build();
        userRepository.save(generalUser);

        User generalUser2 = User.builder()
                .email("general2@user.com")
                .password("asdf1234!")
                .name("홍길똥")
                .nickname("gilddong")
                .age(25)
                .github("abc1233")
                .image("imageurl")
                .role(User.Role.USER)
                .build();
        userRepository.save(generalUser2);
    }

    @Transactional
    public void postInitData() {
        if(postRepository.count() > 0) {
            return;
        }
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .users(userRepository.findById(1L).orElseThrow())
                .build();
        postRepository.save(post);
    }

    @Transactional
    public void commentInitData() {
        if(commentRepository.count() > 0) {
            return;
        }
        commentService.writeComment(userRepository.findById(1L).orElseThrow(),1L, "1번 댓글");
        commentService.writeComment(userRepository.findById(1L).orElseThrow(),1L, "2번 댓글");
        commentService.writeComment(userRepository.findById(1L).orElseThrow(),1L, "3번 댓글");
    }

}
