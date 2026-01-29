package com.project.community.domain.report.controller;

import com.project.community.domain.report.dto.ReportAdminDTO;
import com.project.community.domain.report.dto.ReportRequestDTO;
import com.project.community.domain.report.dto.ReportResponseDTO;
import com.project.community.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(
            @RequestBody ReportRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ReportResponseDTO response = reportService.submitReport(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<Page<ReportAdminDTO>> findAllReports(
            @PageableDefault(size = 10, sort = "createTime", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(reportService.findAllReports(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/approve")
    public ResponseEntity<ReportAdminDTO> approveReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.approve(reportId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{reportId}/reject")
    public ResponseEntity<ReportAdminDTO> rejectReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.reject(reportId));
    }

}
