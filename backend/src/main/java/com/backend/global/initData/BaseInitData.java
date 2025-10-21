package com.backend.global.initData;

import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner initData() {
        return args -> {
            self.initAdminUser();
        };
    }

    @Transactional
    public void initAdminUser() {
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {
            String encodedPassword = passwordEncoder.encode("admin1234");

            User admin = User.builder()
                    .email("admin@naver.com")
                    .password(encodedPassword)
                    .name("관리자")
                    .nickname("admin")
                    .age(30)
                    .github("https://github.com/admin")
                    .image(null)
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }

    }


}
