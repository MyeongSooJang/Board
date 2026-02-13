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
    private String memberName;
    private String memberUsername;
    private Long memberId;
    private Long likeCount;
    private Boolean liked;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getMember().getName(),
                comment.getMember().getUsername(),
                comment.getMember().getMemberId(),
                comment.getLikeCount(),
                false,
                comment.getCreateTime(),
                comment.getUpdateTime());
    }

    public static CommentResponse from(Comment comment, Boolean liked) {
        return new CommentResponse(
                comment.getCommentId(),
                comment.getContent(),
                comment.getParentId(),
                comment.getMember().getName(),
                comment.getMember().getUsername(),
                comment.getMember().getMemberId(),
                comment.getLikeCount(),
                liked,
                comment.getCreateTime(),
                comment.getUpdateTime());
    }
}
