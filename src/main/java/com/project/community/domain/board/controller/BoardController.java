package com.project.community.domain.board.controller;

import com.project.community.domain.board.dto.BoardCreateRequestDTO;
import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.service.BoardService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardResponseDTO>> findAll() {
        return ResponseEntity.ok(boardService.searchAll());
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByBoardTitle(
            @RequestParam String boardTitle) {
        return ResponseEntity.ok(boardService.searchBoardsByBoardTitle(boardTitle));
    }

    @GetMapping("/search/writer")
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByMemberName(
            @RequestParam String memberName) {
        return ResponseEntity.ok(boardService.searchBoardsByMemberName(memberName));
    }

    @GetMapping("/search/keyword")
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByKeyWord(
            @RequestParam String keyword) {
        return ResponseEntity.ok(boardService.searchBoardsByKeyWord(keyword));
    }

    @PostMapping
    public ResponseEntity<URI> createBoard(@RequestBody BoardCreateRequestDTO board) {
        Long boardNo = boardService.createBoard(board);
        URI location = URI.create("/boards/" + boardNo);
        return ResponseEntity.created(location).body(location);
    }
}
