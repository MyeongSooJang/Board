package com.project.community.board.controller;


import com.project.community.board.dto.BoardCreateRequest;
import com.project.community.board.dto.BoardResponse;
import com.project.community.board.dto.BoardSearchRequest;
import com.project.community.board.dto.BoardUpdateRequest;
import com.project.community.board.service.BoardService;
import com.project.community.exception.UnauthorizedException;
import com.project.community.exception.dto.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
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
            description = "정렬 타입에 따라 모든 게시물을 조회합니다")
    public ResponseEntity<Page<BoardResponse>> findAll(
            @Parameter(description = "정렬 방식 (LATEST, OLDEST, HOT, VIEWCOUNT, LIKECOUNT, COMMENTCOUNT)")
            @RequestParam(defaultValue = "latest") String sort,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(boardService.searchAll(sort, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "게시물 검색",
            description = "제목 / 내용 / 작성자 / 제목 + 내용 으로 게시물을 검색합니다")
    public ResponseEntity<Page<BoardResponse>> search(
            @ParameterObject BoardSearchRequest request,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(boardService.searchBoard(request, pageable));
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 조회 (일반)",
            description = "활성화된 게시글만 조회 가능")
    public ResponseEntity<BoardResponse> findByBoardId(
            @Parameter(description = "보여줄 게시글 번호")
            @PathVariable Long boardId) {
        boardService.increaseViewCount(boardId);
        return ResponseEntity.ok(boardService.searchByBoardId(boardId));
    }

    @GetMapping("/{boardId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "게시글 조회 (관리자)",
            description = "삭제된 게시글도 조회 가능 (관리자 전용)")
    public ResponseEntity<BoardResponse> findByBoardIdAsAdmin(
            @Parameter(description = "보여줄 게시글 번호")
            @PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.searchByBoardIdAsAdmin(boardId));
    }

    @PostMapping
    @Operation(summary = "게시글 작성",
            description = "새로운 게시글 작성")
    public ResponseEntity<BoardResponse> createBoard(
            @RequestBody BoardCreateRequest board,
            @AuthenticationPrincipal String username) {
        Long boardId = boardService.createBoard(board, username);
        BoardResponse response = boardService.searchByBoardId(boardId);
        return ResponseEntity.created(URI.create("/boards/" + boardId)).body(response);
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "게시글 수정",
            description = "게시물 번호에 해당하는 상세 내용 수정")
    public ResponseEntity<BoardResponse> updateBoard(
            @Parameter(description = "수정할 게시글 번호")
            @PathVariable Long boardId,
            @Parameter(description = "수정할 상세 내용")
            @RequestBody BoardUpdateRequest request,
            @AuthenticationPrincipal String username
    ) {
        if (username == null) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(boardService.updateBoard(boardId, request, username));
    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시글 삭제",
            description = "게시글 번호에 해당하는 게시글 삭제")
    public ResponseEntity<Void> deleteBoard(
            @Parameter(description = "삭제할 게시글 번호")
            @PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "게시글 조회",
            description = "관리자가 삭제된 게시글 조회")
    public ResponseEntity<Page<BoardResponse>> deleteBoards(
            @Parameter(description = "정렬 방식 (LATEST, VIEWCOUNT, LIKECOUNT, COMMENTCOUNT)")
            @RequestParam(defaultValue = "latest") String sort,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(boardService.searchDeletedAll(sort, pageable));
    }
}
