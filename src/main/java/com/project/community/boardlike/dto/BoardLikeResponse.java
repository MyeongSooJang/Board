package com.project.community.boardlike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardLikeResponse {
    private Long boardId;
    private Long likeCount;
    private boolean liked;
}
