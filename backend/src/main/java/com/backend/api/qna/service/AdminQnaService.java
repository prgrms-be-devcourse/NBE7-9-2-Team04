package com.backend.api.qna.service;

import com.backend.api.qna.dto.request.QnaAnswerRequest;
import com.backend.api.qna.dto.response.QnaResponse;
import com.backend.api.user.service.AdminUserService;
import com.backend.domain.qna.entity.Qna;
import com.backend.domain.qna.entity.QnaCategoryType;
import com.backend.domain.qna.repository.QnaRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQnaService {

    private final QnaRepository qnaRepository;
    private final AdminUserService adminUserService;

    private Qna findByIdOrThrow(Long qnaId) {
        return qnaRepository.findById(qnaId)
                .orElseThrow(() -> new ErrorException(ErrorCode.QNA_NOT_FOUND));
    }

    @Transactional
    public QnaResponse registerAnswer(Long qnaId, @Valid QnaAnswerRequest request, User user) {
        adminUserService.validateAdminAuthority(user);
        Qna qna = findByIdOrThrow(qnaId);
        if (qna.getIsAnswered()) {
            throw new ErrorException(ErrorCode.QNA_ALREADY_ANSWERED);
        }
        qna.registerAnswer(request.answer());
        return QnaResponse.from(qna);
    }

    @Transactional
    public void deleteQna(Long qnaId, User user) {
        adminUserService.validateAdminAuthority(user);
        Qna qna = findByIdOrThrow(qnaId);
        qnaRepository.delete(qna);
    }

    public List<QnaResponse> getAllQna(User user) {
        adminUserService.validateAdminAuthority(user);
        List<Qna> qnaList = qnaRepository.findAll();
        return qnaList.stream()
                .map(QnaResponse::from)
                .toList();
    }

    public QnaResponse getQna(Long qnaId, User user) {
        adminUserService.validateAdminAuthority(user);
        Qna qna = findByIdOrThrow(qnaId);
        return QnaResponse.from(qna);
    }

    public List<QnaResponse> getQnaByCategory(User user, QnaCategoryType categoryType) {
        adminUserService.validateAdminAuthority(user);
        List<Qna> qnaList = qnaRepository.findByCategoryType(categoryType);
        return qnaList.stream()
                .map(QnaResponse::from)
                .toList();
    }
}
