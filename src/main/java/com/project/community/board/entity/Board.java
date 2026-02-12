package com.project.community.board.entity;

import com.project.community.common.BaseEntity;
import com.project.community.board.dto.BoardCreateRequest;
import com.project.community.board.dto.BoardUpdateRequest;
import com.project.community.member.entity.Member;
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

public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String title;
    private String content;
    private Long viewCount = 0L;
    private Long commentCount = 0L;
    private Long likeCount = 0L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Board(String title, String boardContent, Member member) {
        this.title = title;
        this.content = boardContent;
        this.member = member;
    }

    public static Board createBoard(BoardCreateRequest board, Member member) {
        return new Board(board.getBoardTitle(), board.getBoardContent(), member);
    }

    public void updateBoard(BoardUpdateRequest updateBoard) {
        title = updateBoard.getBoardTitle();
        content = updateBoard.getBoardContent();
    }

    public void softDelete() {
        this.deleteTime = LocalDateTime.now();
    }

    public void increaseViewCount() {
        viewCount++;
    }

    public void increaseCommentCount() {
        commentCount++;
    }

    public void decreaseCommentCount() {
        if (commentCount > 0) {
            commentCount--;
        }
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
