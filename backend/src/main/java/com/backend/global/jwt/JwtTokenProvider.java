package com.backend.global.jwt;

import com.backend.domain.user.entity.Role;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${custom.jwt.secretPattern}")
    private String secretPattern;

    @Value("${custom.jwt.access-token.expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${custom.jwt.refresh-token.expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    private Key key;

    //
    @PostConstruct
    public void init() {
        //디코딩한 바이트 시퀀스 사용
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretPattern));
    }

    //AccessToken 생성 (Role 포함)
    public String generateAccessToken(String email, Role role) {}


}
