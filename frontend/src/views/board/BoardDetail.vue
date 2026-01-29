<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { boardApi } from '../../api/board'
import { commentApi } from '../../api/comment'
import ReportModal from '../../components/ReportModal.vue'

const router = useRouter()
const route = useRoute()

const board = ref(null)
const comments = ref([])
const isLoading = ref(false)
const error = ref('')

const commentContent = ref('')
const isSubmittingComment = ref(false)

const replyingToCommentId = ref(null)
const replyContent = ref('')
const isSubmittingReply = ref(false)

const isLoggedIn = ref(!!localStorage.getItem('accessToken'))
const currentMemberId = ref(localStorage.getItem('memberId') || '')

const boardId = computed(() => route.params.boardId)

// 신고 모달
const isReportModalOpen = ref(false)

// 좋아요 관련
const liked = ref(false)
const likeCount = ref(0)
const isTogglingLike = ref(false)
let likeDebounceTimeout = null

const loadBoard = async () => {
  isLoading.value = true
  error.value = ''

  try {
    const response = await boardApi.getDetail(boardId.value)
    board.value = response.data

    // 게시글에서 받은 boardLikeCount 사용
    likeCount.value = board.value.boardLikeCount || 0

    // 로그인한 사용자인 경우 좋아요 상태만 조회
    if (isLoggedIn.value) {
      try {
        const likeResponse = await boardApi.getLikeStatus(boardId.value)
        liked.value = likeResponse.data.liked
      } catch (err) {
        // 좋아요 상태 조회 실패 시 기본값으로 설정
        console.error('좋아요 상태 조회 실패:', err)
        liked.value = false
      }
    } else {
      liked.value = false
    }
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
  }
}

const loadComments = async () => {
  try {
    const response = await commentApi.getList(boardId.value)
    const allComments = response.data || []

    console.log('📌 전체 댓글 데이터:', allComments)

    // 댓글을 parent 댓글과 child 댓글로 정렬 (최신순)
    comments.value = allComments
      .map(comment => {
        const children = allComments
          .filter(c => c.commentParentId === comment.commentId)
          .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
        return {
          ...comment,
          children: children
        }
      })
      .filter(comment => !comment.commentParentId)
      .sort((a, b) => new Date(b.createTime) - new Date(a.createTime))

    console.log('📌 정렬된 댓글:', comments.value)
    console.log('📌 총 원댓글 수:', comments.value.length)
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
    await commentApi.create(boardId.value, commentContent.value, currentMemberId.value)
    commentContent.value = ''
    loadComments()
  } catch (err) {
    alert(err.response?.data?.message || '댓글 작성에 실패했습니다')
  } finally {
    isSubmittingComment.value = false
  }
}

const handleReplyClick = (commentId) => {
  if (!isLoggedIn.value) {
    alert('답글을 작성하려면 로그인해야 합니다')
    router.push('/login')
    return
  }
  replyingToCommentId.value = commentId
}

const handleCancelReply = () => {
  replyingToCommentId.value = null
  replyContent.value = ''
}

const handleAddReply = async (parentCommentId) => {
  if (!replyContent.value.trim()) {
    alert('답글을 입력해주세요')
    return
  }

  isSubmittingReply.value = true

  try {
    await commentApi.create(boardId.value, replyContent.value, currentMemberId.value, parentCommentId)
    replyContent.value = ''
    replyingToCommentId.value = null
    loadComments()
  } catch (err) {
    alert(err.response?.data?.message || '답글 작성에 실패했습니다')
  } finally {
    isSubmittingReply.value = false
  }
}

const handleDeleteComment = async (commentId) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) return

  try {
    await commentApi.delete(boardId.value, commentId)
    loadComments()
  } catch (err) {
    alert(err.response?.data?.message || '댓글 삭제에 실패했습니다')
  }
}

const handleDeleteBoard = async () => {
  if (!confirm('게시물을 삭제하시겠습니까?')) return

  try {
    await boardApi.delete(boardId.value)
    alert('게시물이 삭제되었습니다')
    router.push('/boards')
  } catch (err) {
    alert(err.response?.data?.message || '게시물 삭제에 실패했습니다')
  }
}

const handleEditBoard = () => {
  router.push(`/boards/${boardId.value}/edit`)
}

// 좋아요 토글 (낙관적 업데이트 + 디바운싱)
const handleLikeClick = () => {
  if (!isLoggedIn.value) {
    alert('좋아요를 누르려면 로그인해야 합니다')
    router.push('/login')
    return
  }

  // 낙관적 업데이트: 즉시 UI 업데이트
  const wasLiked = liked.value
  const previousCount = likeCount.value

  liked.value = !liked.value
  likeCount.value = liked.value ? previousCount + 1 : previousCount - 1

  // 기존 요청 취소
  if (likeDebounceTimeout) {
    clearTimeout(likeDebounceTimeout)
  }

  // 디바운싱: 500ms 후 서버에 요청
  likeDebounceTimeout = setTimeout(() => {
    toggleLikeDebounced(wasLiked, previousCount)
  }, 500)
}

const toggleLikeDebounced = async (wasLiked, previousCount) => {
  isTogglingLike.value = true

  try {
    const response = await boardApi.toggleLike(boardId.value)
    // 서버의 최신 정보로 동기화
    liked.value = response.data.liked
    likeCount.value = response.data.likeCount
  } catch (err) {
    // 에러 발생 시 롤백
    liked.value = wasLiked
    likeCount.value = previousCount
    console.error('좋아요 토글 실패:', err)
  } finally {
    isTogglingLike.value = false
  }
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  return `${month}.${day} ${hours}:${minutes}:${seconds}`
}

const handleReportClick = () => {
  if (!isLoggedIn.value) {
    alert('신고하려면 로그인해야 합니다')
    router.push('/login')
    return
  }
  isReportModalOpen.value = true
}

const handleReportSuccess = () => {
  // 신고가 성공적으로 접수됨
  isReportModalOpen.value = false
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
        <div class="header-top">
          <h2>{{ board.boardTitle }}</h2>
        </div>
        <div class="header-bottom">
          <div class="board-meta">
            <span>작성자: {{ board.memberName }}</span>
            <span>조회: {{ board.boardViewCount || 0 }}</span>
            <button
              @click="handleLikeClick"
              class="btn-like"
              :class="{ liked }"
              :disabled="isTogglingLike"
            >
              {{ liked ? '❤️' : '🤍' }} {{ likeCount }}
            </button>
            <span>{{ formatDate(board.updateTime) }}</span>
          </div>
          <div class="board-actions">
            <button @click="router.push('/boards')" class="btn btn-secondary">목록</button>
            <button @click="handleReportClick" class="btn btn-report">신고</button>
            <div v-if="currentMemberId && currentMemberId == board.memberId" class="action-group">
              <button @click="handleEditBoard" class="btn btn-primary">수정</button>
              <button @click="handleDeleteBoard" class="btn btn-danger">삭제</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 게시물 내용 -->
      <div class="board-content">
        {{ board.boardContent }}
      </div>

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
            class="btn btn-comment-submit"
            :disabled="isSubmittingComment"
          >
            {{ isSubmittingComment ? '작성 중' : '작성' }}
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
          <!-- 원댓글 -->
          <template v-for="comment in comments" :key="comment.commentId">
            <div class="comment-item">
              <div class="comment-header">
                <span class="comment-author">{{ comment.memberName }}</span>
                <span class="comment-date">{{ formatDate(comment.createTime) }}</span>
              </div>
              <div class="comment-content">{{ comment.commentContent }}</div>
              <div class="comment-actions">
                <button
                  @click="handleReplyClick(comment.commentId)"
                  class="btn-small btn-reply"
                >
                  답글
                </button>
                <button
                  v-if="currentMemberId && currentMemberId == comment.memberId"
                  @click="handleDeleteComment(comment.commentId)"
                  class="btn-small btn-danger"
                >
                  삭제
                </button>
              </div>
            </div>

            <!-- 대댓글 입력 폼 -->
            <div v-if="replyingToCommentId === comment.commentId" class="reply-form">
              <div class="reply-form-header">
                <span class="reply-form-title">답글 작성</span>
                <button @click="handleCancelReply" class="btn-close">✕</button>
              </div>
              <textarea
                v-model="replyContent"
                placeholder="답글을 입력하세요"
                class="reply-input"
                rows="2"
              ></textarea>
              <button
                @click="handleAddReply(comment.commentId)"
                class="btn btn-comment-submit"
                :disabled="isSubmittingReply"
              >
                {{ isSubmittingReply ? '작성 중' : '작성' }}
              </button>
            </div>

            <!-- 대댓글 -->
            <div v-if="comment.children && comment.children.length > 0" class="replies-container">
              <div v-for="reply in comment.children" :key="reply.commentId" class="reply-item">
                <div class="reply-indicator">↳</div>
                <div class="reply-content">
                  <div class="comment-header">
                    <span class="comment-author">{{ reply.memberName }}</span>
                    <span class="comment-date">{{ formatDate(reply.createTime) }}</span>
                  </div>
                  <div class="comment-content">{{ reply.commentContent }}</div>
                  <div
                    v-if="currentMemberId && currentMemberId == reply.memberId"
                    class="comment-actions"
                  >
                    <button
                      @click="handleDeleteComment(reply.commentId)"
                      class="btn-small btn-danger"
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 신고 모달 -->
    <ReportModal
      :boardId="Number(boardId)"
      :isOpen="isReportModalOpen"
      @close="() => isReportModalOpen = false"
      @success="handleReportSuccess"
    />
  </div>
</template>

<style scoped>
body { color: #000; background-color: #f5f5f5; }

.board-detail-container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border: 1px solid #d5d5d5;
}

.loading,
.error-message {
  text-align: center;
  padding: 30px 20px;
  font-size: 14px;
}

.error-message {
  background-color: #fff3cd;
  color: #856404;
  border-bottom: 1px solid #d5d5d5;
  margin-bottom: 0;
}

.board-detail {
  display: flex;
  flex-direction: column;
  text-align: left;
}

.board-header {
  padding: 20px;
  border-bottom: 2px solid #3d414d;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.header-top {
  display: flex;
  align-items: center;
}

.board-header h2 {
  color: #000;
  margin: 0;
  font-size: 22px;
  font-weight: bold;
  line-height: 1.4;
  text-align: left;
}

.header-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.board-meta {
  display: flex;
  gap: 16px;
  color: #777;
  font-size: 12px;
  padding-top: 8px;
  border-top: 1px solid #e9ecef;
  flex: 1;
  text-align: left;
}

.board-meta span {
  display: flex;
  align-items: center;
}

.btn-like {
  background: none;
  border: 1px solid #ddd;
  padding: 4px 8px;
  border-radius: 12px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  color: #666;
  transition: all 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
}

.btn-like:hover:not(:disabled) {
  background: #fff0f0;
  border-color: #ff6b6b;
  color: #ff6b6b;
}

.btn-like.liked {
  background: #ffe0e0;
  border-color: #ff6b6b;
  color: #ff6b6b;
}

.btn-like:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.board-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
  align-items: center;
}

.action-group {
  display: flex;
  gap: 8px;
}

.board-content {
  padding: 20px;
  line-height: 1.8;
  white-space: pre-wrap;
  color: #333;
  font-size: 14px;
  border-bottom: 1px solid #d5d5d5;
  min-height: 200px;
  text-align: left;
}

.btn {
  padding: 6px 14px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: background 0.2s;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  text-align: center;
}

.btn-primary {
  background: #3d414d;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #2c2f38;
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  background: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background: #7f8c8d;
}

.btn-danger {
  background: #e74c3c;
  color: white;
}

.btn-danger:hover {
  background: #c0392b;
}

.comments-section {
  background: #f7f9fa;
  padding: 16px 20px;
  text-align: left;
}

.comments-section h3 {
  color: #000;
  margin: 0 0 12px 0;
  font-size: 15px;
  font-weight: 600;
  text-align: left;
}

.comment-form {
  margin-bottom: 14px;
  background: white;
  padding: 10px 12px;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  text-align: left;
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-end;
}

.comment-input {
  flex: 1;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-family: inherit;
  font-size: 13px;
  resize: none;
  line-height: 1.4;
  box-sizing: border-box;
  min-height: 60px;
}

.comment-input:focus {
  outline: none;
  border-color: #3d414d;
}

.login-prompt {
  background: #f7f9fa;
  padding: 12px 16px;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  margin-bottom: 16px;
  text-align: center;
  font-size: 13px;
  color: #666;
  word-wrap: break-word;
}

.login-prompt a {
  color: #3498db;
  text-decoration: none;
  font-weight: 600;
}

.login-prompt a:hover {
  text-decoration: underline;
}

.no-comments {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 13px;
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 3px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.replies-container {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-left: 24px;
  margin-top: -2px;
  margin-bottom: 6px;
  padding-left: 0;
}

.reply-item {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}

.comment-item {
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  padding: 10px 12px;
  font-size: 13px;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.reply-indicator {
  color: #999;
  font-size: 12px;
  flex-shrink: 0;
  font-weight: bold;
  margin-top: 2px;
}

.reply-content {
  flex: 1;
  background: white;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  padding: 10px 12px;
  font-size: 13px;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
  gap: 8px;
}

.comment-author {
  color: #3d414d;
  font-weight: 600;
  font-size: 12px;
  min-width: 0;
  word-break: break-all;
}

.comment-date {
  color: #999;
  font-size: 11px;
  white-space: nowrap;
  flex-shrink: 0;
}

.comment-content {
  padding: 4px 0;
  line-height: 1.6;
  white-space: pre-wrap;
  color: #333;
  font-size: 13px;
  word-break: break-word;
  text-align: left;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.comment-actions {
  margin-top: 6px;
  padding-top: 6px;
  border-top: 1px solid #e9ecef;
  text-align: left;
  display: flex;
  gap: 6px;
  align-items: center;
}

.btn-small {
  padding: 3px 8px;
  font-size: 11px;
  border: none;
  border-radius: 2px;
  cursor: pointer;
  transition: background 0.2s;
  background: #e9ecef;
  color: #666;
  font-weight: 500;
  display: inline-block;
}

.btn-small:hover {
  background: #ddd;
}

.btn-small.btn-danger {
  background: #e74c3c;
  color: white;
}

.btn-small.btn-danger:hover {
  background: #c0392b;
}

.btn-small.btn-reply {
  background: #3498db;
  color: white;
}

.btn-small.btn-reply:hover {
  background: #2980b9;
}

.btn-report {
  background: #e67e22;
  color: white;
}

.btn-report:hover {
  background: #d35400;
}

.btn-close {
  background: none;
  border: none;
  color: #999;
  font-size: 16px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

.btn-close:hover {
  color: #333;
}

.reply-form {
  margin-left: 24px;
  margin-top: 8px;
  margin-bottom: 8px;
  background: #f9f9f9;
  border: 1px solid #e9ecef;
  border-radius: 3px;
  padding: 10px 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.reply-form-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #666;
  font-weight: 500;
}

.reply-form-title {
  color: #3d414d;
}

.reply-input {
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-family: inherit;
  font-size: 13px;
  resize: none;
  line-height: 1.4;
  box-sizing: border-box;
  min-height: 45px;
}

.reply-input:focus {
  outline: none;
  border-color: #3d414d;
}

.btn-comment-submit {
  padding: 6px 12px !important;
  font-size: 13px !important;
  font-weight: 500;
  background: #3d414d;
  color: white;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  transition: background 0.2s;
  white-space: nowrap;
  flex-shrink: 0;
}

.btn-comment-submit:hover:not(:disabled) {
  background: #2c2f38;
}

.btn-comment-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 반응형 */
@media (max-width: 768px) {
  .board-header {
    padding: 16px;
  }

  .header-bottom {
    flex-direction: column;
    align-items: flex-start !important;
  }

  .board-meta {
    gap: 8px;
    width: 100%;
  }

  .board-actions {
    width: 100%;
    flex-wrap: wrap;
  }

  .board-content {
    padding: 16px;
  }

  .comments-section {
    padding: 16px;
  }

  .btn {
    padding: 6px 12px;
    font-size: 12px;
  }

  .comment-form {
    flex-direction: column;
    align-items: stretch !important;
  }

  .btn-comment-submit {
    align-self: flex-start;
  }

  .replies-container {
    margin-left: 16px;
  }

  .reply-item {
    flex-direction: column;
  }

  .reply-indicator {
    margin-top: 0;
    margin-bottom: 4px;
  }

  .reply-form {
    margin-left: 16px;
  }

  .comment-actions {
    flex-direction: column;
    align-items: flex-start !important;
    gap: 4px;
  }
}
</style>
