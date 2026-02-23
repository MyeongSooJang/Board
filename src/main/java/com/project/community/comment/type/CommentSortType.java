package com.project.community.comment.type;

public enum CommentSortType {
    LATEST("최신순"),
    TOP("인기순");

    private final String description;

    CommentSortType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CommentSortType from(String value) {
        try {
            return CommentSortType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LATEST;
        }
    }
}
