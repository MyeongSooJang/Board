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
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Boolean isDeleted;
    private LocalDateTime deleteTime;
    private Boolean deletedByReport;

    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getMember().getName(),
                board.getMember().getUsername(),
                board.getUpdateTime(),
                board.getCreateTime(),
                board.getViewCount(),
                board.getLikeCount(),
                board.getCommentCount(),
                board.getDeleteTime() != null,
                board.getDeleteTime(),
                false
        );
    }

    public void setDeletedByReport(boolean deletedByReport) {
        this.deletedByReport = deletedByReport;
    }
}
