package com.backend.api.post.controller;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User otherUser;
    private Post savedPost;

    private final LocalDateTime FIXED_DEADLINE = LocalDateTime.now().plusDays(7).withNano(0);

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        postRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .email("test1@test.com").password("pw").name("작성자1").nickname("user1").age(20).role(Role.USER)
                .github("").build();
        userRepository.save(testUser);

        otherUser = User.builder()
                .email("other@test.com").password("pw").name("다른사람").nickname("other").age(20).role(Role.USER)
                .github("").build();
        userRepository.save(otherUser);

        Post post = Post.builder()
                .title("기존 제목")
                .content("기존 내용")
                .users(testUser)
                .deadline(FIXED_DEADLINE)
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(4)
                .build();
        savedPost = postRepository.save(post);
    }

    @Nested
    @DisplayName("게시글 생성 API")
    class CreatePostApiTest {

        @Test
        @DisplayName("게시글 작성 성공")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void success() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "새로운 게시물",
                    "새로운 내용입니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED,
                    5
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.data.title").value("새로운 게시물"))
                    .andExpect(jsonPath("$.data.recruitCount").value(5))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 인증되지 않은 사용자(로그인 X)")
        @WithAnonymousUser
        void fail1() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "첫번째 게시물",
                    "함께 팀 프로젝트를 진행할 백엔드 개발자 구합니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED,
                    4
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                    .andExpect(jsonPath("$.message").value("로그인된 사용자가 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 제목 누락")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail2() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "", // 제목 누락
                    "내용은 있습니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED,
                    4
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("제목은 필수입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 내용 누락")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail3() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "제목은 있습니다.",
                    "", // 내용 누락
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED,
                    4
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("내용은 필수입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 수정 API")
    class UpdatePostApiTest {

        @Test
        @DisplayName("게시글 수정 성공")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void success() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("수정된 제목", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED, 10);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", savedPost.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.data.title").value("수정된 제목"))
                    .andExpect(jsonPath("$.data.content").value("수정된 내용"))
                    .andExpect(jsonPath("$.data.status").value(PostStatus.CLOSED.name()))
                    .andExpect(jsonPath("$.data.pinStatus").value(PinStatus.PINNED.name()))
                    .andExpect(jsonPath("$.data.recruitCount").value(10))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 게시글")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail_post_not_found() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("수정된 제목", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED, 10);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", 999L)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 작성자가 아님")
        @WithUserDetails(value = "other@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail_not_owner() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("수정된 제목", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED, 10);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", savedPost.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 제목 누락")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail_title_blank() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED, 10);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", savedPost.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("제목은 필수입니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 API")
    class DeletePostApiTest {

        @Test
        @DisplayName("게시글 삭제 성공")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void success() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/posts/{postId}", savedPost.getId())
                            .accept(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("게시글 삭제가 완료되었습니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 게시글")
        @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail_post_not_found() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/posts/{postId}", 999L)
                            .accept(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 작성자가 아님")
        @WithUserDetails(value = "other@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
        void fail_not_owner() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/posts/{postId}", savedPost.getId())
                            .accept(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("접근 권한이 없습니다."))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 단건 조회 API")
    class GetPostApiTest {

        @Test
        @DisplayName("게시글 조회 성공")
        @WithAnonymousUser
        void success() throws Exception {
            // given
            Long postId = savedPost.getId();

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/posts/{postId}", postId)
                            .accept(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("%d번 게시글을 성공적으로 조회했습니다.".formatted(postId)))
                    .andExpect(jsonPath("$.data.postId").value(postId))
                    .andExpect(jsonPath("$.data.title").value("기존 제목"))
                    .andExpect(jsonPath("$.data.content").value("기존 내용"))
                    .andExpect(jsonPath("$.data.recruitCount").value(4))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 게시글")
        @WithAnonymousUser
        void fail_post_not_found() throws Exception {
            // given
            Long nonExistentPostId = 999L;

            // when
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/posts/{postId}", nonExistentPostId)
                            .accept(MediaType.APPLICATION_JSON)
            );

            // then
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 게시글입니다."))
                    .andDo(print());
        }
    }
}
