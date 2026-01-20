package com.project.community.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberCreateRequestDTO {
    @NotBlank(message = "아이디는 필수입니다")
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다")
    private String memberId;

    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 20, message = "이름은 2~20자여야 합니다")
    private String memberName;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 8~20자이며, 대문자, 소문자, 숫자, 특수문자(@$!%*?&)를 각각 1개 이상 포함해야 합니다"
    )
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String memberPwd;

    @NotNull(message = "나이는 필수입니다")
    @Min(value = 10, message = "나이는 10세 이상이어야 합니다")
    @Max(value = 150, message = "나이는 150 이하여야 합니다")
    private Integer memberAge;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    private String memberEmail;

    @NotBlank(message = "전화번호는 필수입니다")
    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "휴대폰 형식: 010-1234-5678")
    private String memberPhone;

    @NotBlank(message = "역할은 필수입니다")
    private String memberRole;
}
