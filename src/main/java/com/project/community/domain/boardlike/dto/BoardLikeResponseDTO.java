package com.project.community.domain.boardlike.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BoardLikeResponseDTO {
    private Long boardId;
    private Long likeCount;
    private boolean liked;
}
