package com.project.community.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String username;
    private String memberName;
    private String role;


    public LoginResponse(String accessToken, String refreshToken, Long expiresIn, String username, String memberName, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.username = username;
        this.memberName = memberName;
        this.role = role;
    }

}
