package com.project.community.domain.comment.controller;

import com.project.community.domain.comment.dto.CommentCreateRequestDTO;
import com.project.community.domain.comment.dto.CommentResponseDTO;
import com.project.community.domain.comment.service.CommentService;
import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/{boardNo}/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> findAllByBoardNo(@PathVariable Long boardNo) {
        List<CommentResponseDTO> comments = commentService.findAllCommentsByBoardNo(boardNo);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable("boardNo") String boardNo,
                                                            @RequestBody CommentCreateRequestDTO comment) {
        CommentResponseDTO response = commentService.createComment(comment);
        return ResponseEntity.created(
                URI.create("/comments/" + response.getCommentNo())
        ).body(response);
    }

}
