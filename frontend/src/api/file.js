import client from './client'

/**
 * 파일 업로드
 * @param {number} boardId - 게시글 ID
 * @param {File} file - 업로드할 파일
 * @returns {Promise<Object>} 파일 응답 (fileId, url, uploadTime)
 */
export const uploadFile = async (boardId, file) => {
  const formData = new FormData()
  formData.append('file', file)

  try {
    const response = await client.post(`/boards/${boardId}/files`, formData)
    return response.data
  } catch (error) {
    console.error('파일 업로드 실패:', error)
    throw error
  }
}

/**
 * 게시글의 파일 목록 조회
 * @param {number} boardId - 게시글 ID
 * @returns {Promise<Array>} 파일 목록
 */
export const getFiles = async (boardId) => {
  try {
    const response = await client.get(`/boards/${boardId}/files`)
    return response.data
  } catch (error) {
    console.error('파일 목록 조회 실패:', error)
    throw error
  }
}

/**
 * 파일 삭제
 * @param {number} boardId - 게시글 ID
 * @param {number} fileId - 파일 ID
 * @returns {Promise<void>}
 */
export const deleteFile = async (boardId, fileId) => {
  try {
    await client.delete(`/boards/${boardId}/files/${fileId}`)
  } catch (error) {
    console.error('파일 삭제 실패:', error)
    throw error
  }
}

export default {
  uploadFile,
  getFiles,
  deleteFile
}
