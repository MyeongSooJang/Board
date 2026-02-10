package com.project.community.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardSearchRequest {
    private String title;
    private String writer;
    private String description;
    private String keyword;
    private SearchType searchType;
}
