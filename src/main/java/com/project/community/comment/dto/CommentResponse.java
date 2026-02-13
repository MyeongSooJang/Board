package com.project.community.comment.dto;

import com.project.community.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long parentId;
    private String name;
    private Long memberId;
    private LocalDateTime createTime;


    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getCommentId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getMember().getName(),
                comment.getMember().getMemberId(),
                comment.getCreateTime());
    }
}
