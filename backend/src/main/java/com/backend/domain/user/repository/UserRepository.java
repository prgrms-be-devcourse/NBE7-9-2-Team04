package com.backend.domain.user.repository;

import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByRole(Role role);

}
