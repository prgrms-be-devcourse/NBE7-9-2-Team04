package com.backend.api.feedback.service;


import com.backend.api.feedback.dto.request.AiFeedbackRequest;
import com.backend.api.feedback.dto.response.AiFeedbackResponse;
import com.backend.api.feedback.dto.response.FeedbackReadResponse;
import com.backend.api.question.service.QuestionService;
import com.backend.api.ranking.service.RankingService;
import com.backend.api.userQuestion.service.UserQuestionService;
import com.backend.domain.answer.entity.Answer;
import com.backend.domain.answer.repository.AnswerRepository;
import com.backend.domain.feedback.entity.Feedback;
import com.backend.domain.feedback.repository.FeedbackRepository;
import com.backend.domain.question.entity.Question;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final OpenAiChatModel openAiChatModel;  // 리퀘스트 정의
    private final FeedbackRepository feedbackRepository;
    private final QuestionService questionService;

    private final AnswerRepository answerRepository;
    private final UserQuestionService userQuestionService;
    private final RankingService rankingService;

    @Async("feedbackExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void createFeedback(Answer answer){
        Question question = questionService.findByIdOrThrow(answer.getQuestion().getId());
        AiFeedbackResponse aiFeedback = createAiFeedback(question, answer);

        Feedback feedback = Feedback.builder()
                .answer(answer)
                .aiScore(aiFeedback.score())
                .content(aiFeedback.content())
                .build();

        feedbackRepository.save(feedback);

        int baseScore = question.getScore();
        double ratio = aiFeedback.score() / 100.0;
        int finalScore = (int)Math.round(ratio * baseScore);

        userQuestionService.updateUserQuestionScore(answer.getAuthor(), question, finalScore);
        rankingService.updateUserRanking(answer.getAuthor());
        //rankingService.recalculateAllRankings();
    }

    @Async("feedbackExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void updateFeedback(Answer answer){
        Question question = questionService.findByIdOrThrow(answer.getQuestion().getId());
        AiFeedbackResponse aiFeedback = createAiFeedback(question, answer);
        Feedback feedback = getFeedbackByAnswerId(answer.getId());
        feedback.update(answer,aiFeedback.score(),aiFeedback.content());
    }

    public Feedback getFeedbackByAnswerId(Long answerId){
        return feedbackRepository.findByAnswerId(answerId)
                .orElseThrow(() -> new ErrorException(ErrorCode.FEEDBACK_NOT_FOUND));
    }

    private AiFeedbackResponse createAiFeedback(Question question, Answer answer){

        ChatClient chatClient = ChatClient.create(openAiChatModel);
        // 리퀘스트 dto 정의
        AiFeedbackRequest request = AiFeedbackRequest.of(question.getContent(),answer.getContent());

        Prompt prompt = createPrompt(request.systemMessage(), request.userMessage(), request.assistantMessage());

        return chatClient.prompt(prompt)
                .call()
                .entity(AiFeedbackResponse.class);
    }

    private Prompt createPrompt(String system, String user, String assistant){
        // role별 메시지
        SystemMessage systemMessage = new SystemMessage(system);
        UserMessage userMessage = new UserMessage(user);
        AssistantMessage assistantMessage = new AssistantMessage(assistant);

        // 옵션
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .model("gpt-4.1-mini")
                .temperature(1.0)
                .build();

        // 프롬프트
        return new Prompt(List.of(systemMessage, userMessage, assistantMessage), options);
    }

    @Transactional(readOnly = true)

    public FeedbackReadResponse readFeedback(Long questionId,User user) {
        Answer answer = answerRepository.findByQuestionIdAndAuthorId(questionId,user.getId())
                .orElseThrow(() -> new ErrorException(ErrorCode.ANSWER_NOT_FOUND));
        Feedback feedback = getFeedbackByAnswerId(answer.getId());

        return FeedbackReadResponse.from(feedback);
    }

    @Transactional(readOnly = true)
    public Feedback getFeedback(Long id){
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ErrorException(ErrorCode.FEEDBACK_NOT_FOUND));
    }
}
