package com.project.community.domain.member.entity;

import com.project.community.domain.member.dto.MemberCreateRequestDTO;
import com.project.community.domain.member.dto.MemberUpdateRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.community.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor

public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(unique = true)
    private String memberId;

    private String memberName;

    private String memberPwd;

    private Integer memberAge;

    @Email
    private String memberEmail;

    private String memberPhone;

    private String memberRole;

    public Member(String memberId,
                  String memberName,
                  String memberPwd,
                  Integer memberAge,
                  String memberEmail,
                  String memberPhone,
                  String memberRole) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPwd = memberPwd;
        this.memberAge = memberAge;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.memberRole = memberRole;
    }

    public static Member createMember(MemberCreateRequestDTO request) {
        return new Member(request.getMemberId(),
                          request.getMemberName(),
                          request.getMemberPwd(),
                          request.getMemberAge(),
                          request.getMemberEmail(),
                          request.getMemberPhone(),
                          request.getMemberRole());
    }

    public Member updateMember(MemberUpdateRequestDTO request) {
        this.memberName = request.getMemberName();
        this.memberPwd = request.getMemberPwd();
        this.memberAge = request.getMemberAge();
        this.memberPhone = request.getMemberPhone();
        return this;
    }
}
