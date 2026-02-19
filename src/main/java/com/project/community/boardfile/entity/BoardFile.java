package com.project.community.boardfile.entity;

import com.project.community.board.entity.Board;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "board_file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BoardFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String savedName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String url;

    @CreatedDate
    protected LocalDateTime createTime;

    @LastModifiedDate
    protected LocalDateTime updateTime;

    // 파일 생성 시 필요한 필드만 받는 생성자
    public BoardFile(Board board, String originalName, String savedName, Long size, String url) {
        this.board = board;
        this.originalName = originalName;
        this.savedName = savedName;
        this.size = size;
        this.url = url;
    }
}
