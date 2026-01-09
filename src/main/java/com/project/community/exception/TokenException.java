package com.project.community.exception;

public class TokenException extends RuntimeException {
    private String code;
    private String message;

    public TokenException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
