package com.backend.global.jwt;

import com.backend.api.user.service.UserDetailsService;
import com.backend.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    @Value("${custom.jwt.secretPattern}")
    private String secretPattern;

    @Value("${custom.jwt.access-token.expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${custom.jwt.refresh-token.expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretPattern.getBytes(StandardCharsets.UTF_8));
    }


    //토큰 공통 생성 로직
    public String generateToken(String email, Role role, long expireTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime); //만료시간 설정

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("role", role.name())
                .signWith(key)
                .compact();
    }

    //AccessToken 생성 (Role 포함)
    public String generateAccessToken(String email, Role role) {
        return generateToken(email, role, ACCESS_TOKEN_EXPIRE_TIME);
    }

    //RefreshToken 생성 (Role 포함)
    public String generateRefreshToken(String email, Role role) {
         return generateToken(email, role, REFRESH_TOKEN_EXPIRE_TIME);
    }

    //claims 파싱
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public Role getRoleFromToken(String token){
        Object role = parseClaims(token).get("role");
        return Role.valueOf(role.toString());
    }

//    public Authentication getAuthentication(String token) { 추후 수정 예정
//        String email = getEmailFromToken(token);
//        UserDetails user = userDetailsService.loadUserByUserName(email);
//        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
//
//    }

    //유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
