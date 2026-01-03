package com.project.community.domain.board.controller;

import com.project.community.domain.board.entity.Board;
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
    public ResponseEntity<?> searchBoardsByBoardTitle(String boardTitle) {
        List<Board> boards = boardService.searchBoardsByBoardTitle(boardTitle);
        return ResponseEntity.ok(boards);
    }

    @GetMapping
    public ResponseEntity<?> searchBoardsByMemberName(String memberName) {
        List<Board> boards = boardService.searchBoardsByMemberName(memberName);
        return ResponseEntity.ok(boards);
    }
}
