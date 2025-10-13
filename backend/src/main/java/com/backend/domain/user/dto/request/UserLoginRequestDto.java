package com.backend.domain.user.dto.request;

import com.backend.domain.user.entity.Users;

public record UserLoginRequestDto(
        String email,
        String password
) {
    public UserLoginRequestDto(Users users){
        this(
                users.getEmail(),
                users.getPassword()
        );
    }
}
