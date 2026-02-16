package com.project.community.boardlike.service;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.boardlike.dto.BoardLikeResponse;
import com.project.community.boardlike.entity.BoardLike;
import com.project.community.boardlike.repository.BoardLikeRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.dto.ErrorCode;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 좋아요 상태 조회 (username 기반, DB 접근 최소화)
     */
    public BoardLikeResponse getStatus(Long boardId, String username) {
        Board board = searchBoard(boardId);
        Optional<BoardLike> existing = boardLikeRepository.findByBoardIdAndUsername(boardId, username);
        boolean liked = existing.isPresent();
        return new BoardLikeResponse(board.getBoardId(), board.getLikeCount(), liked);
    }

    private Board searchBoard(Long boardId) {
        return boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    /**
     * 게시글 좋아요 토글 (username 기반, DB 접근 최소화)
     */
    @Transactional
    public BoardLikeResponse toggleLike(Long boardId, String username) {
        Board board = searchBoard(boardId);
        Optional<BoardLike> existing = boardLikeRepository.findByBoardIdAndUsername(boardId, username);
        boolean liked = processLike(existing, username, board);
        return new BoardLikeResponse(board.getBoardId(), board.getLikeCount(), liked);
    }

    /**
     * 좋아요 토글 처리
     * - 기존 좋아요 있음: 삭제 + 카운트 감소
     * - 기존 좋아요 없음: 생성 + 카운트 증가
     * Dirty Checking으로 Board 변경사항 자동 반영
     */
    private boolean processLike(Optional<BoardLike> existing, String username, Board board) {
        if (existing.isPresent()) {
            board.decreaseLikeCount();
            boardLikeRepository.delete(existing.get());
            return false;
        } else {
            Member member = searchMember(username);
            board.increaseLikeCount();
            boardLikeRepository.save(new BoardLike(member, board));
            return true;
        }
    }

    private Member searchMember(String username) {
        return memberRepository.findByUsernameAndDeleteTimeIsNull(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
