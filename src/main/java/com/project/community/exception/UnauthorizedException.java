package com.project.community.exception;

import com.project.community.exception.dto.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
