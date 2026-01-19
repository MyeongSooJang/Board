package com.project.community.exception;

import com.project.community.exception.dto.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateException extends CustomException {
    public DuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
