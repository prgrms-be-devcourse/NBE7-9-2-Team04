package com.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND,"작성자를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
