package com.project.community.domain.member.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}
