package com.project.community.bookmark.service;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.bookmark.dto.BookmarkResponse;
import com.project.community.bookmark.dto.BookmarkStatusResponse;
import com.project.community.bookmark.entity.Bookmark;
import com.project.community.bookmark.repository.BookmarkRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.dto.ErrorCode;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BookmarkStatusResponse toggleBookmark(Long boardId, String username) {
        Optional<Bookmark> existingBookmark = bookmarkRepository.findByUsernameAndBoardId(username, boardId);

        boolean isBookmarked;
        if (existingBookmark.isPresent()) {
            // 북마크 삭제 (물리 삭제)
            bookmarkRepository.delete(existingBookmark.get());
            isBookmarked = false;
        } else {
            Member member = searchMember(username);
            Board board = searchBoard(boardId);
            Bookmark newBookmark = Bookmark.createBookmark(member, board);
            bookmarkRepository.save(newBookmark);
            isBookmarked = true;
        }

        Long bookmarkCount = bookmarkRepository.countByBoardId(boardId);
        return new BookmarkStatusResponse(isBookmarked, bookmarkCount);
    }



    // 북마크 상태 조회 (username 기반, DB 접근 최소화)
    public boolean isBookmarked(Long boardId, String username) {
        return bookmarkRepository.existsByUsernameAndBoardId(username, boardId);
    }

    // 사용자의 모든 북마크 조회 (페이지네이션, fetch join으로 member/board 포함)
    public Page<BookmarkResponse> findByMemberId(String username, Pageable pageable) {
        return bookmarkRepository.findByUsername(username, pageable)
                .map(BookmarkResponse::from);
    }

    // 북마크 삭제 (username 기반, 물리 삭제)
    @Transactional
    public void deleteBookmark(Long boardId, String username) {
        Bookmark bookmark = bookmarkRepository.findByUsernameAndBoardId(username, boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

    // 게시글의 총 북마크 개수
    public Long getBookmarkCount(Long boardId) {
        return bookmarkRepository.countByBoardId(boardId);
    }

    // 사용자의 총 북마크 개수 (username 기반, DB 접근 최소화)
    public Long getMemberBookmarkCount(String username) {
        return bookmarkRepository.countByUsername(username);
    }

    private Member searchMember(String username) {
        return memberRepository.findByUsernameAndDeleteTimeIsNull(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Board searchBoard(Long boardId) {
        return boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }
}
