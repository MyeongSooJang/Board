package com.project.community.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberCreateRequestDTO {
    private String memberId;
    private String memberName;
    private String memberPwd;
    private Integer memberAge;
    private String memberEmail;
    private String memberPhone;
    private String memberRole;
}
