package com.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND,"작성자를 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다."),
    // resume
    DUPLICATE_RESUME(HttpStatus.BAD_REQUEST, "이미 등록된 이력서가 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
