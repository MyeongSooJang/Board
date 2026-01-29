import client from './client'

export const reportApi = {
  // 신고 제출
  submitReport: (boardId, type, content) =>
    client.post('/report', { boardId, type, content }),

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
