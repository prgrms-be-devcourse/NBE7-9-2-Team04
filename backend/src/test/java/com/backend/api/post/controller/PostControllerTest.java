package com.backend.api.post.controller;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.api.post.dto.request.PostUpdateRequest;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import com.backend.global.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @MockBean
    private Rq rq;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

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

            given(rq.getUser()).willReturn(testUser);

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
                    .andDo(print());
        }

        @Test
        @DisplayName("실패 - 인증되지 않은 사용자")
        void fail1() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "첫번째 게시물",
                    "함께 팀 프로젝트를 진행할 백엔드 개발자 구합니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            given(rq.getUser()).willThrow(new ErrorException(ErrorCode.UNAUTHORIZED_USER));

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
        void fail2() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "", // 제목 누락
                    "내용은 있습니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            given(rq.getUser()).willReturn(testUser);

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
        void fail3() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "제목은 있습니다.",
                    "", // 내용 누락
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            given(rq.getUser()).willReturn(testUser);

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