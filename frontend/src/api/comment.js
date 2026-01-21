import client from './client'

export const commentApi = {
  // 댓글 목록 조회
  getList: (boardNo) =>
    client.get(`/${boardNo}/comment`),

  // 댓글 작성
  create: (boardNo, commentContent, memberNo, commentParentNo = null) =>
    client.post(`/${boardNo}/comment`, {
      commentContent,
      memberNo,
      ...(commentParentNo && { commentParentNo })
    }),

  // 댓글 수정
  update: (boardNo, commentNo, commentContent) =>
    client.put(`/${boardNo}/comment/${commentNo}`, { commentContent }),

  // 댓글 삭제
  delete: (boardNo, commentNo) =>
    client.delete(`/${boardNo}/comment/${commentNo}`)
}
