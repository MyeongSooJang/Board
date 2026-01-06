package com.project.community.domain.board.repository;

import com.project.community.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByUpdateTimeDesc();
    List<Board> findByBoardTitleContainingOrderByUpdateTimeDesc(String boardTitle);
    List<Board> findByMemberMemberNameOrderByUpdateTimeDesc(String memberName);
    @Query("""
              SELECT b
              FROM Board AS b
              WHERE b.boardContent LIKE CONCAT ('%',:keyword, '%') OR
                    b.boardTitle LIKE CONCAT ('%', :keyword, '%')
              ORDER BY b.updateTime DESC
            """)
    List<Board> findByKeywordInTitleOrContent(@Param("keyword") String keyword);
    Board findByBoardNo(Long boardNo);
}
