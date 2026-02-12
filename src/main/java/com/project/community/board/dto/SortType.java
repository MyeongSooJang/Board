package com.project.community.board.dto;

public enum SortType {
    LATEST("latest"),
    OLDEST("oldest"),
    HOT("hot"),
    VIEWCOUNT("viewcount"),
    LIKECOUNT("likecount"),
    COMMENTCOUNT("commentcount");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
