package com.project.community.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlResponse {

    /**
     * 프론트엔드가 PUT 요청으로 업로드할 임시 URL (15분 유효)
     */
    private String uploadUrl;

    /**
     * 업로드 완료 후 게시글에 포함할 최종 이미지 URL (영구 접근 가능)
     */
    private String imageUrl;

    /**
     * S3에 저장된 고유 키 (나중에 삭제 시 필요)
     */
    private String s3Key;
}
