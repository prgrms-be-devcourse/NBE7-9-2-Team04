package com.backend.api.question.service;

import com.backend.api.question.dto.request.AiQuestionRequest;
import com.backend.api.question.dto.response.AiQuestionResponse;
import com.backend.api.question.dto.response.ChatGptResponse;
import com.backend.api.resume.service.ResumeService;
import com.backend.api.user.service.UserService;
import com.backend.domain.question.entity.Question;
import com.backend.domain.resume.entity.Resume;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.url}")
    private String apiUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final RestClient restClient;
    private final QuestionService questionService;
    private final UserService userService;
    private final ResumeService resumeService;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<AiQuestionResponse> createAiQuestion(Long userId) throws JsonProcessingException {

        User user = userService.getUser(userId);
        Resume resume = resumeService.getResumeByUser(user);
        AiQuestionRequest request = AiQuestionRequest.of(resume.getSkill(),resume.getPortfolioUrl());

        String connectionAi = connectionAi(request);

        List<AiQuestionResponse> responses = parseChatGptResponse(connectionAi);

        List<Question> questions = listDtoToEntity(responses, user, resume);

        questionService.createListQuestion(questions);
        return AiQuestionResponse.toDtoList(questions);
    }

    private List<AiQuestionResponse> parseChatGptResponse(String connectionAi) throws JsonProcessingException {
        ChatGptResponse responseDto = objectMapper.readValue(connectionAi, ChatGptResponse.class);

        String content = responseDto.choiceResponses().get(0).message().content();

        String cleanJson = content
                .replaceAll("```json", "")
                .replaceAll("\\n","")
                .trim();

        return objectMapper.readValue(cleanJson, new TypeReference<>() {});
    }

    private String connectionAi(AiQuestionRequest request){
        return restClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .body(request)
                .retrieve()
                .body(String.class);
    }

    public List<Question> listDtoToEntity(List<AiQuestionResponse> responses, User user, Resume resume){
        return responses.stream()
                .map(dto -> Question.builder()
                        .title(resume.getPortfolioUrl())
                        .content(Optional.ofNullable(dto.content())
                                .filter(s -> !s.isBlank())
                                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_CONTENT)))
                        .score(2)
                        .isApproved(true)
                        .author(user)
                        .build()
                )
                .toList();
    }


}
