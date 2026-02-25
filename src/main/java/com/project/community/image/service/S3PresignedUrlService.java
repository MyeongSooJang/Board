package com.project.community.image.service;

import com.project.community.image.dto.PresignedUrlRequest;
import com.project.community.image.dto.PresignedUrlResponse;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3PresignedUrlService {

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.presigned-url-expiration}")
    private int expirationSeconds;


    /**
     * 1. 파일 확장자 추출
     * 2. S3 키 생성: images/{UUID}.{ext}
     * 3. 업로드 명세서 정의 (버킷, 키, Content-Type)
     *    - contentType으로 업로드 가능한 파일 타입 제한
     * 4. Presign 요청: 15분 유효한 임시 URL 생성 요청
     * 5. Presigned URL 객체: 실제 생성된 임시 URL (서명 포함)
     * 6. 영구 접속 URL: 언제든 이미지 조회 가능
     *
     * @param request 파일명, Content-Type (백엔드에서 이미지 타입만 필터링됨)
     * @return uploadUrl(presigned URL, 15분 유효),
     *         imageUrl(영구 공개 URL),
     *         s3Key(삭제할 때 필요)
     */
    public PresignedUrlResponse generatePresignedUrl(PresignedUrlRequest request) {

        String extension = extractExtension(request.getFileName());

        String s3Key = "images/" + UUID.randomUUID() + "." + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType(request.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirationSeconds))
                .putObjectRequest(putObjectRequest)
                .build();


        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String uploadUrl = presignedRequest.url().toString();

        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName, region, s3Key);

        log.info("Presigned URL 생성 완료 - S3 Key: {}, 만료: {}초", s3Key, expirationSeconds);


        return new PresignedUrlResponse(uploadUrl, imageUrl, s3Key);
    }

    /**
     * 파일명에서 확장자 추출
     * 예: "photo.jpg" → "jpg", "image.jpeg" → "jpeg"
     */
    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "jpg"; // 기본값
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
