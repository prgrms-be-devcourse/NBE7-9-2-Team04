package com.backend.api.user.dto.response;

import com.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserMyPageResponse {

    private Long userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private int age;
    private String github;
    private String image;
    private Integer rank;
    private Integer solvedCount;
    private Integer totalScore;


    public static UserMyPageResponse fromEntity(User users, Integer rank, Integer solvedCount, Integer totalScore){
        return UserMyPageResponse.builder()
                .userId(users.getId())
                .email(users.getEmail())
                .password(users.getPassword())
                .name(users.getName())
                .nickname(users.getNickname())
                .age(users.getAge())
                .github(users.getGithub())
                .image(users.getImage())
                .rank(rank)
                .solvedCount(solvedCount)
                .totalScore(totalScore)
                .build();
    }


    @Getter
    public static class UserModify {
        private String email;
        private String password;
        private String name;
        private String nickname;
        private int age;
        private String github;
        private String image;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SolvedProblem {
        private String title;              // 문제 제목
        private LocalDateTime modifyDate; // 수정일
    }
}