package com.backend.api.answer.controller;

import com.backend.domain.answer.repository.AnswerRepository;
import com.backend.domain.answer.entity.Answer;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.Rq.Rq;
import com.backend.global.security.CustomUserDetails;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnswerControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Rq rq;

    // 테스트 데이터 셋업
    @BeforeAll
    @Transactional
    void setUp() {

        User generalUser = User.builder()
                .email("general@user.com")
                .password("asdf1234!")
                .name("홍길동")
                .nickname("gildong")
                .age(20)
                .github("abc123")
                .image(null)
                .role(Role.USER)
                .build();

        User generalUser2 = User.builder()
                .email("general2@user.com")
                .password("asdf1234!")
                .name("홍길똥")
                .nickname("gilddong")
                .age(25)
                .github("abc1233")
                .image(null)
                .role(Role.USER)
                .build();

        userRepository.save(generalUser);
        userRepository.save(generalUser2);

        Question question1 = Question.builder()
                .title("첫 번째 질문 제목")
                .content("첫 번째 질문 내용")
                .author(userRepository.findById(1L).orElseThrow())
                .build();
        questionRepository.save(question1);

        com.backend.domain.answer.entity.Answer answer1 = com.backend.domain.answer.entity.Answer.builder()
                .content("첫 번째 답변 내용")
                .isPublic(true)
                .author(userRepository.findById(1L).orElseThrow())
                .question(question1)
                .build();

        com.backend.domain.answer.entity.Answer answer2 = com.backend.domain.answer.entity.Answer.builder()
                .content("두 번째 답변 내용")
                .isPublic(false)
                .author(userRepository.findById(2L).orElseThrow())
                .question(question1)
                .build();

        com.backend.domain.answer.entity.Answer answer3 = com.backend.domain.answer.entity.Answer.builder()
                .content("세 번째 답변 내용")
                .isPublic(true)
                .author(userRepository.findById(2L).orElseThrow())
                .question(question1)
//                .aiScore(10)
//                .feedback("좋은 답변입니다.")
                .build();

        answerRepository.save(answer1);
        answerRepository.save(answer2);
        answerRepository.save(answer3);
    }

    // 인증 설정
    @BeforeEach
    void setupAuth() {
        User user = userRepository.findById(1L).get();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Nested
    @DisplayName("답변 생성 테스트")
    class t1 {

        @Test
        @DisplayName("1번 질문에 생성 성공")
        void success() throws Exception {
            long targetQuestionId = 1;
            String answer = "새로운 답변 내용입니다.";

            // DB에 답변 생성 전 개수
            long initialAnswerCount = answerRepository.count();

            ResultActions resultActions = mvc
                    .perform(
                            post("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s",
                                            "isPublic": true
                                        }
                                        """.formatted(answer))
                    )
                    .andDo(print());

            // DB에 답변 생성 후 개수 확인
            List<com.backend.domain.answer.entity.Answer> newAnswers = answerRepository.findAll();
            assertThat(newAnswers.size()).isEqualTo(initialAnswerCount + 1);

            // 생성된 답변 정보 동적 확인
            com.backend.domain.answer.entity.Answer createdAnswer = newAnswers.stream()
                    .filter(c -> c.getContent().equals(answer))
                    .filter(c -> c.getQuestion().getId().equals(targetQuestionId))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("새로 생성된 답변을 찾을 수 없습니다."));

            long createdAnswerId = createdAnswer.getId();

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("createAnswer"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("CREATED"))
                    .andExpect(jsonPath("$.message").value("%d번 답변이 생성되었습니다.".formatted(createdAnswerId)))
                    .andExpect(jsonPath("$.data.id").value(createdAnswerId))
                    .andExpect(jsonPath("$.data.createDate").exists())
                    .andExpect(jsonPath("$.data.modifyDate").exists())
                    .andExpect(jsonPath("$.data.content").value(answer))
                    .andExpect(jsonPath("$.data.aiScore").value(Matchers.nullValue()))
                    .andExpect(jsonPath("$.data.isPublic").value(true))
                    .andExpect(jsonPath("$.data.feedback").value(Matchers.nullValue()))
                    .andExpect(jsonPath("$.data.authorId").value(createdAnswer.getAuthor().getId()))
                    .andExpect(jsonPath("$.data.authorNickName").value(createdAnswer.getAuthor().getNickname()))
                    .andExpect(jsonPath("$.data.questionId").value(targetQuestionId));
        }

        @Test
        @DisplayName("필수 필드 누락")
        void fail1() throws Exception {
            long targetQuestionId = 1;

            ResultActions resultActions = mvc
                    .perform(
                            post("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "isPublic": true
                                        }
                                        """.formatted())
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("createAnswer"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("답변 내용을 입력해주세요."));
        }

        @Test
        @DisplayName("필수 필드 누락 2")
        void fail1_2() throws Exception {
            long targetQuestionId = 1;
            String answer = "새로운 답변 내용입니다.";

            ResultActions resultActions = mvc
                    .perform(
                            post("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s"
                                        }
                                        """.formatted(answer))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("createAnswer"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("답변 공개 여부를 선택해주세요."));
        }

        @Test
        @DisplayName("존재하지 않는 질문에 답변 생성 시도")
        void fail2() throws Exception {
            long targetQuestionId = 9999;
            String answer = "새로운 답변 내용입니다.";

            ResultActions resultActions = mvc
                    .perform(
                            post("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s",
                                            "isPublic": true
                                        }
                                        """.formatted(answer))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("createAnswer"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."));
        }

        @Test
        @DisplayName("작성자 정보 저장 확인")
        void success2() throws Exception {
            long targetQuestionId = 1;
            String answer = "새로운 답변 내용입니다.";

            ResultActions resultActions = mvc
                    .perform(
                            post("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s",
                                            "isPublic": true
                                        }
                                        """.formatted(answer))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("createAnswer"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("CREATED"));

            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            Long answerId = JsonPath.parse(responseBody).read("$.data.id", Long.class);

            com.backend.domain.answer.entity.Answer savedAnswer = answerRepository.findById(answerId)
                    .orElseThrow(() -> new RuntimeException("답변이 저장되지 않았습니다."));

            // 작성자 검증
            assertThat(savedAnswer.getAuthor().getId()).isEqualTo(rq.getUser().getId());
        }

    }

    @Nested
    @DisplayName("답변 수정 테스트")
    class t2 {

        @Test
        @DisplayName("1번 답변 수정 성공")
        void success() throws Exception {
            long targetQuestionId = 1; // 동적으로 생성된 질문 ID 사용
            long targetAnswerId = 1; // 동적으로 생성된 답변 ID 사용
            String content = "수정된 답변 내용입니다."; // 수정할 내용
            long expectedAuthorId = 1; // 예상 작성자 ID
            String expectedAuthorNickname = "gildong"; // 예상 작성자 닉네임

            // 초기 modifyDate 값 캡처 (수정되기 전의 시간)
            LocalDateTime initialModifyDate = answerRepository.findById(targetAnswerId)
                    .orElseThrow(() -> new AssertionError("답변을 찾을 수 없습니다."))
                    .getModifyDate();

            // modifiedDate가 초기값보다 확실히 이후가 되도록 보장
            Thread.sleep(100);

            ResultActions resultActions = mvc
                    .perform(
                            patch("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s",
                                            "isPublic": false
                                        }
                                        """.formatted(content))
                    )
                    .andDo(print());

            // [추가] 응답 본문을 통해 답변 ID, 질문 ID, 수정된 내용이 올바른지 검증
            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("updateAnswer")) // PATCH에 맞는 메서드 이름
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("%d번 답변이 수정되었습니다.".formatted(targetAnswerId)))
                    .andExpect(jsonPath("$.data.id").value(targetAnswerId))
                    .andExpect(jsonPath("$.data.createDate").exists())
                    .andExpect(jsonPath("$.data.modifyDate").exists())
                    .andExpect(jsonPath("$.data.content").value(content))
                    .andExpect(jsonPath("$.data.aiScore").value(Matchers.nullValue()))
                    .andExpect(jsonPath("$.data.isPublic").value(false))
                    .andExpect(jsonPath("$.data.feedback").value(Matchers.nullValue()))
                    .andExpect(jsonPath("$.data.authorId").value(expectedAuthorId))
                    .andExpect(jsonPath("$.data.authorNickName").value(expectedAuthorNickname))
                    .andExpect(jsonPath("$.data.questionId").value(targetQuestionId));

            answerRepository.flush();

            // DB 확인: 실제로 수정된 내용이 반영되었는지 최종 검증
            Optional<Answer> optionalAnswer = answerRepository.findById(targetAnswerId);
            assertThat(optionalAnswer).isPresent();
            Answer updatedAnswer = optionalAnswer.get();
            assertThat(updatedAnswer.getContent()).isEqualTo(content); // DB에서 변경내용 확인
            assertThat(updatedAnswer.getQuestion().getId()).isEqualTo(targetQuestionId); // DB에서 Question ID 확인
            assertThat(updatedAnswer.getAuthor().getId()).isEqualTo(expectedAuthorId); // DB에서 Author ID 확인
            assertThat(updatedAnswer.getModifyDate()).isAfter(updatedAnswer.getCreateDate()); // 수정일이 생성일 이후인지 확인
            assertThat(updatedAnswer.getModifyDate()).isAfter(initialModifyDate); // 수정 날짜가 초기 날짜보다 이후인지 확인
        }

        @Test
        @DisplayName("답변 수정 - 다른 작성자의 답변 수정 시도")
        void fail1() throws Exception {
            long targetQuestionId = 1;
            long targetAnswerId = 3;
            String content = "수정된 답변 내용입니다.";

            ResultActions resultActions = mvc
                    .perform(
                            patch("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s",
                                            "isPublic": false
                                        }
                                        """.formatted(content))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("updateAnswer"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("해당 답변에 대한 권한이 없는 사용자입니다."));
        }

        @Test
        @DisplayName("답변 수정 - 없는 답변에 대한 수정 요청 ")
        void fail2() throws Exception {
            long targetQuestionId = 1;
            long targetAnswerId = 4;
            String content = "수정된 답변 내용입니다.";

            ResultActions resultActions = mvc
                    .perform(
                            patch("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                        {
                                            "content": "%s",
                                            "isPublic": false
                                        }
                                        """.formatted(content))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("updateAnswer"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 답변입니다."));
        }

    }

    @Nested
    @DisplayName("답변 삭제 테스트")
    class t3 {

        @Test
        @DisplayName("답변 삭제 - 1번 질문의 1번 답변 삭제")
        void success() throws Exception {
            long targetQuestionId = 1;
            long targetAnswerId = 1;

            ResultActions resultActions = mvc
                    .perform(
                            delete("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                    )
                    .andDo(print());

            // 필수 검증
            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("deleteAnswer"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("%d번 답변이 삭제되었습니다.".formatted(targetAnswerId)));

            // 선택적 검증
            Answer answer = answerRepository.findById(targetAnswerId).orElse(null);
            assertThat(answer).isNull();
        }

        @Test
        @DisplayName("답변 삭제 - 다른 사용자의 답변 삭제 시도")
        void fail1() throws Exception {
            long targetQuestionId = 1;
            long targetAnswerId = 3;

            ResultActions resultActions = mvc
                    .perform(
                            delete("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                    )
                    .andDo(print());

            // 필수 검증
            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("deleteAnswer"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("해당 답변에 대한 권한이 없는 사용자입니다."));

            // 선택적 검증
            Answer answer = answerRepository.findById(targetAnswerId).orElse(null);
            assertThat(answer).isNotNull();
        }

    }

    @Nested
    @DisplayName("답변 목록 조회 테스트")
    class t4 {

        @Test
        @DisplayName("답변 목록 조회, aiScore, feedback 포함")
        void success() throws Exception {

            long targetQuestionId = 1;

            ResultActions resultActions = mvc
                    .perform(
                            get("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("readAnswers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("%d번 질문의 답변 목록 조회 성공".formatted(targetQuestionId)));

            resultActions
                    .andExpect(jsonPath("$.length()").value(3))
                    .andExpect(jsonPath("$.data.answers[*].id", containsInRelativeOrder(3, 1)))
                    .andExpect(jsonPath("$.data.answers[0].id").value(3))
                    .andExpect(jsonPath("$.data.answers[0].createDate").exists())
                    .andExpect(jsonPath("$.data.answers[0].modifyDate").exists())
                    .andExpect(jsonPath("$.data.answers[0].content").value("세 번째 답변 내용"))
                    .andExpect(jsonPath("$.data.answers[0].aiScore").value(10))
                    .andExpect(jsonPath("$.data.answers[0].isPublic").value(true))
                    .andExpect(jsonPath("$.data.answers[0].feedback").value("좋은 답변입니다."))
                    .andExpect(jsonPath("$.data.answers[0].authorId").value(2))
                    .andExpect(jsonPath("$.data.answers[0].authorNickName").value("gilddong"))
                    .andExpect(jsonPath("$.data.answers[0].questionId").value(1))
                    .andExpect(jsonPath("$.data.answers[*].isPublic").value(Matchers.not(Matchers.hasItem(false))))
                    .andExpect(jsonPath("$.data.answers[*].id").value(Matchers.not(Matchers.hasItem(2))));
        }

        @Test
        @DisplayName("답변 목록 조회 - 존재하지 않는 질문")
        void fail1() throws Exception {
            long targetQuestionId = 9999;

            ResultActions resultActions = mvc
                    .perform(
                            get("/api/v1/questions/%d/answers".formatted(targetQuestionId))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("readAnswers"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("답변 단건 조회 테스트")
    class t5 {

        @Test
        @DisplayName("답변 단건 조회")
        void success() throws Exception {

            long targetQuestionId = 1;
            long targetAnswerId = 1;

            ResultActions resultActions = mvc
                    .perform(
                            get("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("readAnswer"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.message").value("%d번 질문의 %d번 답변 조회 성공".formatted(targetQuestionId, targetAnswerId)));

            resultActions
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.createDate").exists())
                    .andExpect(jsonPath("$.data.modifyDate").exists())
                    .andExpect(jsonPath("$.data.content").value("첫 번째 답변 내용"))
                    .andExpect(jsonPath("$.data.aiScore").value(Matchers.nullValue()))
                    .andExpect(jsonPath("$.data.isPublic").value(true))
                    .andExpect(jsonPath("$.data.feedback").value(Matchers.nullValue()))
                    .andExpect(jsonPath("$.data.authorId").value(1))
                    .andExpect(jsonPath("$.data.authorNickName").value("gildong"))
                    .andExpect(jsonPath("$.data.questionId").value(1));
        }

        @Test
        @DisplayName("답변 단건 조회 - 다른 사용자의 비공개 답변 조회 시도")
        void fail1() throws Exception {
            long targetQuestionId = 1;
            long targetAnswerId = 2;

            ResultActions resultActions = mvc
                    .perform(
                            get("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("readAnswer"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("FORBIDDEN"))
                    .andExpect(jsonPath("$.message").value("비공개 답변입니다."));
        }

        @Test
        @DisplayName("답변 단건 조회 - 존재하지 않는 답변 조회 시도")
        void fail2() throws Exception {
            long targetQuestionId = 1;
            long targetAnswerId = 9999;
            ResultActions resultActions = mvc
                    .perform(
                            get("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("readAnswer"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 답변입니다."));
        }

        @Test
        @DisplayName("답변 단건 조회 - 존재하지 않는 질문의 답변 조회 시도")
        void fail3() throws Exception {
            long targetQuestionId = 9999;
            long targetAnswerId = 1;

            ResultActions resultActions = mvc
                    .perform(
                            get("/api/v1/questions/%d/answers/%d".formatted(targetQuestionId, targetAnswerId))
                    )
                    .andDo(print());

            resultActions
                    .andExpect(handler().handlerType(AnswerController.class))
                    .andExpect(handler().methodName("readAnswer"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("질문을 찾을 수 없습니다."));
        }

    }

}
