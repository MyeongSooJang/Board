package com.project.community.domain.boardlike.controller;

import com.project.community.domain.boardlike.dto.BoardLikeResponseDTO;
import com.project.community.domain.boardlike.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/like")
public class BoardLikeController {
    private final BoardLikeService boardLikeService;

    @PostMapping
    public ResponseEntity<BoardLikeResponseDTO> toggleLike(
            @PathVariable Long boardId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BoardLikeResponseDTO response = boardLikeService.convertStatus(boardId, username);
        return ResponseEntity.ok(response);
    }
}
