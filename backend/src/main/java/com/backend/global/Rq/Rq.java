package com.backend.global.Rq;

import com.backend.domain.user.entity.User;
import com.backend.global.security.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Rq {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public User getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //인증 객체 존재 및 principal이 CustomUserDetail일때
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            throw new IllegalStateException("로그인된 사용자가 없습니다.");
        } //예외 처리 수정 필요

        return details.getUser();
    }

    public void setCookie(String name, String value, int maxAge) {
        if (value == null) value = "";

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Strict");

        if (value.isBlank()) {
            cookie.setMaxAge(0);
        }

        response.addCookie(cookie);
    }

    public void deleteCookie(String name) {
        setCookie(name, "", 0);
    }
}