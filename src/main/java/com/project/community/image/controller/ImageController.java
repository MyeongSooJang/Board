package com.project.community.image.controller;

import com.project.community.image.dto.PresignedUrlRequest;
import com.project.community.image.dto.PresignedUrlResponse;
import com.project.community.image.service.S3PresignedUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "이미지 업로드", description = "S3 Presigned URL 기반 이미지 업로드 API")
public class ImageController {

    private final S3PresignedUrlService s3PresignedUrlService;

    /**
     * Presigned URL 발급 API
     * 프론트엔드 업로드 플로우:
     * 1. 이 API 호출 → uploadUrl, imageUrl 받기
     * 2. uploadUrl에 PUT 요청으로 이미지 업로드 (프론트 → S3 직접)
     * 3. imageUrl을 에디터 HTML에 삽입
     * 4. 게시글 저장 시 HTML 전체를 서버로 전송
     * 인증: 불필요 (익명 사용자도 이미지 업로드 가능)
     * 이유: 게시글 작성은 인증 필요하지만, 이미지 미리 업로드는 허용
     */


    @PostMapping("/presigned-url")
    @Operation(
            summary = "Presigned URL 발급",
            description = "이미지 업로드를 위한 S3 presigned URL 생성 (15분 유효)"
    )
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
            @Valid @RequestBody PresignedUrlRequest request) {

        PresignedUrlResponse response = s3PresignedUrlService.generatePresignedUrl(request);
        return ResponseEntity.ok(response);
    }
}
