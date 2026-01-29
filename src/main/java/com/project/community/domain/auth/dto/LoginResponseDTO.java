package com.project.community.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private Long memberId;
    private String memberName;
    private String role;


    public LoginResponseDTO(String accessToken, String refreshToken, Long expiresIn, Long memberId, String memberName, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.memberId = memberId;
        this.memberName = memberName;
        this.role = role;
    }

}
