package com.project.community.exception;

import com.project.community.exception.dto.ErrorCode;
import lombok.Getter;

@Getter
public class ValidationException extends CustomException {
    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
