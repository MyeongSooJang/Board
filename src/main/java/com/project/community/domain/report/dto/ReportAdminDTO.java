package com.project.community.domain.report.dto;

import com.project.community.domain.report.entity.Report;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ReportAdminDTO {
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

    public static ReportAdminDTO from(Report report) {
        return new ReportAdminDTO(
                report.getReportId(),
                report.getType(),
                report.getContent(),
                report.getStatus(),
                report.getMember().getUsername(),
                report.getMember().getName(),
                report.getBoard().getBoardId(),
                report.getBoard().getBoardTitle(),
                report.getBoard().getMember().getUsername(),
                report.getBoard().getMember().getName(),
                report.getCreateTime()
        );
    }
}
