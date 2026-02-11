package com.project.community.comment.service;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.comment.dto.CommentCreateRequestDTO;
import com.project.community.comment.dto.CommentResponseDTO;
import com.project.community.comment.dto.CommentUpdateRequestDTO;
import com.project.community.comment.entity.Comment;
import com.project.community.comment.repository.CommentRepository;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
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
        Board board = searchBoard(boardId);
        Comment saved = commentRepository.save(
                Comment.createComment(comment, board, searchMember(comment)));
        board.increaseCommentCount();
        boardRepository.save(board);
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
        Comment comment = searchComment(commentId);
        Board board = comment.getBoard();
        commentRepository.delete(comment);
        board.decreaseBoardCommentCount();
        boardRepository.save(board);
    }
}
