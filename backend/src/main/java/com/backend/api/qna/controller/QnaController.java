package com.backend.api.qna.controller;

import com.backend.api.qna.dto.request.QnaAddRequest;
import com.backend.api.qna.dto.request.QnaUpdateRequest;
import com.backend.api.qna.dto.response.QnaResponse;
import com.backend.api.qna.service.QnaService;
import com.backend.global.Rq.Rq;
import com.backend.global.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Qna")
@RequiredArgsConstructor
@Tag(name = "Qna", description = "Qna 관련 API")
public class QnaController {

    private final QnaService qnaService;
    private final Rq rq;

    @PostMapping
    @Operation(summary = "Qna 생성", description = "사용자가 Qna를 생성합니다.")
    public ApiResponse<QnaResponse> addQna(
            @Valid @RequestBody QnaAddRequest request
            ) {
        QnaResponse qnaResponse = qnaService.addQna(request, rq.getUser());
        return ApiResponse.created("Qna가 생성되었습니다.", qnaResponse);
    }

    @PutMapping("/{qnaId}")
    @Operation(summary = "Qna 수정", description = "사용자가 Qna를 수정합니다.")
    public ApiResponse<QnaResponse> updateQna(
            @PathVariable Long qnaId,
            @Valid @RequestBody QnaUpdateRequest request ) {
        QnaResponse qnaResponse = qnaService.updateQna(qnaId, request, rq.getUser());
        return ApiResponse.ok("Qna가 수정되었습니다.", qnaResponse);
    }

    @DeleteMapping("/{qnaId}")
    @Operation(summary = "Qna 삭제", description = "사용자가 Qna를 삭제합니다.")
    public ApiResponse<Void> deleteQna(
            @PathVariable Long qnaId ) {
        qnaService.deleteQna(qnaId, rq.getUser());
        return ApiResponse.ok("Qna가 삭제되었습니다.", null);
    }
}
