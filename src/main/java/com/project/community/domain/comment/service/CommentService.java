package com.project.community.domain.comment.service;

import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import com.project.community.domain.comment.dto.CommentCreateRequestDTO;
import com.project.community.domain.comment.dto.CommentResponseDTO;
import com.project.community.domain.comment.dto.CommentUpdateRequestDTO;
import com.project.community.domain.comment.entity.Comment;
import com.project.community.domain.comment.repository.CommentRepository;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<CommentResponseDTO> findAllCommentsByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findAllByBoardOrderByCreateTimeDesc(searchBoard(boardId));
        return comments.stream().map(CommentResponseDTO::from).toList();
    }

    public CommentResponseDTO createComment(Long boardId, CommentCreateRequestDTO comment) {
        Comment saved = commentRepository.save(
                Comment.createComment(comment, searchBoard(boardId), searchMember(comment)));
        return CommentResponseDTO.from(saved);
    }

    private Member searchMember(CommentCreateRequestDTO comment) {
        return memberRepository.findById(comment.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원이 존재하지 않습니다"));
    }

    private Board searchBoard(Long boardId) {
        return boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다"));
    }

    public CommentResponseDTO updateComment(Long commentId, CommentUpdateRequestDTO comment) {
        Comment updated = searchComment(commentId).updateComment(comment);
        return CommentResponseDTO.from(updated);
    }

    private Comment searchComment(Long commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 댓글 번호의 댓글이 존재하지 않습니다"));
    }

    public void deleteComment(Long commentId) {
        commentRepository.delete(searchComment(commentId));
    }
}
