package com.backend.domain.user.repository;

import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByRole(Role role);

    //관리자 제외 조회용 메서드 추가
    Page<User> findAllByRoleNot(Role role, Pageable pageable);
}
