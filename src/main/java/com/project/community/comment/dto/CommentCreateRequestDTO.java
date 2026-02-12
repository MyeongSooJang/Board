package com.project.community.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentCreateRequestDTO {
    private Long boardId;
    private Long commentParentId;
    private String commentContent;
}
