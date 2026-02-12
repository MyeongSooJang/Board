package com.project.community.member.dto;

import com.project.community.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberResponse {
    private Long memberId;
    private String username;
    private String name;
    private Integer age;
    private String email;
    private String phone;
    private String role;

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getMemberId(),
                member.getUsername(),
                member.getName(),
                member.getAge(),
                member.getEmail(),
                member.getPhone(),
                member.getRole());
    }
}
