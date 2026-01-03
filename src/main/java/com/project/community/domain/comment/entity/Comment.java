package com.project.community.domain.comment.entity;

import com.project.community.domain.BaseEntity;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter

public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    private Long commentParentNo;

    private String commentContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne
    private Member memberNo;
}
