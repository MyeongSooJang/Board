package com.project.community.boardlike.repository;

import com.project.community.boardlike.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    @Query("SELECT b FROM BoardLike b WHERE b.board.boardId = :boardId AND b.member.memberId = :memberId")
    Optional<BoardLike> findByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query("SELECT COUNT(b) FROM BoardLike b WHERE b.board.boardId = :boardId")
    Long countByBoardId(@Param("boardId") Long boardId);
}
