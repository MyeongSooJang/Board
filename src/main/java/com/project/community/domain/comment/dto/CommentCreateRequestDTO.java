package com.project.community.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentCreateRequestDTO {
    private Long commentParentNo;
    private String commentContent;
    private Long memberNo;
}
