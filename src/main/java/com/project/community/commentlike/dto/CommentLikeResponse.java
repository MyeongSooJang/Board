package com.project.community.commentlike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeResponse {
    private Long commentId;
    private Long likeCount;
    private Boolean liked;
}
