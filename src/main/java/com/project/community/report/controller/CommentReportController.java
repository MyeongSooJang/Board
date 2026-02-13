package com.project.community.report.controller;

import com.project.community.report.dto.ReportRequest;
import com.project.community.report.dto.ReportResponse;
import com.project.community.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments/{commentId}/reports")
@Tag(name = "댓글 신고", description = "댓글 신고 관련 API")
public class CommentReportController {
    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "댓글 신고",
            description = "부적절한 댓글을 신고합니다")
    public ResponseEntity<ReportResponse> reportComment(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "댓글 번호")
            @PathVariable Long commentId,
            @Parameter(description = "신고 내용")
            @RequestBody ReportRequest request,
            @AuthenticationPrincipal String username) {
        ReportResponse response = reportService.reportComment(commentId, request, username);
        return ResponseEntity.ok(response);
    }
}
