package com.backend.api.user.service;

import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(UserSignupRequest request) {

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .nickname(request.nickname())
                .age(request.age())
                .github(request.github())
                .image(request.image())
                .role(User.Role.USER)
                .build();

        return userRepository.save(user);
    }

}
