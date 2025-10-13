package com.backend.domain.user.dto.request;

import com.backend.domain.user.entity.Users;

public record UserSignupRequestDto(
        String email,
        String password,
        String name,
        String nickname,
        Integer age,
        String github,
        String image
) {
    public UserSignupRequestDto(Users users){
        this(
                users.getEmail(),
                users.getPassword(),
                users.getName(),
                users.getNickname(),
                users.getAge(),
                users.getGithub(),
                users.getImage()
        );
    }
}
