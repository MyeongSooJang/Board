<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { boardApi } from '../../api/board'

const router = useRouter()
const isLoggedIn = ref(!!localStorage.getItem('accessToken'))

const boards = ref([])
const totalPages = ref(1)
const totalElements = ref(0)
const currentPage = ref(0)
const pageSize = ref(10)
const isLoading = ref(false)
const error = ref('')

const searchType = ref('all')
const searchQuery = ref('')
const sortBy = ref('latest')

const loadBoards = async () => {
  isLoading.value = true
  error.value = ''

  try {
    let response

    if (searchType.value === 'all') {
      response = await boardApi.getList(currentPage.value, pageSize.value, sortBy.value)
    } else {
      response = await boardApi.search(
        searchType.value,
        searchQuery.value,
        currentPage.value,
        pageSize.value,
        sortBy.value
      )
    }

    boards.value = response.data.content || response.data
    totalPages.value = response.data.totalPages || 1
    totalElements.value = response.data.totalElements || boards.value.length

    // 현재 페이지에 게시글이 없으면 이전 페이지로 이동
    if (boards.value.length === 0 && currentPage.value > 0) {
      currentPage.value = currentPage.value - 1
      await loadBoards()
    }
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
  }
}

const isNewPost = (createTime) => {
  if (!createTime) return false
  const now = new Date()
  const postDate = new Date(createTime)
  const diffHours = (now - postDate) / (1000 * 60 * 60)
  return diffHours <= 24
}

const formatDate = (date) => {
  if (!date) return ''
  const postDate = new Date(date)
  const now = new Date()
  const diffHours = (now - postDate) / (1000 * 60 * 60)

  // 24시간 이내: HH:mm
  if (diffHours < 24) {
    return postDate.toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    })
  }

  // 24시간 이후: MM.DD
  return postDate.toLocaleDateString('ko-KR', {
    month: '2-digit',
    day: '2-digit'
  }).replace(/\. /g, '.').replace(/\.$/, '')
}

const getDisplayNumber = (index) => {
  // 전체 게시글 수에서 현재 위치를 빼서 계산
  return totalElements.value - (currentPage.value * pageSize.value + index)
}

const visiblePages = computed(() => {
  const pages = []
  const maxVisible = 10
  let start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2) + 1)
  let end = Math.min(totalPages.value, start + maxVisible - 1)

  if (end - start < maxVisible - 1) {
    start = Math.max(1, end - maxVisible + 1)
  }

  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const handleSearch = () => {
  currentPage.value = 0
  loadBoards()
}

const handleSort = () => {
  currentPage.value = 0
  loadBoards()
}

const goToDetail = (boardId) => {
  router.push(`/boards/${boardId}`)
}

const goToWrite = () => {
  if (!isLoggedIn.value) {
    alert('로그인이 필요합니다')
    router.push('/login')
    return
  }
  router.push('/boards/create')
}

const goToPage = (page) => {
  // 유효한 페이지 범위 체크
  if (page >= 0 && page < totalPages.value && totalElements.value > 0) {
    currentPage.value = page
    loadBoards()
  }
}

onMounted(() => {
  loadBoards()
})
</script>

<template>
  <div class="board-container">
    <!-- 상단 헤더 -->
    <div class="board-header">
      <div class="board-info">
        <h2 class="board-title">자유게시판</h2>
        <span class="board-description">자유롭게 이야기를 나누는 공간입니다</span>
      </div>

      <div class="board-actions">
        <button @click="goToWrite" class="write-btn">
          <span class="write-icon">✏️</span>
          글쓰기
        </button>
      </div>
    </div>

    <!-- 검색 바 -->
    <div class="search-bar">
      <select v-model="searchType" class="search-select">
        <option value="all">전체</option>
        <option value="TITLE">제목</option>
        <option value="CONTENT">내용</option>
        <option value="TITLE_AND_CONTENT">제목+내용</option>
        <option value="WRITER">작성자</option>
      </select>
      <input
        v-model="searchQuery"
        type="text"
        placeholder="검색어를 입력하세요"
        class="search-input"
        @keyup.enter="handleSearch"
      />
      <button @click="handleSearch" class="search-btn">검색</button>
    </div>

    <!-- 정렬 바 -->
    <div class="sort-bar">
      <span class="sort-label">정렬:</span>
      <select v-model="sortBy" @change="handleSort" class="sort-select">
        <option value="latest">최신순</option>
        <option value="hot">인기순</option>
        <option value="viewcount">조회수순</option>
        <option value="likecount">좋아요순</option>
        <option value="commentcount">댓글수순</option>
      </select>
    </div>

    <!-- 에러 메시지 -->
    <div v-if="error" class="error-message">{{ error }}</div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="loading">로딩 중...</div>

    <!-- 게시글 없음 -->
    <div v-else-if="boards.length === 0" class="no-data">
      게시물이 없습니다
    </div>

    <!-- 게시글 테이블 -->
    <table v-else class="board-table">
      <thead>
        <tr>
          <th class="col-number">번호</th>
          <th class="col-title">제목</th>
          <th class="col-writer">글쓴이</th>
          <th class="col-date">작성일</th>
          <th class="col-view">조회</th>
          <th class="col-like">좋아요</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="(board, index) in boards"
          :key="board.boardId"
          @click="goToDetail(board.boardId)"
          class="board-row"
        >
          <td class="col-number">{{ getDisplayNumber(index) }}</td>
          <td class="col-title">
            <div class="title-wrapper">
              <span class="title-text">{{ board.boardTitle }}</span>
              <span v-if="board.boardCommentCount > 0" class="comment-count">
                [{{ board.boardCommentCount }}]
              </span>
              <span v-if="isNewPost(board.createTime || board.updateTime)" class="new-badge">
                N
              </span>
            </div>
          </td>
          <td class="col-writer">{{ board.memberName }}</td>
          <td class="col-date">{{ formatDate(board.updateTime) }}</td>
          <td class="col-view">{{ board.boardViewCount || 0 }}</td>
          <td class="col-like">
            <span v-if="board.boardLikeCount > 0" class="like-count">❤️ {{ board.boardLikeCount }}</span>
            <span v-else class="like-empty">-</span>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div v-if="totalPages > 1" class="pagination">
      <button
        @click="goToPage(0)"
        :disabled="currentPage === 0"
        class="page-btn"
      >
        처음
      </button>
      <button
        @click="goToPage(currentPage - 1)"
        :disabled="currentPage === 0"
        class="page-btn"
      >
        이전
      </button>

      <template v-for="page in visiblePages" :key="page">
        <button
          @click="goToPage(page - 1)"
          :class="{ active: currentPage === page - 1 }"
          class="page-num"
        >
          {{ page }}
        </button>
      </template>

      <button
        @click="goToPage(currentPage + 1)"
        :disabled="currentPage === totalPages - 1"
        class="page-btn"
      >
        다음
      </button>
      <button
        @click="goToPage(totalPages - 1)"
        :disabled="currentPage === totalPages - 1"
        class="page-btn"
      >
        끝
      </button>
    </div>
  </div>
</template>

<style scoped>
/* 전체 컨테이너 */
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border: 1px solid #d5d5d5;
}

/* 상단 헤더 */
.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 2px solid #3d414d;
  background: white;
}

.board-info {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.board-title {
  font-size: 20px;
  font-weight: bold;
  color: #000;
  margin: 0;
}

.board-description {
  font-size: 13px;
  color: #777;
}

.board-actions {
  display: flex;
  gap: 8px;
}

.write-btn {
  padding: 8px 16px;
  background: #3d414d;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: background 0.2s;
}

.write-btn:hover {
  background: #2c2f38;
}

.write-icon {
  font-size: 14px;
}

/* 검색 바 */
.search-bar {
  display: flex;
  gap: 6px;
  padding: 12px 20px;
  background: #f7f9fa;
  border-bottom: 1px solid #d5d5d5;
}

/* 정렬 바 */
.sort-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 20px;
  background: white;
  border-bottom: 1px solid #d5d5d5;
}

.sort-label {
  font-size: 13px;
  font-weight: 500;
  color: #333;
}

.sort-select {
  padding: 6px 10px;
  border: 1px solid #d5d5d5;
  border-radius: 3px;
  font-size: 13px;
  background: white;
  color: #333;
  cursor: pointer;
  transition: all 0.2s;
}

.sort-select:hover {
  border-color: #3d414d;
  background: #f7f9fa;
}

.sort-select:focus {
  outline: none;
  box-shadow: 0 0 3px #3d414d;
}

.search-select {
  padding: 8px 12px;
  border: 1px solid #3d414d;
  border-radius: 3px;
  font-size: 13px;
  background: white;
  color: #333;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.search-select:hover {
  background: #f7f9fa;
  border-color: #2c2f38;
}

.search-select:focus {
  outline: none;
  box-shadow: 0 0 3px #3d414d;
}

.search-input {
  flex: 1;
  padding: 6px 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-size: 13px;
}

.search-btn {
  padding: 6px 16px;
  background: #3d414d;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
  transition: background 0.2s;
}

.search-btn:hover {
  background: #2c2f38;
}

/* 에러 및 로딩 */
.error-message {
  padding: 16px 20px;
  background: #fff3cd;
  color: #856404;
  border-bottom: 1px solid #d5d5d5;
  font-size: 13px;
}

.loading,
.no-data {
  padding: 60px 20px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

/* 게시글 테이블 */
.board-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.board-table thead {
  background: #f7f9fa;
  border-bottom: 1px solid #d5d5d5;
}

.board-table th {
  padding: 10px 8px;
  font-weight: 600;
  color: #333;
  text-align: center;
  border-bottom: 1px solid #d5d5d5;
}

.board-table tbody tr {
  border-bottom: 1px solid #e9ecef;
  cursor: pointer;
  transition: background 0.1s;
}

.board-table tbody tr:hover {
  background: #f8f9fa;
}

.board-table td {
  padding: 10px 8px;
  color: #000;
  vertical-align: middle;
}

/* 컬럼 너비 */
.col-number {
  width: 60px;
  text-align: center;
}

.col-title {
  text-align: left;
  padding-left: 16px !important;
}

.col-writer {
  width: 120px;
  text-align: center;
}

.col-date {
  width: 100px;
  text-align: center;
  color: #777;
  font-size: 12px;
}

.col-view {
  width: 60px;
  text-align: center;
  color: #777;
}

.col-like {
  width: 70px;
  text-align: center;
  font-size: 13px;
}

.like-count {
  color: #ff6b6b;
  font-weight: 600;
  white-space: nowrap;
}

.like-empty {
  color: #ccc;
  font-weight: 300;
}

/* 제목 스타일 */
.title-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
}

.title-text {
  color: #000;
  font-weight: normal;
}

.board-row:hover .title-text {
  color: #3d414d;
  text-decoration: underline;
}

/* 댓글 수 */
.comment-count {
  color: #e74c3c;
  font-weight: 600;
  font-size: 12px;
  flex-shrink: 0;
}

/* 새 글 뱃지 */
.new-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  background: #3498db;
  color: white;
  font-size: 10px;
  font-weight: bold;
  border-radius: 2px;
  flex-shrink: 0;
}

/* 페이지네이션 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
  padding: 20px;
  background: white;
}

.page-btn,
.page-num {
  min-width: 32px;
  height: 32px;
  padding: 0 8px;
  border: 1px solid #d5d5d5;
  background: white;
  color: #333;
  font-size: 13px;
  cursor: pointer;
  border-radius: 3px;
  transition: all 0.2s;
}

.page-btn:hover:not(:disabled),
.page-num:hover {
  background: #f7f9fa;
  border-color: #3d414d;
}

.page-btn:disabled {
  color: #ccc;
  cursor: not-allowed;
  background: #f9f9f9;
}

.page-num.active {
  background: #3d414d;
  color: white;
  border-color: #3d414d;
  font-weight: 600;
}

/* 반응형 */
@media (max-width: 768px) {
  .board-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .search-bar {
    flex-direction: column;
  }

  .sort-bar {
    flex-direction: row;
  }

  .col-view,
  .col-date,
  .col-like {
    display: none;
  }

  .col-writer {
    width: 80px;
    font-size: 12px;
  }

  .board-table {
    font-size: 12px;
  }
}
</style>
