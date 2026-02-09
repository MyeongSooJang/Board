package com.project.community.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReportRequestDTO {
    private Long boardId;
    private String type;
    private String content;
}
