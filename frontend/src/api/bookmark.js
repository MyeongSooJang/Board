import client from './client'

export const bookmarkApi = {
  // 북마크 토글 (추가/삭제)
  toggleBookmark: (boardId) =>
    client.post(`/bookmarks/boards/${boardId}`),

  // 북마크 상태 조회
  getStatus: (boardId) =>
    client.get(`/bookmarks/boards/${boardId}/status`),

  // 내 북마크 목록 조회
  getMyBookmarks: (page = 0, size = 10) =>
    client.get('/bookmarks/my-list', {
      params: { page, size }
    }),

  // 북마크 삭제
  deleteBookmark: (boardId) =>
    client.delete(`/bookmarks/boards/${boardId}`),

  // 게시글의 북마크 개수
  getBookmarkCount: (boardId) =>
    client.get(`/bookmarks/boards/${boardId}/count`),

  // 내 북마크 개수
  getMyBookmarkCount: () =>
    client.get('/bookmarks/my-count')
}
