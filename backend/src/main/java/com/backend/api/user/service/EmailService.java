package com.backend.api.user.service;

import com.backend.domain.user.entity.AccountStatus;
import com.backend.domain.user.entity.User;
import com.backend.domain.user.entity.VerificationCode;
import com.backend.domain.user.repository.UserRepository;
import com.backend.domain.user.repository.VerificationCodeRepository;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;

    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    public void sendVerificationCode(String email) {
        try {
            if(userRepository.findByEmail(email).isPresent()) {
                throw new ErrorException(ErrorCode.DUPLICATE_EMAIL);
            }

            // 기존 코드 삭제 후 재발급
            verificationCodeRepository.findByEmail(email)
                    .ifPresent(verificationCodeRepository::delete);

            String code = generateVerificationCode();

            VerificationCode verification = VerificationCode.builder()
                    .email(email)
                    .code(code)
                    .expiresAt(LocalDateTime.now().plusMinutes(5))
                    .verified(false)
                    .build();

            verificationCodeRepository.save(verification);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[Dev-Station] 이메일 인증 코드");
            message.setText("""
                    안녕하세요. Dev-Station 입니다.

                    회원가입을 위해 아래 인증 코드를 입력해주세요.
                    인증코드: %s

                    해당 코드는 5분간 유효합니다.
                    """.formatted(code));

            mailSender.send(message);
            log.info("[이메일 인증] 인증코드 전송 완료: {}", email);

        } catch (ErrorException e) {
            // 우리가 직접 던진 비즈니스 예외는 그대로 던짐
            throw e;
        } catch (Exception e) {
            // 나머지 (SMTP 서버, 네트워크 오류 등)만 EMAIL_SEND_FAILED 처리
            log.error("이메일 인증코드 전송 실패: {}", e.getMessage(), e);
            throw new ErrorException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            sb.append(CHAR_SET.charAt(index));
        }
        return sb.toString();
    }

    public void verifyCode(String email, String code) {
        VerificationCode verification = verificationCodeRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException(ErrorCode.INVALID_VERIFICATION_CODE));

        if (verification.isExpired()) {
            throw new ErrorException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        if (!verification.getCode().equals(code)) {
            throw new ErrorException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        verification.markAsVerified();
        verificationCodeRepository.save(verification);
        log.info("[이메일 인증] 인증 성공: {}", email);
    }

    // 3. 인증 여부 확인
    public boolean isVerified(String email) {
        return verificationCodeRepository.findByEmail(email)
                .map(VerificationCode::isVerified)
                .orElse(false);
    }


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
