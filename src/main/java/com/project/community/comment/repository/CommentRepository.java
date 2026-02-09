package com.project.community.comment.repository;

import com.project.community.board.entity.Board;
import com.project.community.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardOrderByCreateTimeDesc(Board board);
    Optional<Comment> findByCommentId(Long commentId);
}
