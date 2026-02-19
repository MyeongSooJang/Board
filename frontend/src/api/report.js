import client from './client'

export const reportApi = {
  // 게시글 신고 제출
  submitReport: (boardId, type, content, targetType = 'BOARD') =>
    client.post('/report', { boardId, commentId: null, type, content, targetType }),

  // 댓글 신고 제출
  reportComment: (commentId, type, content) =>
    client.post('/report', { boardId: null, commentId, type, content, targetType: 'COMMENT' }),

  // 관리자 - 신고 목록 조회
  getReportList: (page = 0, size = 10, sort = 'createTime,desc') =>
    client.get('/report/admin', { params: { page, size, sort } }),

  // 관리자 - 신고 승인
  approveReport: (reportId) =>
    client.patch(`/report/${reportId}/approve`),

  // 관리자 - 신고 거절
  rejectReport: (reportId) =>
    client.patch(`/report/${reportId}/reject`)
}
