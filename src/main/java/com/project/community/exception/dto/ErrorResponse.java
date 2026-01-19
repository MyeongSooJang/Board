package com.project.community.exception.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String errorCode;
    private String message;
    private int httpStatus;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ErrorResponse(ErrorCode errorCode) {
        this(
                errorCode.name(),
                errorCode.getMessage(),
                errorCode.getHttpStatus(),
                LocalDateTime.now(),
                null
        );
    }
}
