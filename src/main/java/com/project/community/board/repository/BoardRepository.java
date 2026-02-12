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
    Page<Board> findByDeleteTimeIsNull(Pageable pageable);

    @Query("""
               SELECT b
               FROM Board As b
               WHERE b.title LIKE CONCAT ('%',:boardTitle, '%')
               AND   b.deleteTime IS NULL
            """)
    Page<Board> findByBoardTitle(@Param("boardTitle") String boardTitle, Pageable pageable);

    @Query("""
               SELECT b
               FROM Board As b
               WHERE b.content LIKE CONCAT ('%',:boardContent, '%')
               AND   b.deleteTime IS NULL
            """)
    Page<Board> findByBoardContent(@Param("boardContent") String boardContent, Pageable pageable);

    @Query("""
              SELECT b
              FROM Board AS b
              WHERE (b.content LIKE CONCAT ('%',:keyword, '%') OR b.title LIKE CONCAT ('%', :keyword, '%'))
              AND b.deleteTime IS NULL
            """)
    Page<Board> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
              SELECT b
              FROM Board AS b
              WHERE b.member.name = :memberName
              AND b.deleteTime IS NULL
            """)
    Page<Board> findByWriter(@Param("memberName") String memberName, Pageable pageable);



    Optional<Board> findByBoardIdAndDeleteTimeIsNull(@Param("boardId") Long boardId);
}
