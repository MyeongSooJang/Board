package com.project.community.domain.board.controller;

import com.project.community.domain.board.dto.BoardCreateRequestDTO;
import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.dto.BoardUpdateRequestDTO;
import com.project.community.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
@RequestMapping("/boards")
@Tag(name = "게시판", description = "게시판 관련 API")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    @Operation(summary = "모든 게시물 조회",
            description = "최신순으로 모든 게시물을 조회합니다")
    public ResponseEntity<List<BoardResponseDTO>> findAll() {
        return ResponseEntity.ok(boardService.searchAll());
    }

    @GetMapping("/search/title")
    @Operation(summary = "제목으로 게시물 조회",
            description = "입력한 제목이 포함된 게시글을 최신순으로 조회합니다")
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByBoardTitle(
            @Parameter(description = "검색할 게시물의 제목", example = "안녕하세요")
            @RequestParam String boardTitle) {
        return ResponseEntity.ok(boardService.searchBoardsByBoardTitle(boardTitle));
    }

    @GetMapping("/search/writer")
    @Operation(summary = "작성자로 게시물 조회",
            description = "작성자가 일치하는 게시글을 최신순으로 조회합니다")
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByMemberName(
            @Parameter(description = "검색할 게시물 작성자 이름", example = "ms")
            @RequestParam String memberName) {
        return ResponseEntity.ok(boardService.searchBoardsByMemberName(memberName));
    }

    @GetMapping("/search/keyword")
    @Operation(summary = "키워드로 게시물 조회",
            description = "제목과 내용에 키워드가 포함된 게시글을 최신순으로 조회합니다")
    public ResponseEntity<List<BoardResponseDTO>> searchBoardsByKeyWord(
            @Parameter(description = "검색할 키워드 (제목, 내용 포함)", example = "Java")
            @RequestParam String keyword) {
        return ResponseEntity.ok(boardService.searchBoardsByKeyWord(keyword));
    }

    @GetMapping("/{boardNo}")
    @Operation(summary = "게시글 클릭시 게시물 내용 출력",
            description = "해당하는 번호의 게시글 내용 출력")
    public ResponseEntity<BoardResponseDTO> findByBoardNo(
            @Parameter(description = "보여줄 게시글 번호")
            @PathVariable Long boardNo) {
        return ResponseEntity.ok(boardService.searchByBoardNo(boardNo));
    }

    @PostMapping
    @Operation(summary = "게시글 작성",
            description = "새로운 게시글 작성")
    public ResponseEntity<URI> createBoard(@RequestBody BoardCreateRequestDTO board) {
        Long boardNo = boardService.createBoard(board);
        URI location = URI.create("/boards/" + boardNo);
        return ResponseEntity.created(location).body(location);
    }

    @PutMapping("/{boardNo}")
    @Operation(summary = "게시글 수정",
            description = "게시물 번호에 해당하는 상세 내용 수정")
    public ResponseEntity<BoardResponseDTO> updateBoard(
            @Parameter(description = "수정할 게시글 번호")
            @PathVariable Long boardNo,
            @Parameter(description = "수정할 상세 내용")
            @RequestBody BoardUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(boardService.updateBoard(boardNo, requestDTO));
    }

    @DeleteMapping("/{boardNo}")
    @Operation(summary = "게시글 삭제",
            description = "게시글 번호에 해당하는 게시글 삭제")
    public ResponseEntity<Void> deleteBoard(
            @Parameter(description = "삭제할 게시글 번호")
            @PathVariable Long boardNo) {
        boardService.deleteBoard(boardNo);
        return ResponseEntity.ok().build();
    }
}
