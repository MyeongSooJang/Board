package com.project.community.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginRequestDTO {
    private String memberId;
    private String password;
}
