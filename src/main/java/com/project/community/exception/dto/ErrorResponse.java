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
    private Map<String, Object> errors;
}
