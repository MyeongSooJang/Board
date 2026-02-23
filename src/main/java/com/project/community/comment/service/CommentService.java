package com.project.community.comment.service;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.comment.dto.CommentCreateRequest;
import com.project.community.comment.dto.CommentResponse;
import com.project.community.comment.dto.CommentUpdateRequest;
import com.project.community.comment.entity.Comment;
import com.project.community.comment.repository.CommentRepository;
import com.project.community.comment.type.CommentSortType;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.dto.ErrorCode;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<CommentResponse> findAllByBoardId(Long boardId, String sort, Pageable pageable) {
        CommentSortType sortType = CommentSortType.from(sort);
        Page<Comment> comments = switch (sortType) {
            case LATEST -> commentRepository.findByBoardIdLatest(boardId, pageable);
            case TOP -> commentRepository.findByBoardIdTop(boardId, pageable);
        };
        return comments.map(comment -> CommentResponse.from(comment, false));
    }

    public Page<CommentResponse> searchComments(Long boardId, String keyword, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByBoardIdAndKeyword(boardId, keyword, pageable);
        return comments.map(comment -> CommentResponse.from(comment, false));
    }


    @Transactional
    public CommentResponse createComment(Long boardId, CommentCreateRequest request, String username) {
        Board board = searchBoard(boardId);
        Member member = memberRepository.findByUsernameAndDeleteTimeIsNull(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        Comment saved = commentRepository.save(Comment.createComment(request.getParentId(), request.getContent(), board, member));
        board.increaseCommentCount();
        boardRepository.save(board);
        return CommentResponse.from(saved, false);
    }

    private Board searchBoard(Long boardId) {
        return boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    public CommentResponse updateComment(Long boardId, Long commentId, CommentUpdateRequest request, String username) {
        searchBoard(boardId);
        Comment updated = searchComment(commentId).updateComment(boardId, request.getContent(), username);
        return CommentResponse.from(updated, false);
    }

    private Comment searchComment(Long commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public void deleteComment(Long boardId, Long commentId,  String username) {
        Comment comment = searchComment(commentId);
        comment.softDelete(boardId,username);
        comment.getBoard().decreaseCommentCount();
    }
}
