package com.project.community.domain.member.entity;

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
}
