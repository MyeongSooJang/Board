package com.project.community.commentlike.repository;

import com.project.community.commentlike.entity.CommentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @Query("SELECT cl FROM CommentLike cl WHERE cl.comment.commentId = :commentId AND cl.member.memberId = :memberId AND cl.comment.deleteTime IS NULL")
    Optional<CommentLike> findByCommentIdAndMemberId(@Param("commentId") Long commentId, @Param("memberId") Long memberId);

    @Query("SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.commentId = :commentId AND cl.comment.deleteTime IS NULL")
    Long countByCommentId(@Param("commentId") Long commentId);
}
