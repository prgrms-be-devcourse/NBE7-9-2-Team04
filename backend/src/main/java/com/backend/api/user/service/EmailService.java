package com.backend.api.user.service;

import com.backend.domain.user.entity.AccountStatus;
import com.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendStatusChangeMail(User user) {
        try {
            AccountStatus status = user.getAccountStatus();

            String subject;
            String content;

            switch (status) {
                case SUSPENDED -> {
                    subject = "[계정 정지 안내]";
                    content = """
                            안녕하세요, %s님.
                            
                            회원님의 계정이 현재 '정지 상태'로 전환되었습니다.
                            정책 위반 혹은 신고 누적으로 인한 조치일 수 있습니다.
                            
                            자세한 내용은 관리자에게 문의 바랍니다.
                            문의: support@devStation.com
                            """.formatted(user.getName());
                }
                case DEACTIVATED -> {
                    subject = "[회원 탈퇴 완료 안내]";
                    content = """
                            안녕하세요, %s님.
                            
                            회원님의 계정 탈퇴 처리가 완료되었습니다.
                            탈퇴 이후에도 일정 기간 동안 개인정보가 보관될 수 있습니다.
                            
                            다시 서비스를 이용하시려면 재가입을 진행해주세요.
                            """.formatted(user.getName());
                }
                case BANNED -> {
                    subject = "[계정 영구 정지 안내]";
                    content = """
                            안녕하세요, %s님.
                            
                            회원님의 계정이 '영구 정지' 처리되었습니다.
                            중대한 정책 위반으로 인해 재가입이 제한됩니다.
                            
                            문의가 필요하신 경우 support@devStation.com 으로 연락 바랍니다.
                            """.formatted(user.getName());
                }
                case ACTIVE -> {
                    subject = "[계정 복구 안내]";
                    content = """
                            안녕하세요, %s님.
                            
                            회원님의 계정이 정상 상태로 복구되었습니다.
                            지금부터 정상적으로 로그인 및 활동이 가능합니다.
                            """.formatted(user.getName());
                }
                default -> {
                    log.info("이메일 전송 대상 상태가 아닙니다: {}", status);
                    return;
                }
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            log.info("[{}] 상태 변경 메일 전송 완료: {}", status, user.getEmail());

        } catch (Exception e) {
            log.error("이메일 전송 실패 (userId={}, email={}): {}",
                    user.getId(), user.getEmail(), e.getMessage(), e);
        }
    }
}
