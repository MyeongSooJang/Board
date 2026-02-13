package com.project.community.commentlike.controller;

import com.project.community.commentlike.dto.CommentLikeResponse;
import com.project.community.commentlike.service.CommentLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments/{commentId}/likes")
@Tag(name = "댓글 좋아요", description = "댓글 좋아요 관련 API")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @GetMapping
    @Operation(summary = "댓글 좋아요 상태 조회",
            description = "현재 사용자의 댓글 좋아요 상태 조회")
    public ResponseEntity<CommentLikeResponse> getLikeStatus(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "댓글 번호")
            @PathVariable Long commentId,
            @AuthenticationPrincipal String username) {
        CommentLikeResponse response = commentLikeService.getLikeStatus(commentId, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "댓글 좋아요 토글",
            description = "댓글 좋아요를 추가 또는 제거합니다")
    public ResponseEntity<CommentLikeResponse> toggleLike(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "댓글 번호")
            @PathVariable Long commentId,
            @AuthenticationPrincipal String username) {
        CommentLikeResponse response = commentLikeService.toggleLike(commentId, username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "댓글 좋아요 삭제",
            description = "댓글 좋아요를 삭제합니다")
    public ResponseEntity<Void> deleteLike(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "댓글 번호")
            @PathVariable Long commentId,
            @AuthenticationPrincipal String username) {
        commentLikeService.toggleLike(commentId, username);
        return ResponseEntity.noContent().build();
    }
}
