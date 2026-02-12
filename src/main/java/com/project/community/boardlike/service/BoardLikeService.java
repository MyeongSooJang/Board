package com.project.community.boardlike.service;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.boardlike.dto.BoardLikeResponse;
import com.project.community.boardlike.entity.BoardLike;
import com.project.community.boardlike.repository.BoardLikeRepository;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardLikeResponse getLikeStatus(Long boardId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Optional<BoardLike> existing = boardLikeRepository.findByBoardIdAndMemberId(boardId, member.getMemberId());
        boolean liked = existing.isPresent();
        Long likeCount = boardLikeRepository.countByBoardId(boardId);

        return new BoardLikeResponse(board.getBoardId(), likeCount, liked);
    }

    public BoardLikeResponse toggleLike(Long boardId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Optional<BoardLike> existing = boardLikeRepository.findByBoardIdAndMemberId(boardId, member.getMemberId());
        boolean liked;

        liked = convertLikeStatus(existing, member, board);

        Long likeCount = boardLikeRepository.countByBoardId(boardId);

        return new BoardLikeResponse(board.getBoardId(), likeCount, liked);
    }

    private boolean convertLikeStatus(Optional<BoardLike> existing, Member member, Board board) {
        boolean liked;
        if (existing.isPresent()) {
            boardLikeRepository.delete(existing.get());
            liked = false;
        } else {
            BoardLike newLike = new BoardLike(null, member, board);
            boardLikeRepository.save(newLike);
            liked = true;
        }
        return liked;
    }
}
