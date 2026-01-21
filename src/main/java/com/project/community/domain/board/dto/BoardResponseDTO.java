package com.project.community.domain.board.dto;

import com.project.community.domain.board.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardResponseDTO {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String memberName;
    private Long memberId;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    private Integer boardViewCount;

    public static BoardResponseDTO from(Board board) {
        return new BoardResponseDTO(
                board.getBoardId(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getMember().getName(),
                board.getMember().getMemberId(),
                board.getUpdateTime(),
                board.getCreateTime(),
                board.getBoardViewCount()
        );
    }
}
