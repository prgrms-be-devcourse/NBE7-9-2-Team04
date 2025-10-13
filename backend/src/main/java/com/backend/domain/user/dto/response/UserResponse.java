package com.backend.domain.user.dto.response;

import com.backend.domain.user.entity.Users;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String name,
        String nickname,
        Integer age,
        String github,
        String image,
        String role,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
    public UserResponse(Users users) {
        this(
                users.getId(),
                users.getEmail(),
                users.getName(),
                users.getNickname(),
                users.getAge(),
                users.getGithub(),
                users.getImage(),
                users.getRole().name(),
                users.getCreateDate(),
                users.getModifyDate()
        );
    }
}
