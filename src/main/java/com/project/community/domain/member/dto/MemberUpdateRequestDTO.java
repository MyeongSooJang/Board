package com.project.community.domain.member.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberUpdateRequestDTO {
    @Size(min = 2, max = 50, message = "이름은 2~50자여야 합니다")
    private String memberName;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String memberPwd;

    @Min(value = 18, message = "나이는 18세 이상이어야 합니다")
    @Max(value = 150, message = "나이는 150 이하여야 합니다")
    private Integer memberAge;

    @Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "휴대폰 형식: 010-1234-5678")
    private String memberPhone;
}
