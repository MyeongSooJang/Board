package com.project.community.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 입력 값 검증 오류 400
    VALIDATION_ERROR(400, "입력값 검증에 실패하였습니다"),
    INVALID_MEMBER_ID(400, "아이디는 4~20자여야 합니다"),
    INVALID_MEMBER_NAME(400, "이름은 2~20자여야 합니다"),
    INVALID_PASSWORD(400, "비밀번호는 최소 8자 이상이어야 합니다"),
    INVALID_PASSWORD_PATTERN(400, "비밀번호는 8~20자이며, 대문자, 소문자, 숫자, 특수문자(@$!%*?&)를 각각 1개 이상 포함해야 합니다"),
    INVALID_AGE(400, "나이는 10~150세여야 합니다"),
    INVALID_EMAIL(400, "유효한 이메일 형식이어야 합니다"),
    INVALID_PHONE(400, "휴대폰 형식: 010-1234-5678"),
    MISSING_REQUIRED_FIELDS(400, "필수 필드가 누락되었습니다"),

    // Unauthorized 401
    UNAUTHORIZED(401, "인증이 필요합니다"),
    INVALID_CREDENTIALS(401, "아이디 또는 비밀번호가 일치하지 않습니다"),
    TOKEN_EXPIRED(401, "토큰이 만료되었습니다"),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),
    MALFORMED_TOKEN(401, "잘못된 형식의 토큰입니다"),
    EMPTY_TOKEN(401, "토큰이 없습니다"),


    // 403 Forbidden
    FORBIDDEN(403, "권한이 없습니다"),

    // 404 NotFound
    BOARD_NOT_FOUND(404,"게시글을 찾을 수 없습니다"),
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(404, "댓글을 찾을 수 없습니다"),

    // 비지니스 로직 오류 409
    DUPLICATE_MEMBER_ID(409, "이미 존재하는 아이디입니다"),
    DUPLICATE_EMAIL(409, "이미 존재하는 이메일입니다"),
    DUPLICATE_PHONE(409, "이미 존재하는 전화번호입니다");

    private final int httpStatus;
    private final String message;

}
