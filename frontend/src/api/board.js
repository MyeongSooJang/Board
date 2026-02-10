import client from './client'

export const boardApi = {
  // 게시판 목록 조회 (페이징)
  getList: (page = 0, size = 10) =>
    client.get('/boards', { params: { page, size } }),

  // 게시물 상세 조회
  getDetail: (boardId) =>
    client.get(`/boards/${boardId}`),

  // 통합 검색 (제목, 내용, 제목+내용, 작성자)
  search: (searchType, query, page = 0, size = 10, sort = 'createTime,desc') =>
    client.get('/boards/search', {
      params: {
        searchType,
        ...(searchType === 'TITLE' && { title: query }),
        ...(searchType === 'CONTENT' && { keyword: query }),
        ...(searchType === 'TITLE_AND_CONTENT' && { keyword: query }),
        ...(searchType === 'WRITER' && { writer: query }),
        page,
        size,
        sort
      }
    }),

  // 게시물 작성
  create: (boardTitle, boardContent, memberId) =>
    client.post('/boards', { boardTitle, boardContent, memberId }),

  // 게시물 수정
  update: (boardId, boardTitle, boardContent) =>
    client.put(`/boards/${boardId}`, { boardTitle, boardContent }),

  // 게시물 삭제
  delete: (boardId) =>
    client.delete(`/boards/${boardId}`),

  // 좋아요 토글
  toggleLike: (boardId) =>
    client.post(`/boards/${boardId}/like`),

  // 좋아요 상태 조회
  getLikeStatus: (boardId) =>
    client.get(`/boards/${boardId}/like`)
}
