import client from './client'

export const commentApi = {
  // 댓글 목록 조회
  getList: (boardId) =>
    client.get(`/boards/${boardId}/comment`),

  // 댓글 작성
  create: (boardId, commentContent, memberId, commentParentId = null) =>
    client.post(`/boards/${boardId}/comment`, {
      commentContent,
      memberId,
      boardId,
      ...(commentParentId && { commentParentId })
    }),

  // 댓글 수정
  update: (boardId, commentId, commentContent) =>
    client.put(`/boards/${boardId}/comment/${commentId}`, { commentContent }),

  // 댓글 삭제
  delete: (boardId, commentId) =>
    client.delete(`/boards/${boardId}/comment/${commentId}`)
}
