package com.backend.global.exception;

import com.backend.global.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 커스텀 예외 처리
    @ExceptionHandler(ErrorException.class)
    protected ResponseEntity<ApiResponse<?>> handleCustomException(ErrorException e) {
        log.error("ErrorException: {} - {}", e.getErrorCode().name(), e.getMessage(), e);
        return new ResponseEntity<>(
                ApiResponse.fail(e)
                , e.getErrorCode().getHttpStatus());
    }

    // @Valid 유효성 검사 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldError().getDefaultMessage();

        log.warn("MethodArgumentNotValidException {}", e.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(HttpStatus.BAD_REQUEST,message)
                , HttpStatus.BAD_REQUEST);
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAllException(final Exception e) {
        log.error("handleAllException {}", e.getMessage(), e);
        return new ResponseEntity<>(
                ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
