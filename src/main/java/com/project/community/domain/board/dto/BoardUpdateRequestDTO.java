package com.project.community.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardUpdateRequestDTO {
    private String boardTitle;
    private String boardContent;
}
