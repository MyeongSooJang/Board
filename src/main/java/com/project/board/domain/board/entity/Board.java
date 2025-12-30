package com.project.board.domain.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.board.domain.BaseEntity;
import com.project.board.domain.member.entity.Member;

@Entity
@NoArgsConstructor
@Getter
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardNo;

    private String boardTitle;

    private String boardContent;

    @ManyToOne
    private Member memberNo;
}
