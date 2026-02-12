package com.project.community.auth.dto;

import lombok.Getter;

@Getter

public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;

    public RefreshTokenResponse(String accessToken, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
