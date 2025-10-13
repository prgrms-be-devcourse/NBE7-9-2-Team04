package com.backend.domain.user.dto.request;

import com.backend.domain.user.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
    public UserLoginRequest(Users users){
        this(
                users.getEmail(),
                users.getPassword()
        );
    }
}
