package com.backend.domain.user.dto.request;

import com.backend.domain.user.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserSignupRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name,

        @NotBlank
        String nickname,

        @NotBlank
        Integer age,

        @NotBlank
        String github,

        String image
) {
    public UserSignupRequest(Users users){
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
