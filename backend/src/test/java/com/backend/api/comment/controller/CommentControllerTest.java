package com.backend.api.comment.controller;


import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.domain.comment.entity.Comment;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    private User testAuthor; // Controller의 임시 사용자(ID 1L) 역할
    private User testOtherUser; // 권한 없음 테스트에 사용
    private Post testPost1; // 댓글이 달릴 게시글
    private Comment testComment1; // 수정/권한 테스트 대상 댓글 (testAuthor가 작성)

    // 헬퍼 메서드: Post 객체 생성
    private Post createPost(User author, String title) {
        return Post.builder()
                .title(title)
                .content("내용")
                .users(author) // Post 엔티티 필드명 'users'
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .build();
    }

    // 헬퍼 메서드: Comment 객체 생성
    private Comment createComment(Post post, User author, String content) {
        return Comment.builder()
                .content(content)
                .author(author)
                .post(post)
                .build();
    }

    @BeforeEach
    void setupTestData() {
        // 1. 사용자 생성 및 저장 (Controller의 임시 사용자 역할을 대신함)
        testAuthor = userRepository.save(User.builder()
                .email("test1@test.com").password("pw").name("작성자1").nickname("user1").age(20).role(User.Role.USER)
                .github("").build());

        // 2. 다른 사용자 생성 및 저장 (권한 없음 테스트용)
        testOtherUser = userRepository.save(User.builder()
                .email("test2@test.com").password("pw").name("작성자2").nickname("user2").age(30).role(User.Role.USER)
                .github("").build());

        // 3. 게시글 생성 및 저장
        testPost1 = postRepository.save(createPost(testAuthor, "테스트 게시글 1"));

        // 4. 댓글 생성 및 저장 (수정/삭제 테스트용)
        testComment1 = commentRepository.save(createComment(testPost1, testAuthor, "초기 댓글 내용 1"));
    }


    @Test
    @DisplayName("댓글 생성 - 게시글에 생성 성공")
    void t1() throws Exception {

        long targetPostId = testPost1.getId(); // 동적으로 생성된 게시글 ID 사용
        String content = "새로운 댓글";

        // DB에 댓글 생성 전 개수
        long initialCommentCount = commentRepository.count();

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/posts/%d/comments".formatted(targetPostId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "content": "%s"
                                        }
                                        """.formatted(content))
                )
                .andDo(print());

        // DB에 댓글 생성 후 개수 확인
        List<Comment> newComments = commentRepository.findAll();
        assertThat(newComments.size()).isEqualTo(initialCommentCount + 1);

        // 생성된 댓글 정보 동적 확인
        Comment createdComment = newComments.stream()
                .filter(c -> c.getContent().equals(content))
                .filter(c -> c.getPost().getId().equals(targetPostId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("새로 생성된 댓글을 찾을 수 없습니다."));

        long createdCommentId = createdComment.getId();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("createComment"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("%d번 댓글이 생성되었습니다.".formatted(createdCommentId)))
                .andExpect(jsonPath("$.data.id").value(createdCommentId))
                .andExpect(jsonPath("$.data.createDate").exists())
                .andExpect(jsonPath("$.data.modifyDate").exists())
                .andExpect(jsonPath("$.data.content").value(content))
                .andExpect(jsonPath("$.data.authorId").value(createdComment.getAuthor().getId()))
                .andExpect(jsonPath("$.data.authorNickName").value(createdComment.getAuthor().getNickname()))
                .andExpect(jsonPath("$.data.postId").value(targetPostId));
    }


    @Test
    @DisplayName("댓글 수정 - 자신의 댓글 수정 성공")
    void t2() throws Exception {
        long targetPostId = testPost1.getId(); // 동적으로 생성된 게시글 ID 사용
        long targetCommentId = testComment1.getId(); // 동적으로 생성된 댓글 ID 사용
        String content = "댓글 내용 수정"; // 수정할 내용
        long expectedAuthorId = testAuthor.getId(); // 예상 작성자 ID
        String expectedAuthorNickname = testAuthor.getNickname(); // 예상 작성자 닉네임

        // 초기 modifyDate 값 캡처 (수정되기 전의 시간)
        LocalDateTime initialModifyDate = commentRepository.findById(targetCommentId)
                .orElseThrow(() -> new AssertionError("댓글을 찾을 수 없습니다."))
                .getModifyDate();

        // modifiedDate가 초기값보다 확실히 이후가 되도록 보장
        Thread.sleep(100);

        ResultActions resultActions = mvc
                .perform(
                        patch("/api/v1/posts/%d/comments/%d".formatted(targetPostId, targetCommentId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "content": "%s"
                                        }
                                        """.formatted(content))
                )
                .andDo(print());

        // [추가] 응답 본문을 통해 댓글 ID, 게시글 ID, 수정된 내용이 올바른지 검증
        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("updateComment")) // PATCH에 맞는 메서드 이름
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("%d번 댓글이 성공적으로 수정되었습니다.".formatted(targetCommentId)))
                .andExpect(jsonPath("$.data.id").value(targetCommentId))
                .andExpect(jsonPath("$.data.createDate").exists())
                .andExpect(jsonPath("$.data.modifyDate").exists())
                .andExpect(jsonPath("$.data.content").value(content))
                .andExpect(jsonPath("$.data.authorId").value(expectedAuthorId))
                .andExpect(jsonPath("$.data.authorNickName").value(expectedAuthorNickname))
                .andExpect(jsonPath("$.data.postId").value(targetPostId))
                .andExpect(jsonPath("$.data.content").value(content)) // 내용이 수정되었는지 확인
                .andExpect(jsonPath("$.data.authorId").value(expectedAuthorId)); // 작성자 ID가 유지되는지 확인

        commentRepository.flush();

        // DB 확인: 실제로 수정된 내용이 반영되었는지 최종 검증
        Optional<Comment> optionalComment = commentRepository.findById(targetCommentId);
        assertThat(optionalComment).isPresent();
        Comment updatedComment = optionalComment.get();
        assertThat(updatedComment.getContent()).isEqualTo(content); // DB에서 변경내용 확인
        assertThat(updatedComment.getPost().getId()).isEqualTo(targetPostId); // DB에서 Post ID 확인
        assertThat(updatedComment.getAuthor().getId()).isEqualTo(expectedAuthorId); // DB에서 Author ID 확인
        assertThat(updatedComment.getModifyDate()).isAfter(updatedComment.getCreateDate()); // 수정일이 생성일 이후인지 확인
        assertThat(updatedComment.getModifyDate()).isAfter(initialModifyDate); // 수정 날짜가 초기 날짜보다 이후인지 확인
    }

}
