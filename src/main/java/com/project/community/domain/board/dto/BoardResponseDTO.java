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
    private Long memberNo;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    private Integer boardViewCount;

    public static BoardResponseDTO from(Board board) {
        return new BoardResponseDTO(
                board.getBoardNo(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getMember().getMemberName(),
                board.getMember().getMemberId(),
                board.getMember().getMemberNo(),
                board.getUpdateTime(),
                board.getCreateTime(),
                board.getBoardViewCount()
        );
    }
}
