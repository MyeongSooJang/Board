package com.project.community.boardlike.controller;

import com.project.community.boardlike.dto.BoardLikeResponse;
import com.project.community.boardlike.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/like")
public class BoardLikeController {
    private final BoardLikeService boardLikeService;

    @GetMapping
    public ResponseEntity<BoardLikeResponse> getLikeStatus(
            @PathVariable Long boardId,
            @AuthenticationPrincipal String username
    ) {
        BoardLikeResponse response = boardLikeService.getStatus(boardId, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BoardLikeResponse> toggleLike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal String username
    ) {
        BoardLikeResponse response = boardLikeService.toggleLike(boardId, username);
        return ResponseEntity.ok(response);
    }
}
