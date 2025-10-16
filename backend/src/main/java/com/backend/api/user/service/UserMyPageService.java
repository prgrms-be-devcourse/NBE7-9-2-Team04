package com.backend.api.user.service;

import com.backend.api.user.dto.response.UserMyPageResponse;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMyPageService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserMyPageResponse modifyUser(Long userId, UserMyPageResponse.UserModify modify) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.updateUser(
                modify.getEmail(),
                passwordEncoder.encode(modify.getPassword()),
                modify.getName(),
                modify.getNickname(),
                modify.getAge(),
                modify.getGithub(),
                modify.getImage()
        );

        User saved = userRepository.save(user);
        return UserMyPageResponse.fromEntity(saved);
    }

    public UserMyPageResponse getInformation(Long userId){
        User users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return UserMyPageResponse.fromEntity(users);
    }

}
