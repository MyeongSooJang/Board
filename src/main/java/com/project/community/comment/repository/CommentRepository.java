package com.project.community.comment.repository;

import com.project.community.board.entity.Board;
import com.project.community.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.board.boardId = :boardId AND c.deleteTime IS NULL
            ORDER BY c.createTime DESC
            """)
    Page<Comment> findByBoardIdLatest(Long boardId, Pageable pageable);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.board.boardId = :boardId AND c.deleteTime IS NULL
            ORDER BY c.likeCount DESC, c.createTime DESC
            """)
    Page<Comment> findByBoardIdTop(Long boardId, Pageable pageable);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.commentId = :commentId AND c.deleteTime IS NULL
            """)
    Optional<Comment> findByCommentId(Long commentId);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.board.boardId = :boardId AND c.content LIKE %:keyword% AND c.deleteTime IS NULL
            ORDER BY c.createTime DESC
            """)
    Page<Comment> findByBoardIdAndKeyword(Long boardId, String keyword, Pageable pageable);

    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.parentId = :parentId AND c.deleteTime IS NULL
            """)
    List<Comment> findByParentIdAndNotDeleted(Long parentId);

}
