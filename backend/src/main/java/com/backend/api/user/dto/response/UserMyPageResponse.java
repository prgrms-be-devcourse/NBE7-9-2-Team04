package com.backend.api.user.dto.response;

import com.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserMyPageResponse {

    private int userId;
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
}