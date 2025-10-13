package com.backend.api.user.service;

import com.backend.api.user.dto.request.UserSignupRequest;
import com.backend.domain.user.entity.Users;
import com.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Users signUp(UserSignupRequest request) {

        String encodedPassword = passwordEncoder.encode(request.password());
        Users user = new Users(request, encodedPassword);

        return userRepository.save(user);
    }

}
