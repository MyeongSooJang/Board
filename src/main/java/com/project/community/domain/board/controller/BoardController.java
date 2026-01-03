package com.project.community.domain.board.controller;

import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardResponseDTO>> findAll() {
        return ResponseEntity.ok(boardService.searchAll());
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByBoardTitle(String boardTitle) {
        return ResponseEntity.ok(boardService.searchBoardsByBoardTitle(boardTitle));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByMemberName(String memberName) {
        return ResponseEntity.ok(boardService.searchBoardsByMemberName(memberName));
    }



}
