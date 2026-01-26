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


    public LoginResponseDTO(String accessToken, String refreshToken, Long expiresIn, Long memberId, String memberName) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.memberId = memberId;
        this.memberName = memberName;
    }

}
