package com.project.community.exception;

import com.project.community.exception.dto.ErrorCode;

public class TokenException extends CustomException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
