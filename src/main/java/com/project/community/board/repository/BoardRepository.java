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
    @Query("""
      SELECT b FROM Board b
      JOIN FETCH b.member m
      WHERE b.deleteTime IS NULL
      ORDER BY b.createTime DESC
  """)
    Page<Board> findLatestBoards(Pageable pageable);

    @Query("""
      SELECT b FROM Board b
      JOIN FETCH b.member m
      WHERE b.deleteTime IS NULL
      ORDER BY b.likeCount DESC, b.createTime DESC
  """)
    Page<Board> findHotBoards(Pageable pageable);

    @Query("""
      SELECT b FROM Board b
      JOIN FETCH b.member m
      WHERE b.deleteTime IS NULL
      ORDER BY b.createTime ASC
  """)
    Page<Board> findOldestBoards(Pageable pageable);

    @Query("""
      SELECT b FROM Board b
      JOIN FETCH b.member m
      WHERE b.deleteTime IS NULL
      ORDER BY b.viewCount DESC
  """)
    Page<Board> findByViewCount(Pageable pageable);

    @Query("""
      SELECT b FROM Board b
      JOIN FETCH b.member m
      WHERE b.deleteTime IS NULL
      ORDER BY b.commentCount DESC
  """)
    Page<Board> findByCommentCount(Pageable pageable);

    @Query("""
      SELECT b FROM Board b
      JOIN FETCH b.member m
      WHERE b.deleteTime IS NULL
      ORDER BY b.likeCount DESC
  """)
    Page<Board> findByLikeCount(Pageable pageable);

    @Query("""
               SELECT b
               FROM Board As b
               JOIN FETCH b.member m
               WHERE b.title LIKE CONCAT ('%',:boardTitle, '%')
               AND   b.deleteTime IS NULL
            """)
    Page<Board> findByBoardTitle(@Param("boardTitle") String boardTitle, Pageable pageable);

    @Query("""
               SELECT b
               FROM Board As b
               JOIN FETCH b.member m
               WHERE b.content LIKE CONCAT ('%',:boardContent, '%')
               AND   b.deleteTime IS NULL
            """)
    Page<Board> findByBoardContent(@Param("boardContent") String boardContent, Pageable pageable);

    @Query("""
              SELECT b
              FROM Board AS b
              JOIN FETCH b.member m
              WHERE (b.content LIKE CONCAT ('%',:keyword, '%') OR b.title LIKE CONCAT ('%', :keyword, '%'))
              AND b.deleteTime IS NULL
            """)
    Page<Board> findByKeyWord(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
              SELECT b
              FROM Board AS b
              JOIN FETCH b.member m
              WHERE m.name = :memberName
              AND b.deleteTime IS NULL
            """)
    Page<Board> findByWriter(@Param("memberName") String memberName, Pageable pageable);

    Optional<Board> findByBoardIdAndDeleteTimeIsNull(@Param("boardId") Long boardId);
}
