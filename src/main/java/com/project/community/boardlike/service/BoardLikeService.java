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

    public BoardLikeResponse getStatus(Long boardId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        Optional<BoardLike> existing = boardLikeRepository.findByBoardIdAndMemberId(boardId, member.getMemberId());
        boolean liked = existing.isPresent();

        return new BoardLikeResponse(board.getBoardId(), board.getLikeCount(), liked);
    }

    @Transactional
    public BoardLikeResponse toggleLike(Long boardId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        Optional<BoardLike> existing = boardLikeRepository.findByBoardIdAndMemberId(boardId, member.getMemberId());

        boolean liked = convertStatus(existing, member, board);
        return new BoardLikeResponse(board.getBoardId(), board.getLikeCount(), liked);
    }

    private boolean convertStatus(Optional<BoardLike> existing, Member member, Board board) {
        if (existing.isPresent()) {
            board.decreaseLikeCount();
            boardLikeRepository.delete(existing.get());
            return false;
        } else {
            board.increaseLikeCount();
            boardLikeRepository.save(new BoardLike(member, board));
            return true;
        }
    }
}
