package com.project.community.exception.handler;

import com.project.community.exception.DuplicateException;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.TokenException;
import com.project.community.exception.ValidationException;
import com.project.community.exception.dto.ErrorCode;
import com.project.community.exception.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode);
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode);
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode);
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode);
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getHttpStatus())
                .body(new ErrorResponse(ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(ErrorCode.FORBIDDEN.getHttpStatus())
                .body(new ErrorResponse(ErrorCode.FORBIDDEN));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = getErrors(ex);

        ErrorResponse response = new ErrorResponse(
                ErrorCode.VALIDATION_ERROR.name(),
                ErrorCode.VALIDATION_ERROR.getMessage(),
                ErrorCode.VALIDATION_ERROR.getHttpStatus(),
                LocalDateTime.now(),
                fieldErrors
        );
        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getHttpStatus())
                .body(response);
    }

    private static Map<String, String> getErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        return fieldErrors;
    }

}
