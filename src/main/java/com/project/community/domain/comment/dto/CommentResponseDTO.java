package com.project.community.domain.comment.dto;

import com.project.community.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponseDTO {
    private Long commentId;
    private String commentContent;
    private Long commentParentId;
    private String memberName;
    private Long memberId;
    private LocalDateTime createTime;

    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(comment.getCommentId(),
                comment.getCommentContent(),
                comment.getCommentParentId(),
                comment.getMember().getName(),
                comment.getMember().getMemberId(),
                comment.getCreateTime());
    }
}
