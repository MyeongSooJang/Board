package com.project.community.domain.board.dto;

import com.project.community.domain.board.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardResponseDTO {
    private Long boardNo;
    private String boardTitle;
    private String boardContent;
    private String memberName;
    private String memberId;
    private LocalDateTime updateTime;

    public static BoardResponseDTO from(Board board) {
        return new BoardResponseDTO(
                board.getBoardNo(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getMember().getMemberName(),
                board.getMember().getMemberId(),
                board.getUpdateTime()
        );
    }
}
