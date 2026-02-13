package com.project.community.boardlike.dto;

import com.project.community.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardLikeResponse {
    private Long boardId;
    private Long likeCount;
    private boolean liked;

    public BoardLikeResponse(Board board) {
        this.boardId = board.getBoardId();
        this.likeCount = board.getLikeCount();

    }
}
