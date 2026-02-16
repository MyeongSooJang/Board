package com.project.community.bookmark.controller;

import com.project.community.bookmark.dto.BookmarkResponse;
import com.project.community.bookmark.dto.BookmarkStatusResponse;
import com.project.community.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/bookmarks")
@Tag(name = "북마크", description = "북마크 관련 API")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/boards/{boardId}")
    @Operation(summary = "북마크 추가 또는 삭제",
            description = "게시글을 북마크에 추가하거나 삭제 (토글)")
    public ResponseEntity<BookmarkStatusResponse> toggleBookmark(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @AuthenticationPrincipal String username) {
        BookmarkStatusResponse response = bookmarkService.toggleBookmark(boardId, username);
        return ResponseEntity.ok(new BookmarkStatusResponse(response.isBookmarked(), response.getBookmarkCount()));
    }

    @GetMapping("/boards/{boardId}/status")
    @Operation(summary = "북마크 상태 조회",
            description = "사용자가 해당 게시글을 북마크했는지 확인")
    public ResponseEntity<BookmarkStatusResponse> getBookmarkStatus(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @AuthenticationPrincipal String username) {
        boolean isBookmarked = bookmarkService.isBookmarked(boardId, username);
        Long bookmarkCount = bookmarkService.getBookmarkCount(boardId);
        return ResponseEntity.ok(new BookmarkStatusResponse(isBookmarked, bookmarkCount));
    }

    @DeleteMapping("/boards/{boardId}")
    @Operation(summary = "북마크 삭제",
            description = "게시글의 북마크 삭제")
    public ResponseEntity<Void> deleteBookmark(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId,
            @AuthenticationPrincipal String username) {
        bookmarkService.deleteBookmark(boardId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-list")
    @Operation(summary = "내 북마크 목록",
            description = "사용자가 북마크한 게시글 목록 조회")
    public ResponseEntity<Page<BookmarkResponse>> getMyBookmarks(
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal String username) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookmarkResponse> bookmarks = bookmarkService.findByMemberId(username, pageable);
        return ResponseEntity.ok(bookmarks);
    }

    @GetMapping("/boards/{boardId}/count")
    @Operation(summary = "북마크 개수",
            description = "게시글의 총 북마크 개수")
    public ResponseEntity<Long> getBookmarkCount(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardId) {
        Long count = bookmarkService.getBookmarkCount(boardId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/my-count")
    @Operation(summary = "내 북마크 개수",
            description = "사용자의 총 북마크 개수")
    public ResponseEntity<Long> countBookMark(
            @AuthenticationPrincipal String username) {
        Long count = bookmarkService.getMemberBookmarkCount(username);
        return ResponseEntity.ok(count);
    }

}
