package com.project.community.board.dto;

import lombok.Getter;

@Getter
public enum SearchType {
    TITLE("제목"),
    CONTENT("내용"),
    TITLE_AND_CONTENT("제목 + 내용"),
    WRITER("작성자");

    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
