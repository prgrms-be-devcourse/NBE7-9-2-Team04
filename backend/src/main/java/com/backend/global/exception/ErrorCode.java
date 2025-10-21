package com.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  
    //사용자 관련
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    NOT_FOUND_NICKNAME(HttpStatus.NOT_FOUND, "작성자를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "이메일이 존재하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인된 사용자가 없습니다."),

    //token
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."),


    // Post & Comment
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    COMMENT_INVALID_USER(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),

    //Question
    QUESTION_TITLE_NOT_BLANK(HttpStatus.BAD_REQUEST, "질문 제목은 공백일 수 없습니다."),
    QUESTION_CONTENT_NOT_BLANK(HttpStatus.BAD_REQUEST, "질문 내용은 공백일 수 없습니다."),
    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, "질문을 찾을 수 없습니다."),
    INVALID_QUESTION_SCORE(HttpStatus.BAD_REQUEST, "질문 점수는 음수일 수 없습니다."),
    QUESTION_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 질문입니다."),
    ALREADY_APPROVED_QUESTION(HttpStatus.BAD_REQUEST, "이미 승인된 질문입니다."),
    QUESTION_NOT_APPROVED(HttpStatus.FORBIDDEN, "승인되지 않은 질문입니다."),
    NOT_FOUND_CONTENT(HttpStatus.NOT_FOUND,"질문 내용을 찾을 수 없습니다."),

    // resume
    DUPLICATE_RESUME(HttpStatus.BAD_REQUEST, "이미 등록된 이력서가 있습니다."),
    NOT_FOUND_RESUME(HttpStatus.NOT_FOUND, "이력서를 찾을 수 없습니다."),
    INVALID_USER(HttpStatus.FORBIDDEN, "이력서 수정 권한이 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
