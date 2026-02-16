package com.project.community.comment.entity;

import com.project.community.board.entity.Board;
import com.project.community.common.BaseEntity;
import com.project.community.exception.UnauthorizedException;
import com.project.community.exception.dto.ErrorCode;
import com.project.community.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Long likeCount = 0L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment(Long parentId, String content, Board board, Member member) {
        this.parentId = parentId;
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public static Comment createComment(Long parentId, String content, Board board, Member member) {
        return new Comment(parentId, content, board, member);
    }

    public Comment updateComment(String content, String username) {
        if (!member.getUsername().equals(username)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
        this.content = content;
        return this;
    }

    public void softDelete() {
        this.deleteTime = LocalDateTime.now();
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        if (likeCount > 0) {
            likeCount--;
        }
    }

}