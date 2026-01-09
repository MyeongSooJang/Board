package com.project.community.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberUpdateRequestDTO {
    private String memberName;
    private String memberPwd;
    private Integer memberAge;
    private String memberPhone;
}
