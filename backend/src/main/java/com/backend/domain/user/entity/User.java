package com.backend.domain.user.entity;

import com.backend.domain.resume.entity.Resume;
import com.backend.global.entity.BaseEntity;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE; // 기본값 ACTIVE

    @Builder
    public User(String email,
                String password,
                String name,
                String nickname,
                int age,
                String github,
                String image,
                Role role)
    {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.age = age;
        this.github = github;
        this.image = image;
        this.role = role;
    }
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Resume resume;


    public void changeStatus(AccountStatus newStatus) {
        if (newStatus != null && !this.accountStatus.equals(newStatus)) {
            this.accountStatus = newStatus;
        }
    }

    public boolean validateActiveStatus() {
        return this.accountStatus == AccountStatus.ACTIVE;
    }

    public boolean validateLoginAvaliable() {
        return this.accountStatus == AccountStatus.ACTIVE || this.accountStatus == AccountStatus.SUSPENDED;
    }
}
