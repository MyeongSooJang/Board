package com.project.community.exception;

import com.project.community.exception.dto.ErrorCode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
