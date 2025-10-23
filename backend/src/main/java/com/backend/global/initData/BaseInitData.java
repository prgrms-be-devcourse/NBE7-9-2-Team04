package com.backend.global.initData;

import com.backend.api.answer.dto.request.AnswerCreateRequest;
import com.backend.api.answer.service.AnswerService;
import com.backend.api.comment.service.CommentService;
import com.backend.domain.answer.repository.AnswerRepository;
import com.backend.domain.comment.repository.CommentRepository;
import com.backend.domain.post.entity.PinStatus;
import com.backend.domain.post.entity.Post;
import com.backend.domain.post.entity.PostCategoryType;
import com.backend.domain.post.entity.PostStatus;
import com.backend.domain.post.repository.PostRepository;
import com.backend.domain.question.entity.Question;
import com.backend.domain.question.repository.QuestionRepository;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerService answerService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.userInitData();  // 회원 초기 데이터 등록
            self.postInitData();  // 게시글 초기 데이터 등록
            self.commentInitData();  // 댓글 초기 데이터 등록
            self.questionInitData();  // 질문 초기 데이터 등록
            self.answerInitData();  // 답변 초기 데이터 등록
            self.initAdminUser();
        };
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

    @Transactional
    public void userInitData() {
        if(userRepository.count() > 0) {
            return;
        }
        User generalUser = User.builder()
                .email("general@user.com")
                .password(passwordEncoder.encode("asdf1234!"))
                .name("홍길동")
                .nickname("gildong")
                .age(20)
                .github("abc123")
                .image(null)
                .role(Role.USER)
                .build();
        userRepository.save(generalUser);

        User generalUser2 = User.builder()
                .email("general2@user.com")
                .password(passwordEncoder.encode("asdf1234!"))
                .name("홍길똥")
                .nickname("gilddong")
                .age(25)
                .github("abc1233")
                .image(null)
                .role(Role.USER)
                .build();
        userRepository.save(generalUser2);
    }

    @Transactional
    public void postInitData() {
        if(postRepository.count() > 0) {
            return;
        }
        Post post1 = Post.builder()
                .title("제목1")
                .introduction("소개")
                .content("내요오오오오오오오옹1")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(userRepository.findById(1L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("제목2")
                .introduction("소개")
                .content("내요오오오오오오오옹2")
                .deadline(LocalDateTime.now().plusDays(7))
                .status(PostStatus.ING)
                .pinStatus(PinStatus.PINNED)
                .recruitCount(5)
                .users(userRepository.findById(2L).orElseThrow())
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
                .users(userRepository.findById(2L).orElseThrow())
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
                .users(userRepository.findById(2L).orElseThrow())
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
                .users(userRepository.findById(2L).orElseThrow())
                .postCategoryType(PostCategoryType.PROJECT)
                .build();
        postRepository.save(post16);
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

    @Transactional
    public void questionInitData() {
        if (questionRepository.count() > 0) {
            return;
        }
        Question question = Question.builder()
                .title("샘플 질문 제목")
                .content("샘플 질문 내용")
                .author(userRepository.findById(1L).orElseThrow())
                .build();
        questionRepository.save(question);
    }

    @Transactional
    public void answerInitData() {
        if (answerRepository.count() > 0) {
            return;
        }
        answerService.writeAnswer(userRepository.findById(1L).orElseThrow(),1L, new AnswerCreateRequest("1번 답변", true));
        answerService.writeAnswer(userRepository.findById(1L).orElseThrow(),1L, new AnswerCreateRequest("2번 답변", false));
        answerService.writeAnswer(userRepository.findById(2L).orElseThrow(),1L, new AnswerCreateRequest("3번 답변", true));
    }
}
