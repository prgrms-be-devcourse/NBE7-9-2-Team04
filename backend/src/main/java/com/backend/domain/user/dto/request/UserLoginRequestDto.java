package com.backend.domain.user.dto.request;

import com.backend.domain.user.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {
    public UserLoginRequestDto(Users users){
        this(
                users.getEmail(),
                users.getPassword()
        );
    }
}
