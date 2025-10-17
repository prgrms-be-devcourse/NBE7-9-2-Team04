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

    // 관리자 권한 검증
    public void validateAdminAuthority(User user) {
        if (user == null) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED_USER);
        }
        if (user.getRole() != Role.ADMIN) {
            throw new ErrorException(ErrorCode.FORBIDDEN);
        }
    }

    // 사용자 존재 여부 검증
    private User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(ErrorCode.NOT_FOUND_USER));
    }

    // 전체 사용자 조회
    public List<AdminUserResponse> getAllUsers(User admin) {
        validateAdminAuthority(admin);
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ErrorException(ErrorCode.NOT_FOUND_USER);
        }

        return mapToResponseList(users);
    }

    // 사용자 목록을 응답 DTO 리스트로 매핑
    private List<AdminUserResponse> mapToResponseList(List<User> users) {
        return users.stream()
                .map(AdminUserResponse::from)
                .toList();
    }

    // 특정 사용자 조회
    public AdminUserResponse getUserById(Long userId, User admin) {
        validateAdminAuthority(admin);
        User user = findByIdOrThrow(userId);
        return AdminUserResponse.from(user);
    }

    // 사용자 상태 변경
    @Transactional
    public AdminUserResponse changeUserStatus(Long userId, AdminUserStatusUpdateRequest request, User admin) {
        validateAdminAuthority(admin);
        User user = findByIdOrThrow(userId);

        validateNotDuplicateStatus(user, request.status());
        user.changeStatus(request.status());

        sendStatusChangeMailIfNeeded(user, request.status());
        return AdminUserResponse.from(user);
    }

    // 중복 상태 변경 방지
    private void validateNotDuplicateStatus(User user, AccountStatus newStatus) {
        if (user.getAccountStatus().equals(newStatus)) {
            throw new ErrorException(ErrorCode.DUPLICATE_STATUS);
        }
    }

    // 상태 변경에 따른 이메일 발송
    private void sendStatusChangeMailIfNeeded(User user, AccountStatus status) {
        if (status == AccountStatus.SUSPENDED || status == AccountStatus.BANNED) {
            emailService.sendStatusChangeMail(user);
        }
    }
}
