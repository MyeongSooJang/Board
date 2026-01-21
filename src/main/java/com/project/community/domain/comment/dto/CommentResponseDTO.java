package com.project.community.domain.comment.dto;

import com.project.community.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentResponseDTO {
    private Long commentNo;
    private String commentContent;
    private Long commentParentNo;
    private String memberName;
    private String memberId;
    private Long memberNo;
    private LocalDateTime createdTime;

    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(comment.getCommentNo(),
                comment.getCommentContent(),
                comment.getCommentParentNo(),
                comment.getMember().getMemberName(),
                comment.getMember().getMemberId(),
                comment.getMember().getMemberNo(),
                comment.getCreateTime());
    }
}
