package com.backend.domain.user.entity;

import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Builder
@AllArgsConstructor
public class Users extends BaseEntity {

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int age;

    @Column(length = 255, nullable = false)
    private String github;

    @Column(length = 255, nullable = true)
    private String image;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public Users(UserSignupRequest request, String encodedPassword) {
        this.email = request.email();
        this.password = encodedPassword;
        this.name = request.name();
        this.nickname = request.nickname();
        this.age = request.age();
        this.github = request.github();
        this.image = request.image();
        this.role = Role.USER; //기본 권한은 USER로 세팅
    }


    public enum Role {
        USER, ADMIN
    }
    
    

}
