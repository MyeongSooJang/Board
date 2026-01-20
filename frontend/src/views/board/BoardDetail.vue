<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { boardApi } from '../../api/board'
import { commentApi } from '../../api/comment'

const router = useRouter()
const route = useRoute()

const board = ref(null)
const comments = ref([])
const isLoading = ref(false)
const error = ref('')

const commentContent = ref('')
const isSubmittingComment = ref(false)

const isLoggedIn = ref(!!localStorage.getItem('accessToken'))
const currentMemberId = ref(localStorage.getItem('memberId') || '')

const boardNo = ref(route.params.boardNo)

const loadBoard = async () => {
  isLoading.value = true
  error.value = ''

  try {
    const response = await boardApi.getDetail(boardNo.value)
    board.value = response.data
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
  }
}

const loadComments = async () => {
  try {
    const response = await commentApi.getList(boardNo.value)
    comments.value = response.data || []
  } catch (err) {
    console.error('댓글 로드 실패:', err)
  }
}

const handleAddComment = async () => {
  if (!isLoggedIn.value) {
    alert('댓글을 작성하려면 로그인해야 합니다')
    router.push('/login')
    return
  }

  if (!commentContent.value.trim()) {
    alert('댓글을 입력해주세요')
    return
  }

  isSubmittingComment.value = true

  try {
    await commentApi.create(boardNo.value, commentContent.value)
    commentContent.value = ''
    loadComments()
  } catch (err) {
    alert(err.response?.data?.message || '댓글 작성에 실패했습니다')
  } finally {
    isSubmittingComment.value = false
  }
}

const handleDeleteComment = async (commentNo) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) return

  try {
    await commentApi.delete(boardNo.value, commentNo)
    loadComments()
  } catch (err) {
    alert(err.response?.data?.message || '댓글 삭제에 실패했습니다')
  }
}

const handleDeleteBoard = async () => {
  if (!confirm('게시물을 삭제하시겠습니까?')) return

  try {
    await boardApi.delete(boardNo.value)
    alert('게시물이 삭제되었습니다')
    router.push('/boards')
  } catch (err) {
    alert(err.response?.data?.message || '게시물 삭제에 실패했습니다')
  }
}

const handleEditBoard = () => {
  router.push(`/boards/${boardNo.value}/edit`)
}

const formatDate = (date) => {
  return new Date(date).toLocaleString('ko-KR')
}

onMounted(() => {
  loadBoard()
  loadComments()
})
</script>

<template>
  <div class="board-detail-container">
    <div v-if="isLoading" class="loading">로딩 중...</div>

    <div v-else-if="error" class="error-message">{{ error }}</div>

    <div v-else-if="board" class="board-detail">
      <!-- 게시물 헤더 -->
      <div class="board-header">
        <h2>{{ board.boardTitle }}</h2>
        <div class="board-meta">
          <span>작성자: {{ board.memberName }}</span>
          <span>{{ formatDate(board.updateTime) }}</span>
        </div>
        <div v-if="currentMemberId === board.memberId" class="board-actions">
          <button @click="handleEditBoard" class="btn btn-primary">수정</button>
          <button @click="handleDeleteBoard" class="btn btn-danger">삭제</button>
        </div>
      </div>

      <!-- 게시물 내용 -->
      <div class="board-content">
        {{ board.boardContent }}
      </div>

      <!-- 뒤로가기 버튼 -->
      <button @click="router.push('/boards')" class="btn btn-secondary">목록</button>

      <!-- 댓글 섹션 -->
      <div class="comments-section">
        <h3>댓글 ({{ comments.length }})</h3>

        <!-- 댓글 작성 폼 -->
        <div v-if="isLoggedIn" class="comment-form">
          <textarea
            v-model="commentContent"
            placeholder="댓글을 입력하세요"
            class="comment-input"
            rows="3"
          ></textarea>
          <button
            @click="handleAddComment"
            class="btn btn-primary"
            :disabled="isSubmittingComment"
          >
            {{ isSubmittingComment ? '작성 중...' : '댓글 작성' }}
          </button>
        </div>

        <div v-else class="login-prompt">
          <p>댓글을 작성하려면 <router-link to="/login">로그인</router-link>해주세요</p>
        </div>

        <!-- 댓글 목록 -->
        <div v-if="comments.length === 0" class="no-comments">
          댓글이 없습니다
        </div>

        <div v-else class="comments-list">
          <div v-for="comment in comments" :key="comment.commentNo" class="comment-item">
            <div class="comment-header">
              <span class="comment-author">{{ comment.memberName }}</span>
              <span class="comment-date">{{ formatDate(comment.createdTime) }}</span>
            </div>
            <div class="comment-content">{{ comment.commentContent }}</div>
            <div
              v-if="currentMemberId === comment.memberId"
              class="comment-actions"
            >
              <button
                @click="handleDeleteComment(comment.commentNo)"
                class="btn-small btn-danger"
              >
                삭제
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
body { color: #000; background-color: #f5f5f5; }

.board-detail-container {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.loading,
.error-message {
  text-align: center;
  padding: 2rem;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  border-radius: 4px;
}

.board-detail {
  display: flex;
  flex-direction: column;
}

.board-header {
  border-bottom: 2px solid #ecf0f1;
  padding-bottom: 1rem;
  margin-bottom: 1.5rem;
}

.board-header h2 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
}

.board-meta {
  display: flex;
  gap: 1rem;
  color: #7f8c8d;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}

.board-actions {
  display: flex;
  gap: 0.5rem;
}

.board-content {
  padding: 2rem 0;
  line-height: 1.6;
  white-space: pre-wrap;
  margin-bottom: 2rem;
}

.btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.btn-primary {
  background-color: #42b883;
  color: white;
}

.btn-primary:hover {
  background-color: #36a372;
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
  margin-bottom: 2rem;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
}

.btn-danger {
  background-color: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background-color: #c0392b;
}

.comments-section {
  margin-top: 2rem;
  border-top: 2px solid #ecf0f1;
  padding-top: 2rem;
}

.comments-section h3 {
  color: #2c3e50;
  margin-bottom: 1rem;
}

.comment-form {
  margin-bottom: 2rem;
}

.comment-input {
  width: 100%;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: inherit;
  font-size: 1rem;
  resize: vertical;
}

.login-prompt {
  background-color: #ecf0f1;
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  text-align: center;
}

.login-prompt a {
  color: #42b883;
  text-decoration: none;
  font-weight: bold;
}

.no-comments {
  text-align: center;
  padding: 1rem;
  color: #7f8c8d;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-item {
  border: 1px solid #ecf0f1;
  padding: 1rem;
  border-radius: 4px;
  background-color: #f8f9fa;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.comment-author {
  color: #42b883;
  font-weight: bold;
}

.comment-date {
  color: #7f8c8d;
}

.comment-content {
  padding: 0.5rem 0;
  line-height: 1.5;
  white-space: pre-wrap;
}

.comment-actions {
  margin-top: 0.5rem;
}

.btn-small {
  padding: 0.25rem 0.75rem;
  font-size: 0.85rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-small.btn-danger {
  background-color: #e74c3c;
  color: white;
}

.btn-small.btn-danger:hover {
  background-color: #c0392b;
}
</style>
