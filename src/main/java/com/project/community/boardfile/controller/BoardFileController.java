package com.project.community.boardfile.controller;

import com.project.community.boardfile.dto.BoardFileResponse;
import com.project.community.boardfile.service.BoardFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/files")
@Tag(name = "게시글 파일", description = "게시글 파일 업로드 및 관리 API")
public class BoardFileController {

    private final BoardFileService boardFileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드", description = "게시글에 파일 업로드")
    public ResponseEntity<BoardFileResponse> uploadFile(
            @Parameter(description = "게시글 ID")
            @PathVariable Long boardId,
            @Parameter(description = "업로드할 파일")
            @RequestParam("file") MultipartFile file) throws IOException {
        BoardFileResponse response = boardFileService.upload(boardId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "게시글의 파일 목록 조회", description = "해당 게시글에 업로드된 모든 파일 조회")
    public ResponseEntity<List<BoardFileResponse>> getFiles(
            @Parameter(description = "게시글 ID")
            @PathVariable Long boardId) {
        List<BoardFileResponse> files = boardFileService.findFiles(boardId);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "파일 삭제", description = "특정 파일 삭제 (디스크 + DB)")
    public ResponseEntity<Void> deleteFile(
            @Parameter(description = "게시글 ID")
            @PathVariable Long boardId,
            @Parameter(description = "파일 ID")
            @PathVariable Long fileId) throws IOException {
        boardFileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}