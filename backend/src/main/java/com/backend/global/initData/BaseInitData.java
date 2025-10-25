package com.backend.global.initData;

import com.backend.api.answer.dto.request.AnswerCreateRequest;
import com.backend.api.answer.service.AnswerService;
import com.backend.api.comment.service.CommentService;
import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.api.user.service.EmailService;
import com.backend.api.user.service.UserService;
import com.backend.domain.answer.repository.AnswerRepository;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostCategoryType;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.qna.entity.Qna;
import com.backend.domain.qna.entity.QnaCategoryType;
import com.backend.domain.qna.repository.QnaRepository;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.entity.VerificationCode;
import com.backend.domain.user.repository.UserRepository;
import com.backend.domain.user.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;


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
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QnaRepository qnaRepository;
    private final AnswerService answerService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserService userService;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.userInitData();  // 회원 초기 데이터 등록
            self.postInitData();  // 게시글 초기 데이터 등록
            self.questionInitData();  // 질문 초기 데이터 등록
            self.answerInitData();  // 답변 초기 데이터 등록
            self.qnaInitData(); // QnA 초기 데이터 등록
            self.initAdminUser();
        };
    }

    @Transactional
    public void userInitData() {
        if(userRepository.count() > 0) {
            return;
        }

        // 15개의 이메일 리스트
        List<String> emails = IntStream.rangeClosed(1, 15)
                .mapToObj(i -> "user" + i + "@test.com")
                .toList();

        for (int i = 0; i < emails.size(); i++) {
            String email = emails.get(i);

            // 1️⃣ 이메일 인증 코드 더미 데이터 생성
            VerificationCode verification = VerificationCode.builder()
                    .email(email)
                    .code("INITOK" + i) // 임의의 더미 코드
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .verified(true) // ✅ 인증 완료 상태로 저장
                    .build();
            verificationCodeRepository.save(verification);

            // 2️⃣ 회원가입 요청 객체 생성
            UserSignupRequest request = new UserSignupRequest(
                    email,
                    "abc12345",                              // 비밀번호
                    "홍길동" + (i + 1),                       // 이름
                    "user" + (i + 1),                        // 닉네임
                    20 + (i % 5),                            // 나이 (20~24)
                    "https://github.com/user" + (i + 1),     // 깃허브
                    null                                     // 프로필 이미지 없음
            );

            userService.signUp(request);
        }
//        User generalUser = User.builder()
//                .email("general@user.com")
//                .password(passwordEncoder.encode("asdf1234!"))
//                .name("홍길동")
//                .nickname("gildong")
//                .age(20)
//                .github("abc123")
//                .image(null)
//                .role(Role.USER)
//                .build();
//        userRepository.save(generalUser);
//
//        User generalUser2 = User.builder()
//                .email("general2@user.com")
//                .password(passwordEncoder.encode("asdf1234!"))
//                .name("홍길똥")
//                .nickname("gilddong")
//                .age(25)
//                .github("abc1233")
//                .image(null)
//                .role(Role.USER)
//                .build();
//        userRepository.save(generalUser2);
    }

    @Transactional
    public void postInitData() {
        if(postRepository.count() > 0) {
            return;
        }

        User user1 = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User 1L not found for Post Init"));
        User user2 = userRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("User 2L not found for Post Init"));



        Post post1 = Post.builder()
                .title("제목1")
                .introduction("소개")
                .content("내요오오오오오오오옹1")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(user1)
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post1);


        commentService.writeComment(user1, post1.getId(), "1번 댓글");
        commentService.writeComment(user1, post1.getId(), "2번 댓글");
        commentService.writeComment(user1, post1.getId(), "3번 댓글");



        Post post2 = Post.builder()
                .title("제목2")
                .introduction("소개")
                .content("내요오오오오오오오옹2")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(user2)
                .postCategoryType(PostCategoryType.STUDY)
                .build();
        postRepository.save(post2);

        Post post3 = Post.builder()
                .title("제목3")
                .introduction("소개")
                .content("내요오오오오오오오옹3")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(user2)
                .postCategoryType(PostCategoryType.STUDY)
                .build();
        postRepository.save(post3);

        Post post4 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(user2)
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post4);
        Post post5 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post5);

        Post post6 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post6);

        Post post7 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.CLOSED)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post7);

        Post post8 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post8);

        Post post9 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post9);

        Post post10 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post10);

        Post post11 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post11);

        Post post12 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post12);

        Post post13 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post13);

        Post post14 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post14);

        Post post15 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post15);

        Post post16 = Post.builder()
                .title("제목")
                .introduction("소개")
                .content("내요오오오오오오오옹")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.NOT_PINNED)
                .recruitCount(5)
                .users(user2)
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post16);
    }

    @Transactional
    public void questionInitData() {
        if (questionRepository.count() > 0) {
            return;
        }
        User user1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User 1L not found for Question Init"));

        Question question = Question.builder()
                .title("샘플 질문 제목")
                .content("샘플 질문 내용")
                .author(user1)
                .build();
        questionRepository.save(question);

        Question q1 = Question.builder()
                .title("운영체제에서 프로세스와 스레드의 차이점은 무엇인가요?")
                .content("프로세스와 스레드는 모두 실행 단위를 나타내지만, 메모리 구조나 자원 공유 방식이 다릅니다. 각각의 차이점을 설명해주세요.")
                .author(user1)
                .score(30)
                .build();
        questionRepository.save(q1);

        Question q2 = Question.builder()
                .title("데이터베이스에서 인덱스(Index)는 어떤 역할을 하나요?")
                .content("인덱스를 사용하면 검색 속도가 빨라지지만, 삽입/삭제 시 오버헤드가 생깁니다. 인덱스의 동작 원리와 장단점을 설명해주세요.")
                .author(user1)
                .score(50)
                .build();
        questionRepository.save(q2);

        Question q3 = Question.builder()
                .title("TCP와 UDP의 차이점을 설명해주세요.")
                .content("두 프로토콜의 연결 방식, 신뢰성, 속도 측면에서의 차이와 각각이 주로 사용되는 사례를 알려주세요.")
                .author(user1)
                .score(40)
                .build();
        questionRepository.save(q3);

        Question q4 = Question.builder()
                .title("자바의 Garbage Collection은 어떻게 동작하나요?")
                .content("GC의 기본 원리와 주요 알고리즘(Mark and Sweep, Generational GC 등)을 설명하고, 성능 최적화 방법에 대해 설명해주세요.")
                .author(user1)
                .score(70)
                .build();
        questionRepository.save(q4);

        Question q5 = Question.builder()
                .title("데드락(Deadlock)은 어떤 상황에서 발생하나요?")
                .content("데드락의 4가지 필요 조건과, 이를 예방하거나 해결할 수 있는 방법을 구체적으로 설명해주세요.")
                .author(user1)
                .score(10)
                .build();
        questionRepository.save(q5);
    }

    @Transactional
    public void answerInitData() {
        if (answerRepository.count() > 0) {
            return;
        }
        User user1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User 1L not found for Answer Init"));
        User user2 = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("User 2L not found for Answer Init"));


        Question firstQuestion = questionRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No Question found for Answer Init"));
        Long questionId = firstQuestion.getId();

        answerService.writeAnswer(user1, questionId, new AnswerCreateRequest("1번 답변", true));
        answerService.writeAnswer(user1, questionId, new AnswerCreateRequest("2번 답변", false));
        answerService.writeAnswer(user2, questionId, new AnswerCreateRequest("3번 답변", true));
    }

    @Transactional
    public void qnaInitData() {
        if (qnaRepository.count() > 0) {
            return;
        }

        User user1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User 1L not found for Qna Init"));
        User user2 = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("User 2L not found for Qna Init"));

        Qna qna1 = Qna.builder()
                .title("로그인이 자꾸 실패합니다.")
                .content("회원가입은 정상적으로 되었는데, 로그인 시 '이메일 또는 비밀번호가 올바르지 않습니다'라는 문구가 계속 나옵니다.")
                .author(user1)
                .categoryType(QnaCategoryType.ACCOUNT)
                .build();

        Qna qna2 = Qna.builder()
                .title("프리미엄 멤버십 결제 관련 문의드립니다.")
                .content("결제 완료 후에도 프리미엄 기능이 활성화되지 않습니다.")
                .author(user2)
                .categoryType(QnaCategoryType.PAYMENT)
                .build();

        Qna qna3 = Qna.builder()
                .title("사이트 접속 시 오류가 발생합니다.")
                .content("크롬 브라우저에서 접속 시 자꾸 500 오류가 뜹니다.")
                .author(user1)
                .categoryType(QnaCategoryType.SYSTEM)
                .build();

        Qna qna4 = Qna.builder()
                .title("팀 모집글 관련 문의드립니다.")
                .content("모집글 작성 시 마감일을 수정할 수 있나요?")
                .author(user2)
                .categoryType(QnaCategoryType.RECRUITMENT)
                .build();

        Qna qna5 = Qna.builder()
                .title("사이트 개선 제안드립니다.")
                .content("Q&A 게시판에 검색 기능이 추가되면 좋겠습니다.")
                .author(user1)
                .categoryType(QnaCategoryType.SUGGESTION)
                .build();

        Qna qna6 = Qna.builder()
                .title("기타 문의드립니다.")
                .content("회원탈퇴는 어디서 하나요?")
                .author(user2)
                .categoryType(QnaCategoryType.OTHER)
                .build();

        qnaRepository.save(qna1);
        qnaRepository.save(qna2);
        qnaRepository.save(qna3);
        qnaRepository.save(qna4);
        qnaRepository.save(qna5);
        qnaRepository.save(qna6);
    }

    @Transactional
    public void initAdminUser() {
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {
            String encodedPassword = passwordEncoder.encode("admin1234");

            User admin = User.builder()
                    .email("admin@naver.com")
                    .password(encodedPassword)
                    .name("관리자")
                    .nickname("admin")
                    .age(30)
                    .github("https://github.com/admin")
                    .image(null)
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}