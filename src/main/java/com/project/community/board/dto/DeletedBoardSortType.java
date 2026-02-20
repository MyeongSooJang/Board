package com.project.community.board.dto;

import lombok.Getter;


@Getter
public enum DeletedBoardSortType {
    LATEST("latest"),
    VIEWCOUNT("viewcount"),
    LIKECOUNT("likecount"),
    COMMENTCOUNT("commentcount");

    private final String value;

    DeletedBoardSortType(String value) {
        this.value = value;
    }

    public static DeletedBoardSortType fromSort(String sort) {
        for (DeletedBoardSortType sortType : values()) {
            if (sortType.value.equalsIgnoreCase(sort)) {
                return sortType;
            }
        }
        return LATEST;
    }
}
