package com.project.community.domain.comment.entity;

import com.project.community.domain.BaseEntity;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.comment.dto.CommentCreateRequestDTO;
import com.project.community.domain.comment.dto.CommentUpdateRequestDTO;
import com.project.community.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long commentParentId;

    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(Long commentParentId, String commentContent, Board board, Member member) {
        this.commentParentId = commentParentId;
        this.commentContent = commentContent;
        this.board = board;
        this.member = member;
    }

    public static Comment createComment(CommentCreateRequestDTO dto, Board board, Member member) {
        return new Comment(dto.getCommentParentId(), dto.getCommentContent(), board, member);
    }

    public Comment updateComment(CommentUpdateRequestDTO dto) {
        this.commentContent = dto.getCommentContent();
        return this;
    }
}
