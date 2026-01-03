package com.project.community.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardCreateRequestDTO {
    private String boardTitle;
    private String boardContent;
    private Long memberNo;
}
