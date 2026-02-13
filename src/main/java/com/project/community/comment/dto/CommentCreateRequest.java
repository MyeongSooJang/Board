package com.project.community.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentCreateRequest {
    private Long boardId;
    private Long parentId;
    private String content;
}
