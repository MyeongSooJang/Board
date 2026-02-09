package com.project.community.boardfile.dto;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
public class BoardFileRequestDTO {
    private Long boardId;
    private MultipartFile file;
}
