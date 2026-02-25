package com.project.community.image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUrlRequest {

    @NotBlank(message = "파일명은 필수입니다")
    private String fileName;

    @NotBlank(message = "Content-Type은 필수입니다")
    @Pattern(regexp = "^image/(jpeg|jpg|png|gif|webp)$",
            message = "지원되는 이미지 형식: JPEG, PNG, GIF, WEBP")
    private String contentType;
}
