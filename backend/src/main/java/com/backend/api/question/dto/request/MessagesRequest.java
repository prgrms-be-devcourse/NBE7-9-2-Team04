package com.backend.api.question.dto.request;

public record MessagesRequest(
        String role,
        String content
) {
    public static MessagesRequest of(String role ,String content){
        return new MessagesRequest(role,content);
    }
}
