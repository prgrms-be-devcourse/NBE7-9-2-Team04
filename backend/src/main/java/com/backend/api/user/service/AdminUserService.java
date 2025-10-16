package com.backend.api.user.service;

import com.backend.api.user.dto.request.AdminUserStatusUpdateRequest;
import com.backend.api.user.dto.response.AdminUserResponse;
import com.backend.domain.user.entity.AccountStatus;
import com.backend.domain.user.entity.Role;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.repository.UserRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public void CheckAdmin(User user) {
        if (user == null) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED_USER);
        }
        if (user.getRole() != Role.ADMIN) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }
    }

    public List<AdminUserResponse> getAllUsers(User admin) {
        CheckAdmin(admin);
        return userRepository.findAll().stream()
                .map(AdminUserResponse::from)
                .toList();
    }

    public AdminUserResponse getUserById(Long userId, User admin) {
        CheckAdmin(admin);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));

        return new AdminUserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getAge(),
                user.getGithub(),
                user.getImage(),
                user.getRole(),
                user.getAccountStatus()
        );
    }

    @Transactional
    public AdminUserResponse changeUserStatus(Long userId, AdminUserStatusUpdateRequest request, User admin) {
        CheckAdmin(admin);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));

        AccountStatus newStatus = request.status();

        if (user.getAccountStatus().equals(newStatus)) {
            throw new ErrorException(ErrorCode.DUPLICATE_STATUS);
        }

        if (admin.getRole() != Role.ADMIN) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }

        user.changeStatus(newStatus);

        if(request.status() == AccountStatus.SUSPENDED || request.status() == AccountStatus.BANNED) {
            emailService.sendStatusChangeMail(user);
        }

        return AdminUserResponse.from(user);

    }
}
