package com.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),

    //사용자 관련
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다."),
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND,"작성자를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일이 존재하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 없습니다"),



    // resume
    DUPLICATE_RESUME(HttpStatus.BAD_REQUEST, "이미 등록된 이력서가 있습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
