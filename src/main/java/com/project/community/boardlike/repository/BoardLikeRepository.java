package com.project.community.boardlike.repository;

import com.project.community.boardlike.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    // username 기반 조회 (fetch join으로 board, member 포함)
    @Query("""
            SELECT b
            FROM BoardLike b
            JOIN FETCH b.board board
            JOIN FETCH b.member member
            WHERE board.boardId = :boardId AND member.username = :username AND board.deleteTime IS NULL
            """)
    Optional<BoardLike> findByBoardIdAndUsername(@Param("boardId") Long boardId, @Param("username") String username);

    // memberId 기반 조회 (하위호환성 유지)
    @Query("""
            SELECT b
            FROM BoardLike b
            JOIN FETCH b.board board
            JOIN FETCH b.member member
            WHERE board.boardId = :boardId AND member.memberId = :memberId AND board.deleteTime IS NULL
            """)
    Optional<BoardLike> findByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}
