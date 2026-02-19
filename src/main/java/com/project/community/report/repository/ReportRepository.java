package com.project.community.report.repository;

import com.project.community.report.entity.Report;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("""
            SELECT r
            FROM Report r
            WHERE r.member.memberId = :memberId AND
                  r.board.boardId = :boardId AND
                  r.deleteTime IS NULL
           """)
    Optional<Report> findByMemberIdAndBoardId(@Param("memberId") Long memberId, @Param("boardId") Long boardId);


    @Query("""
            SELECT r
            FROM Report r
            WHERE r.member.memberId = :memberId AND 
                  r.commentId = :commentId AND 
                  r.deleteTime IS NULL
          """)
    Optional<Report> findByMemberIdAndCommentId(@Param("memberId") Long memberId, @Param("commentId") Long commentId);
}
