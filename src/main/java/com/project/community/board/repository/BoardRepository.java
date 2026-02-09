package com.project.community.board.repository;

import com.project.community.board.entity.Board;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByDeleteTimeIsNull(Pageable pageable);

    @Query("""
               SELECT b
               FROM Board As b
               WHERE b.boardTitle LIKE CONCAT ('%',:boardTitle, '%')
               AND   b.deleteTime IS NULL
            """)
    Page<Board> findByBoardTitle(@Param("boardTitle") String boardTitle, Pageable pageable);

    Page<Board> findByDeleteTimeIsNullAndMemberName(@Param("memberName") String memberName, Pageable pageable);

    @Query("""
              SELECT b
              FROM Board AS b
              WHERE (b.boardContent LIKE CONCAT ('%',:keyword, '%') OR b.boardTitle LIKE CONCAT ('%', :keyword, '%'))
              AND b.deleteTime IS NULL
            """)
    Page<Board> findByKeywordInTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    Optional<Board> findByBoardIdAndDeleteTimeIsNull(@Param("boardId") Long boardId);
}
