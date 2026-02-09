package com.project.community.report.entity;

import com.project.community.common.BaseEntity;
import com.project.community.board.entity.Board;
import com.project.community.member.entity.Member;
import com.project.community.report.dto.ReportRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String type;

    @Column(nullable = false)
    private String content;

    private String status = "SUBMITTED";

    private Report(Member member, Board board, ReportRequestDTO request) {
        this.member = member;
        this.board = board;
        this.type = request.getType();
        this.content = request.getContent();
    }

    public static Report createReport(Member member, Board board, ReportRequestDTO request) {
        return new Report(member, board, request);
    }

    public Report approve() {
        this.status = "APPROVED";
        return this;
    }

    public Report reject() {
        this.status = "REJECTED";
        return this;
    }
}
