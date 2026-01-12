package com.project.community.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequestDTO {
    private String memberId;
    private String password;
}
