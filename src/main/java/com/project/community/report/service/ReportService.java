package com.project.community.report.service;

import static com.project.community.report.entity.Report.createReport;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import com.project.community.report.dto.ReportAdminResponse;
import com.project.community.report.dto.ReportRequest;
import com.project.community.report.dto.ReportResponse;
import com.project.community.report.entity.Report;
import com.project.community.report.repository.ReportRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.ValidationException;
import com.project.community.exception.dto.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public ReportResponse submitReport(ReportRequest request, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        Report report = createReport(member, board, request);
        Report savedReport = reportRepository.save(report);
        return ReportResponse.from(savedReport);
    }

    public Page<ReportAdminResponse> findAllReports(Pageable pageable) {
        Page<Report> reports = reportRepository.findAll(pageable);
        return reports.map(ReportAdminResponse::from);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ReportAdminResponse approve(Long reportId) {
        Report report = searchReportById(reportId);
        validateSubmit(report);

        Report approveReport = report.approve();
        reportRepository.save(approveReport);

        Board board = approveReport.getBoard();
        board.softDelete();
        boardRepository.save(board);


        return ReportAdminResponse.from(approveReport);
    }

    private Report searchReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REPORT_NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ReportAdminResponse reject(Long reportId) {
        Report report = searchReportById(reportId);
        validateSubmit(report);

        Report rejectReport = report.reject();
        reportRepository.save(rejectReport);

        return ReportAdminResponse.from(rejectReport);
    }

    private void validateSubmit(Report report) {
        if (!report.getStatus().equals("SUBMITTED")) {
            throw new ValidationException(ErrorCode.INVALID_REPORT_STATUS);
        }
    }
}
