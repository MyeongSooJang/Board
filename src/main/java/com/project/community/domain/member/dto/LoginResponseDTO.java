package com.project.community.domain.member.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}
