package com.project.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentCreateRequestDTO {
    private Long commentParentId;
    private String commentContent;
    private Long boardId;
    private Long memberId;
}
