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
    private Long memberId;

    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    private Integer age;

    private String email;

    @Email
    private String phone;

    private String role = "GUEST";


    public Member(String username, String password, String name, Integer age, String email, String phone, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public static Member createMember(String encodingPassword, MemberCreateRequestDTO request) {
        return new Member(
                request.getUsername(),
                encodingPassword,
                request.getName(),
                request.getAge(),
                request.getEmail(),
                request.getPhone(),
                request.getRole());
    }

    public Member updateMember(String encodingPassword, MemberUpdateRequestDTO request) {
        this.name = request.getName();
        this.password = encodingPassword;
        this.age = request.getAge();
        this.phone = request.getPhone();
        return this;
    }
}
