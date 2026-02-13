import client from './client'

export const commentApi = {
  // 댓글 목록 조회 (정렬 옵션 포함)
  getList: (boardId, sort = 'latest', page = 0, size = 15) =>
    client.get(`/boards/${boardId}/comments`, {
      params: { sort, page, size }
    }),

  // 댓글 검색
  search: (boardId, keyword, page = 0, size = 15) =>
    client.get(`/boards/${boardId}/comments/search`, {
      params: { keyword, page, size }
    }),

  // 댓글 작성
  create: (boardId, content, parentId = null) =>
    client.post(`/boards/${boardId}/comments`, {
      content,
      ...(parentId && { parentId })
    }),

  // 댓글 수정
  update: (boardId, commentId, content) =>
    client.put(`/boards/${boardId}/comments/${commentId}`, { content }),

  // 댓글 삭제
  delete: (boardId, commentId) =>
    client.delete(`/boards/${boardId}/comments/${commentId}`),

  // 댓글 좋아요 상태 조회
  getLikeStatus: (boardId, commentId) =>
    client.get(`/boards/${boardId}/comments/${commentId}/likes`),

  // 댓글 좋아요 토글
  toggleLike: (boardId, commentId) =>
    client.post(`/boards/${boardId}/comments/${commentId}/likes`),

  // 댓글 좋아요 삭제
  deleteLike: (boardId, commentId) =>
    client.delete(`/boards/${boardId}/comments/${commentId}/likes`),

  // 댓글 신고
  reportComment: (boardId, commentId, reportRequest) =>
    client.post(`/boards/${boardId}/comments/${commentId}/reports`, reportRequest)
}
