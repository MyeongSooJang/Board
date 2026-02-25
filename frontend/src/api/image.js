import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api/images'

/**
 * Presigned URL 발급 요청
 * @param {string} fileName - 파일명 (예: "photo.jpg")
 * @param {string} contentType - MIME 타입 (예: "image/jpeg")
 * @returns {Promise<{uploadUrl: string, imageUrl: string, s3Key: string}>}
 */
export const getPresignedUrl = async (fileName, contentType) => {
  const response = await axios.post(`${API_BASE_URL}/presigned-url`, {
    fileName,
    contentType
  })
  return response.data
}

/**
 * S3에 이미지 직접 업로드 (PUT 요청)
 * @param {string} uploadUrl - presigned URL
 * @param {File} file - 업로드할 파일 객체
 * @param {string} contentType - MIME 타입
 * @returns {Promise<void>}
 */
export const uploadToS3 = async (uploadUrl, file, contentType) => {
  await axios.put(uploadUrl, file, {
    headers: {
      'Content-Type': contentType
    },
    // presigned URL은 AWS 엔드포인트이므로 인증 헤더 제거
    transformRequest: [(data, headers) => {
      delete headers.common['Authorization']
      return data
    }]
  })
}
