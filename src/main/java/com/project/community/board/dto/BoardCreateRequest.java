package com.project.community.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardCreateRequest {
    private String boardTitle;
    private String boardContent;
    private Long memberId;
}
