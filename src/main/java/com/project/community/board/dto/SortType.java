package com.project.community.board.dto;

import lombok.Getter;

@Getter
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

    public static SortType fromSort(String sort) {
        for (SortType sortType : values()) {
            if (sortType.value.equalsIgnoreCase(sort)) {
                return sortType;
            }
        }
        return LATEST;
    }
}
