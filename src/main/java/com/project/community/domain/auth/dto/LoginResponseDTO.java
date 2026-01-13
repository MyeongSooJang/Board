package com.project.community.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType =  "Bearer";
    private Long expiresIn;
    private String memberId;

    public LoginResponseDTO(String accessToken, String refreshToken, Long expiresIn, String memberId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.memberId = memberId;
    }

}
