<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { bookmarkApi } from '../../api/bookmark'

const router = useRouter()

const bookmarks = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const totalElements = ref(0)
const totalPages = ref(0)
const isLoading = ref(false)
const error = ref('')

onMounted(() => {
  loadBookmarks()
})

const loadBookmarks = async () => {
  isLoading.value = true
  error.value = ''

  try {
    const response = await bookmarkApi.getMyBookmarks(currentPage.value, pageSize.value)
    bookmarks.value = response.data.content || []
    totalElements.value = response.data.totalElements || 0
    totalPages.value = response.data.totalPages || 0
  } catch (err) {
    error.value = err.response?.data?.message || '북마크 목록을 불러오지 못했습니다'
    console.error('북마크 로드 실패:', err)
  } finally {
    isLoading.value = false
  }
}

const handleDeleteBookmark = async (bookmarkItem) => {
  if (!confirm('북마크를 삭제하시겠습니까?')) return

  try {
    await bookmarkApi.deleteBookmark(bookmarkItem.boardId)
    alert('북마크가 삭제되었습니다')
    loadBookmarks()
  } catch (err) {
    alert(err.response?.data?.message || '북마크 삭제에 실패했습니다')
  }
}

const goToBoard = (boardId) => {
  router.push(`/boards/${boardId}`)
}

const goToPreviousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--
    loadBookmarks()
  }
}

const goToNextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
    loadBookmarks()
  }
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  return `${month}.${day} ${hours}:${minutes}`
}
</script>

<template>
  <div class="bookmark-container">
    <div class="bookmark-header">
      <h1>📌 내 북마크</h1>
      <button @click="router.push('/boards')" class="btn btn-secondary">
        목록으로
      </button>
    </div>

    <div v-if="isLoading" class="loading">
      로딩 중...
    </div>

    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>

    <div v-else class="bookmark-content">
      <div v-if="bookmarks.length === 0" class="no-bookmarks">
        <p>저장된 북마크가 없습니다</p>
        <button @click="router.push('/boards')" class="btn btn-primary">
          게시글 보러 가기
        </button>
      </div>

      <div v-else class="bookmarks-list">
        <div
          v-for="bookmark in bookmarks"
          :key="bookmark.markId"
          class="bookmark-item"
          @click="goToBoard(bookmark.boardId)"
        >
          <div class="bookmark-content-wrapper">
            <h3 class="bookmark-title">{{ bookmark.boardTitle }}</h3>
            <div class="bookmark-meta">
              <span class="author">{{ bookmark.memberName }}</span>
              <span class="date">{{ formatDate(bookmark.createTime) }}</span>
              <span class="stats">
                👁 {{ bookmark.viewCount }} | ❤️ {{ bookmark.likeCount }} | 💬 {{ bookmark.commentCount }}
              </span>
            </div>
            <div class="bookmark-time">
              <small>북마크 저장: {{ formatDate(bookmark.bookmarkedAt) }}</small>
            </div>
          </div>
          <button
            @click.stop="handleDeleteBookmark(bookmark)"
            class="btn-delete"
            title="북마크 삭제"
          >
            ✕
          </button>
        </div>
      </div>

      <!-- 페이지네이션 -->
      <div v-if="totalPages > 1" class="pagination">
        <button
          @click="goToPreviousPage"
          :disabled="currentPage === 0"
          class="btn btn-nav"
        >
          ← 이전
        </button>
        <span class="page-info">
          {{ currentPage + 1 }} / {{ totalPages }}
        </span>
        <button
          @click="goToNextPage"
          :disabled="currentPage === totalPages - 1"
          class="btn btn-nav"
        >
          다음 →
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
body { color: #000; background-color: #f5f5f5; }

.bookmark-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: white;
  border: 1px solid #d5d5d5;
}

.bookmark-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #3d414d;
}

.bookmark-header h1 {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  color: #000;
}

.loading,
.error-message {
  text-align: center;
  padding: 40px 20px;
  font-size: 14px;
}

.error-message {
  background-color: #fff3cd;
  color: #856404;
  border-radius: 3px;
  margin-bottom: 20px;
}

.no-bookmarks {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.no-bookmarks p {
  font-size: 16px;
  margin-bottom: 20px;
}

.bookmarks-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 20px;
}

.bookmark-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 14px;
  background: #f9f9f9;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  cursor: pointer;
  transition: all 0.2s;
  gap: 12px;
}

.bookmark-item:hover {
  background: #f0f0f0;
  border-color: #3d414d;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.bookmark-content-wrapper {
  flex: 1;
  min-width: 0;
}

.bookmark-title {
  margin: 0 0 6px 0;
  font-size: 14px;
  font-weight: 600;
  color: #3d414d;
  text-align: left;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.bookmark-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.bookmark-meta span {
  display: inline-block;
}

.author {
  color: #3d414d;
  font-weight: 500;
}

.date,
.stats {
  color: #999;
}

.bookmark-time {
  font-size: 11px;
  color: #bbb;
  text-align: left;
}

.btn-delete {
  flex-shrink: 0;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 3px;
  padding: 4px 8px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 600;
  transition: background 0.2s;
}

.btn-delete:hover {
  background: #c0392b;
}

.btn {
  padding: 6px 14px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: background 0.2s;
}

.btn-primary {
  background: #3d414d;
  color: white;
}

.btn-primary:hover {
  background: #2c2f38;
}

.btn-secondary {
  background: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background: #7f8c8d;
}

.btn-nav {
  background: #3d414d;
  color: white;
}

.btn-nav:hover:not(:disabled) {
  background: #2c2f38;
}

.btn-nav:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  padding: 16px 0;
  border-top: 1px solid #e9ecef;
}

.page-info {
  font-size: 13px;
  color: #666;
  font-weight: 500;
  min-width: 60px;
  text-align: center;
}

@media (max-width: 768px) {
  .bookmark-container {
    padding: 16px;
  }

  .bookmark-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .bookmark-meta {
    flex-wrap: wrap;
    gap: 8px;
  }

  .bookmark-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .btn-delete {
    align-self: flex-end;
    margin-top: -28px;
  }
}
</style>
