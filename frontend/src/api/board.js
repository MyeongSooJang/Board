import client from './client'

export const boardApi = {
  // 게시판 목록 조회 (페이징)
  getList: (page = 0, size = 10) =>
    client.get('/boards', { params: { page, size } }),

  // 게시물 상세 조회
  getDetail: (boardNo) =>
    client.get(`/boards/${boardNo}`),

  // 제목으로 검색
  searchByTitle: (title, page = 0, size = 10) =>
    client.get('/boards/search/title', { params: { title, page, size } }),

  // 작성자로 검색
  searchByWriter: (writer, page = 0, size = 10) =>
    client.get('/boards/search/writer', { params: { writer, page, size } }),

  // 키워드 검색 (제목+내용)
  searchByKeyword: (keyword, page = 0, size = 10) =>
    client.get('/boards/search/keyword', { params: { keyword, page, size } }),

  // 게시물 작성
  create: (boardTitle, boardContent) =>
    client.post('/boards', { boardTitle, boardContent }),

  // 게시물 수정
  update: (boardNo, boardTitle, boardContent) =>
    client.put(`/boards/${boardNo}`, { boardTitle, boardContent }),

  // 게시물 삭제
  delete: (boardNo) =>
    client.delete(`/boards/${boardNo}`)
}
