package com.project.community.comment.controller;

import com.project.community.comment.dto.CommentCreateRequest;
import com.project.community.comment.dto.CommentResponse;
import com.project.community.comment.dto.CommentUpdateRequest;
import com.project.community.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
@Tag(name = "댓글", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "모든 댓글 조회",
            description = "게시글 번호에 대한 전체 댓글을 조회 (정렬: LATEST, TOP)")
    public ResponseEntity<Page<CommentResponse>> findAllByBoardId(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "정렬 방식 (LATEST, TOP)")
            @RequestParam(defaultValue = "latest") String sort,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> comments = commentService.findAllByBoardId(boardId, sort, pageable);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/search")
    @Operation(summary = "댓글 검색",
            description = "게시글의 댓글을 키워드로 검색")
    public ResponseEntity<Page<CommentResponse>> searchComments(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "검색 키워드")
            @RequestParam String keyword,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> comments = commentService.searchComments(boardId, keyword, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @Operation(summary = "댓글 작성",
            description = "게시글에 대한 댓글 작성")
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "생성할 댓글 내용")
            @RequestBody CommentCreateRequest comment,
            @AuthenticationPrincipal String username) {
        CommentResponse response = commentService.createComment(boardId, comment, username);
        return ResponseEntity.created(
                URI.create("/boards/" + boardId + "/comments/" + response.getCommentId())
        ).body(response);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정",
            description = "해당하는 댓글번호 댓글 수정")
    public ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "댓글 번호")
            @PathVariable Long commentId,
            @Parameter(description = "수정할 댓글 내용")
            @RequestBody CommentUpdateRequest request,
            @Parameter(description = "회원 아이디")
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request, username));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제",
            description = "해당하는 댓글번호 댓글 삭제")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "댓글 번호")
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
