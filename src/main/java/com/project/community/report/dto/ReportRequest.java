package com.project.community.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportRequest {
    private Long boardId;
    private Long commentId;
    private String type;
    private String content;
    private String targetType;
}
