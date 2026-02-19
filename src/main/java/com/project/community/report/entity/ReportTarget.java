package com.project.community.report.entity;

public enum ReportTarget {
    BOARD,
    COMMENT;

    public static ReportTarget changeReportType(String input){
        return ReportTarget.valueOf(input.toUpperCase());
    }
}
