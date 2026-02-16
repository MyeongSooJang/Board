package com.project.community.bookmark.dto;

import com.project.community.bookmark.entity.Bookmark;
import com.project.community.board.entity.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long markId;
    private Long boardId;
    private String boardTitle;
    private String memberName;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createTime;
    private LocalDateTime bookmarkedAt;

    public static BookmarkResponse from(Bookmark bookmark) {
        Board board = bookmark.getBoard();
        return new BookmarkResponse(
                bookmark.getMarkId(),
                board.getBoardId(),
                board.getTitle(),
                board.getMember().getName(),
                board.getViewCount(),
                board.getLikeCount(),
                board.getCommentCount(),
                board.getCreateTime(),
                bookmark.getCreateTime()
        );
    }
}
