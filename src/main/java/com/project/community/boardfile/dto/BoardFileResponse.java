package com.project.community.boardfile.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardFileResponse {
    private Long fileId;
    private String url;
    private LocalDateTime uploadTime;
}
