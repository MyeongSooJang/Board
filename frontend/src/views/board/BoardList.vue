<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { boardApi } from '../../api/board'

const router = useRouter()

const boards = ref([])
const totalPages = ref(1)
const currentPage = ref(0)
const pageSize = ref(10)
const isLoading = ref(false)
const error = ref('')

const searchType = ref('all') // all, title, writer, keyword
const searchQuery = ref('')

const loadBoards = async () => {
  isLoading.value = true
  error.value = ''

  try {
    let response

    if (searchType.value === 'title') {
      response = await boardApi.searchByTitle(searchQuery.value, currentPage.value, pageSize.value)
    } else if (searchType.value === 'writer') {
      response = await boardApi.searchByWriter(searchQuery.value, currentPage.value, pageSize.value)
    } else if (searchType.value === 'keyword') {
      response = await boardApi.searchByKeyword(searchQuery.value, currentPage.value, pageSize.value)
    } else {
      response = await boardApi.getList(currentPage.value, pageSize.value)
    }

    boards.value = response.data.content || response.data
    totalPages.value = response.data.totalPages || 1
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 0
  loadBoards()
}

const goToDetail = (boardNo) => {
  router.push(`/boards/${boardNo}`)
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR')
}

const goToPage = (page) => {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page
    loadBoards()
  }
}

onMounted(() => {
  loadBoards()
})
</script>

<template>
  <div class="board-list-container">
    <h2>게시판</h2>

    <!-- 검색 폼 -->
    <div class="search-section">
      <div class="search-form">
        <select v-model="searchType" class="search-select">
          <option value="all">전체</option>
          <option value="title">제목</option>
          <option value="writer">작성자</option>
          <option value="keyword">제목+내용</option>
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
    </div>

    <!-- 에러 메시지 -->
    <div v-if="error" class="error-message">{{ error }}</div>

    <!-- 게시물 테이블 -->
    <div v-if="isLoading" class="loading">로딩 중...</div>

    <div v-else-if="boards.length === 0" class="no-data">
      게시물이 없습니다
    </div>

    <table v-else class="board-table">
      <thead>
        <tr>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="board in boards" :key="board.boardNo" @click="goToDetail(board.boardNo)" class="board-row">
          <td class="title">{{ board.boardTitle }}</td>
          <td>{{ board.memberName }}</td>
          <td>{{ formatDate(board.updateTime) }}</td>
        </tr>
      </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div v-if="totalPages > 1" class="pagination">
      <button
        @click="goToPage(currentPage - 1)"
        :disabled="currentPage === 0"
        class="page-btn"
      >
        이전
      </button>

      <button
        v-for="page in totalPages"
        :key="page"
        @click="goToPage(page - 1)"
        :class="{ active: currentPage === page - 1 }"
        class="page-num"
      >
        {{ page }}
      </button>

      <button
        @click="goToPage(currentPage + 1)"
        :disabled="currentPage === totalPages - 1"
        class="page-btn"
      >
        다음
      </button>
    </div>
  </div>
</template>

<style scoped>
.board-list-container {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.board-list-container h2 {
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.search-section {
  margin-bottom: 2rem;
}

.search-form {
  display: flex;
  gap: 0.5rem;
}

.search-select,
.search-input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-input {
  flex: 1;
}

.search-btn {
  padding: 0.75rem 1.5rem;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background-color: #36a372;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.loading,
.no-data {
  text-align: center;
  padding: 2rem;
  color: #7f8c8d;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 2rem;
}

.board-table thead {
  background-color: #ecf0f1;
}

.board-table th,
.board-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #ecf0f1;
}

.board-table th {
  font-weight: bold;
  color: #2c3e50;
}

.board-row {
  cursor: pointer;
  transition: background-color 0.3s;
}

.board-row:hover {
  background-color: #f8f9fa;
}

.title {
  color: #42b883;
  font-weight: 500;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
}

.page-btn,
.page-num {
  padding: 0.5rem 0.75rem;
  border: 1px solid #ddd;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.page-btn:hover:not(:disabled),
.page-num:hover {
  background-color: #ecf0f1;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-num.active {
  background-color: #42b883;
  color: white;
  border-color: #42b883;
}
</style>
