package com.backend.api.post.controller;

import com.backend.api.post.dto.request.PostAddRequest;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    private final LocalDateTime FIXED_DEADLINE = LocalDateTime.now().plusDays(7).withNano(0);


    @BeforeAll
    void setupAll() {
        objectMapper.registerModule(new JavaTimeModule());



        User user = User.builder()
                .email("test1@test.com").password("pw").name("작성자1").nickname("user1").age(20).role(Role.USER)
                .github("").build();
        testUser = userRepository.save(user);
    }

    @Nested
    @DisplayName("게시글 생성 API")
    class CreatePostApiTest {

        @Test
        @DisplayName("게시글 작성 성공")
        void success() throws Exception {
            // given
            final Long EXPECTED_POST_ID = 1L;
            final String TEST_NICKNAME = "user1";
            PostAddRequest request = new PostAddRequest(
                    "첫번째 게시물",
                    "함께 팀 프로젝트를 진행할 백엔드 개발자 구합니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(testUser.getId(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .with(authentication(auth))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            String expectedMessage = "%d번 게시글 등록을 완료했습니다.".formatted(EXPECTED_POST_ID);

            resultActions
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(PostController.class))
                    .andExpect(handler().methodName("createPost"))
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value(expectedMessage))
                    .andExpect(jsonPath("$.data.postId").value(EXPECTED_POST_ID))
                    .andExpect(jsonPath("$.data.title").value(request.title()))
                    .andExpect(jsonPath("$.data.content").value(request.content()))
                    .andExpect(jsonPath("$.data.nickName").value(TEST_NICKNAME))
                    .andExpect(jsonPath("$.data.status").value(PostStatus.ING.name()))
                    .andExpect(jsonPath("$.data.pinStatus").value(PinStatus.NOT_PINNED.name()))
                    .andExpect(jsonPath("$.data.deadline").exists())
                    .andDo(print());
        }


        @Test
        @DisplayName("userId가 존재하지 않을 때")
        void fail1() throws Exception {
            // given
            PostAddRequest request = new PostAddRequest(
                    "첫번째 게시물",
                    "함께 팀 프로젝트를 진행할 백엔드 개발자 구합니다.",
                    FIXED_DEADLINE,
                    PostStatus.ING,
                    PinStatus.NOT_PINNED
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(null, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/api/v1/posts")
                            .with(authentication(auth))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            );

            // then
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("유저를 찾을 수 없습니다."))
                    .andDo(print());
        }
    }
}
