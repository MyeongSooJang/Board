package com.project.community.domain.comment.controller;

import com.project.community.domain.comment.dto.CommentCreateRequestDTO;
import com.project.community.domain.comment.dto.CommentResponseDTO;
import com.project.community.domain.comment.dto.CommentUpdateRequestDTO;
import com.project.community.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/{boardNo}/comment")
@Tag(name = "댓글", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "모든 댓글 조회",
            description = "게시글 번호에 대한 전체 댓글을 조회")
    public ResponseEntity<List<CommentResponseDTO>> findAllByBoardNo(
            @Parameter(description = "게시글 번호")
            @PathVariable Long boardNo) {
        List<CommentResponseDTO> comments = commentService.findAllCommentsByBoardNo(boardNo);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @Operation(summary = "댓글 작성",
            description = "게시글에 대한 댓글 작성")
    public ResponseEntity<CommentResponseDTO> createComment(
            @Parameter(description = "게시글 번호")
            @PathVariable("boardNo") Long boardNo,
            @Parameter(description = "생성할 댓글 내용")
            @RequestBody CommentCreateRequestDTO comment) {
        CommentResponseDTO response = commentService.createComment(boardNo, comment);
        return ResponseEntity.created(
                URI.create("/comments/" + response.getCommentNo())
        ).body(response);
    }

    @PutMapping("/{commentNo}")
    @Operation(summary = "댓글 수정",
            description = "해당하는 댓글번호 댓글 수정")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @Parameter(description = "댓글 번호")
            @PathVariable("commentNo") Long commentNo,
            @Parameter(description = "수정할 댓글 내용")
            @RequestBody CommentUpdateRequestDTO comment) {
        return ResponseEntity.ok(commentService.updateComment(commentNo, comment));
    }

    @DeleteMapping("/{commentNo}")
    @Operation(summary = "댓글 삭제",
            description = "해당하는 댓글번호 댓글 삭제")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "댓글 번호")
            @PathVariable("commentNo") Long commentNo) {
        commentService.deleteComment(commentNo);
        return ResponseEntity.ok().build();
    }
}
