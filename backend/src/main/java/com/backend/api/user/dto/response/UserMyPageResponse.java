package com.backend.api.user.dto.response;

import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class UserMyPageResponse {

    private long userId;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private int age;
    private String github;
    private String image;

    public static UserMyPageResponse fromEntity(User users){
        return UserMyPageResponse.builder()
                .email(users.getEmail())
                .password(users.getPassword())
                .name(users.getName())
                .nickname(users.getNickname())
                .age(users.getAge())
                .github(users.getGithub())
                .image(users.getImage())
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
        private long title;              // 문제 ID// 문제 제목
        private LocalDateTime modifyDate; // 수정일
    }
}