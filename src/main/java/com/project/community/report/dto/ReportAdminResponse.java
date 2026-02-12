package com.project.community.report.dto;

import com.project.community.report.entity.Report;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ReportAdminResponse {
    private Long reportId;
    private String type;
    private String content;
    private String status;
    private String reporterUsername;
    private String reporterName;
    private Long boardId;
    private String boardTitle;
    private String reportedUsername;
    private String reportedName;
    private LocalDateTime reportedDate;

    public static ReportAdminResponse from(Report report) {
        return new ReportAdminResponse(
                report.getReportId(),
                report.getType(),
                report.getContent(),
                report.getStatus(),
                report.getMember().getUsername(),
                report.getMember().getName(),
                report.getBoard().getBoardId(),
                report.getBoard().getTitle(),
                report.getBoard().getMember().getUsername(),
                report.getBoard().getMember().getName(),
                report.getCreateTime()
        );
    }
}
