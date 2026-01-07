package com.project.community.domain.comment.controller;

import com.project.community.domain.comment.dto.CommentCreateRequestDTO;
import com.project.community.domain.comment.dto.CommentResponseDTO;
import com.project.community.domain.comment.dto.CommentUpdateRequestDTO;
import com.project.community.domain.comment.service.CommentService;
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

    @PutMapping("/{commentNo}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable("commentNo") Long commentNo,
            @RequestBody CommentUpdateRequestDTO comment) {
        return  ResponseEntity.ok(commentService.updateComment(commentNo, comment));
    }

//    @DeleteMapping("/{commentNo}")
//    public ResponseEntity<CommentResponseDTO> deleteComment(@PathVariable("boardNo") Long boardNo,
//                                                            @PathVariable("commentNo") Long commentNo) {
//
//    }
}
