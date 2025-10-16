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
import com.backend.global.security.JwtTokenProvider;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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


    @Autowired // @MockBean에서 @Autowired로 변경
    private JwtTokenProvider jwtTokenProvider;

    private User testUser;
    private User otherUser;
    private Post savedPost;
    private String accessToken;
    private String otherUserAccessToken;

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
        accessToken = jwtTokenProvider.generateAccessToken(testUser.getId(), testUser.getEmail(), testUser.getRole());

        otherUser = User.builder()
                .email("other@test.com").password("pw").name("다른사람").nickname("other").age(20).role(Role.USER)
                .github("").build();
        userRepository.save(otherUser);
        otherUserAccessToken = jwtTokenProvider.generateAccessToken(otherUser.getId(), otherUser.getEmail(), otherUser.getRole());

        Post post = Post.builder()
                .title("기존 제목")
                .content("기존 내용")
                .users(testUser)
                .deadline(FIXED_DEADLINE)
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .build();
        savedPost = postRepository.save(post);
    }

    @Nested
    @DisplayName("게시글 생성 API")
    class CreatePostApiTest {

        @Test
        @DisplayName("게시글 작성 성공")
        void success() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "새로운 게시물",
                    "새로운 내용입니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.data.title").value("새로운 게시물"))
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 인증되지 않은 사용자(로그인 X)")
        void fail1() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "첫번째 게시물",
                    "함께 팀 프로젝트를 진행할 백엔드 개발자 구합니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts") // 인증 헤더 없이 요청
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
        void fail2() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "", // 제목 누락
                    "내용은 있습니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
        void fail3() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "제목은 있습니다.",
                    "", // 내용 누락
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
        void success() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("수정된 제목", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", savedPost.getId())
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 게시글")
        void fail_post_not_found() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("수정된 제목", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", 999L)
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
        void fail_not_owner() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("수정된 제목", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", savedPost.getId())
                            .header("Authorization", "Bearer " + otherUserAccessToken) // 다른 사용자 토큰으로 요청
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
        void fail_title_blank() throws Exception {
            // given
            PostUpdateRequest request = new PostUpdateRequest("", "수정된 내용", FIXED_DEADLINE, PostStatus.CLOSED, PinStatus.PINNED);

            // when
            ResultActions resultActions = mockMvc.perform(
                    put("/api/v1/posts/{postId}", savedPost.getId())
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
        void success() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/posts/{postId}", savedPost.getId())
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
        void fail_post_not_found() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/posts/{postId}", 999L)
                            .header("Authorization", "Bearer " + accessToken) // 헤더 추가
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
        void fail_not_owner() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/v1/posts/{postId}", savedPost.getId())
                            .header("Authorization", "Bearer " + otherUserAccessToken) // 다른 사용자 토큰으로 요청
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
}
