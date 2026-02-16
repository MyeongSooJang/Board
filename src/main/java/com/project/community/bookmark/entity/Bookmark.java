package com.project.community.bookmark.entity;

import com.project.community.board.entity.Board;
import com.project.community.common.BaseEntity;
import com.project.community.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookmark", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "board_id"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long markId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Bookmark(Member member, Board board) {
        this.member = member;
        this.board = board;
    }

    public static Bookmark createBookmark(Member member, Board board) {
        return new Bookmark(member, board);
    }

    public void softDelete() {
        this.deleteTime = LocalDateTime.now();
    }
}
