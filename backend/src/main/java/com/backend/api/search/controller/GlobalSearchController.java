package com.backend.api.search.controller;

import com.backend.api.search.dto.SearchResultDto;
import com.backend.api.search.service.GlobalSearchService;
import com.backend.global.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class GlobalSearchController {

    private final GlobalSearchService globalSearchService;

    @GetMapping
    public ApiResponse<List<SearchResultDto>> search(@RequestParam String keyword) {
        return ApiResponse.ok(globalSearchService.searchAll(keyword));
    }
}