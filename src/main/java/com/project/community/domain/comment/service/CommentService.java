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

    public List<CommentResponseDTO> findAllCommentsByBoardNo(Long boardNo) {
        List<Comment> comments = commentRepository.findAllByBoardOrderByCreateTimeDesc(searchBoard(boardNo));
        return comments.stream().map(CommentResponseDTO::from).toList();
    }

    public CommentResponseDTO createComment(CommentCreateRequestDTO comment) {
        Comment saved = commentRepository.save(
                Comment.createComment(comment, searchBoard(comment), searchMember(comment)));
        return CommentResponseDTO.from(saved);
    }

    private Member searchMember(CommentCreateRequestDTO comment) {
        return memberRepository.findByMemberNo(comment.getMemberNo())
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원 번호의 회원이 존재하지 않습니다"));
    }

    private Board searchBoard(CommentCreateRequestDTO comment) {
        return boardRepository.findByBoardNo(comment.getBoardNo())
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다"));
    }

    private Board searchBoard(Long boardNo) {
        return boardRepository.findByBoardNo(boardNo)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다"));
    }

    public CommentResponseDTO updateComment(Long commentNo, CommentUpdateRequestDTO comment) {
        Comment updated = searchComment(commentNo).updateComment(comment);
        return CommentResponseDTO.from(updated);
    }

    private Comment searchComment(Long commentNo) {
        return commentRepository.findByCommentNo(commentNo)
                .orElseThrow(() -> new NoSuchElementException("해당하는 대슥ㄹ 번호의 댓글이 존재하지 않습니다"));
    }

    public void deleteComment(Long commentNo) {
        commentRepository.delete(searchComment(commentNo));
    }
}
