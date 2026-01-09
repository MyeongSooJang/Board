package com.project.community.domain.member.dto;

import com.project.community.domain.member.entity.Member;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemberResponseDTO {
    private Long memberNo;
    private String memberId;
    private String memberName;
    private Integer memberAge;
    private String memberEmail;
    private String memberPhone;
    private String memberRole;

    public static MemberResponseDTO from(Member member) {
        return new MemberResponseDTO(member.getMemberNo(),
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberAge(),
                member.getMemberEmail(),
                member.getMemberPhone(),
                member.getMemberRole());
    }
}
