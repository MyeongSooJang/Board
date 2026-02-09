package com.project.community.report.dto;

import com.project.community.report.entity.Report;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReportResponseDTO {
    private Long reportId;
    private String type;
    private String content;
    private String status;
    private Long reporterId;
    private String reporterName;
    private Long boardId;
    private String boardTitle;
    private LocalDateTime createTime;

    public static ReportResponseDTO from(Report report) {
        return new ReportResponseDTO(
                report.getReportId(),
                report.getType(),
                report.getContent(),
                report.getStatus(),
                report.getMember().getMemberId(),
                report.getMember().getName(),
                report.getBoard().getBoardId(),
                report.getBoard().getBoardTitle(),
                report.getCreateTime()
        );
    }

}
