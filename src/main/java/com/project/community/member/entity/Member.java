package com.project.community.member.entity;

import com.project.community.member.dto.MemberCreateRequest;
import com.project.community.member.dto.MemberUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.community.common.BaseEntity;

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

    @Email
    private String email;

    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "휴대폰 형식: 010-1234-5678")
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

    public static Member createMember(String encodingPassword, MemberCreateRequest request) {
        return new Member(
                request.getUsername(),
                encodingPassword,
                request.getName(),
                request.getAge(),
                request.getEmail(),
                request.getPhone(),
                request.getRole());
    }

    public Member update(String encodingPassword, MemberUpdateRequest request) {
        if (request.getName() != null && !request.getName().isEmpty()) {
            this.name = request.getName();
        }
        if (encodingPassword != null && !encodingPassword.isEmpty()) {
            this.password = encodingPassword;
        }
        if (request.getAge() != null) {
            this.age = request.getAge();
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            this.phone = request.getPhone();
        }
        return this;
    }

    public void softDelete() {
        this.deleteTime = LocalDateTime.now();
    }
}
