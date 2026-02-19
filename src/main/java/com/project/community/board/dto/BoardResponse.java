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
    private String memberUsername;
    private Long memberId;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    private Long boardViewCount;
    private Long boardLikeCount;
    private Long boardCommentCount;
    private Boolean isDeleted;

    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getMember().getName(),
                board.getMember().getUsername(),
                board.getMember().getMemberId(),
                board.getUpdateTime(),
                board.getCreateTime(),
                board.getViewCount(),
                board.getLikeCount(),
                board.getCommentCount(),
                board.getDeleteTime() != null
        );
    }
}
