package com.project.community.board.dto;

import com.project.community.board.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardResponse {
    private Long boardId;
    private String boardTitle;
    private String boardContent;
    private String memberName;
    private Long memberId;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    private Long boardViewCount;
    private Long boardLikeCount;
    private Long boardCommentCount;

    public static BoardResponse from(Board board, Long boardLikeCount, Long boardCommentCount) {
        return new BoardResponse(
                board.getBoardId(),
                board.getBoardTitle(),
                board.getBoardContent(),
                board.getMember().getName(),
                board.getMember().getMemberId(),
                board.getUpdateTime(),
                board.getCreateTime(),
                board.getBoardViewCount(),
                boardLikeCount,
                boardCommentCount
        );
    }
}
