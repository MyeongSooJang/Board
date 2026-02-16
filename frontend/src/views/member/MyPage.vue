<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { memberApi } from '../../api/member'
import { boardApi } from '../../api/board'
import { bookmarkApi } from '../../api/bookmark'

const router = useRouter()

const username = ref(localStorage.getItem('username'))
const memberName = ref(localStorage.getItem('memberName') || '')

const userData = ref({
  name: '',
  username: '',
  age: '',
  phone: '',
  email: ''
})

const editMode = ref(false)
const editFormData = ref({
  name: '',
  password: '',
  passwordConfirm: '',
  age: '',
  phone: ''
})

const myBoards = ref([])
const myBookmarks = ref([])
const bookmarkPage = ref(0)
const bookmarkPageSize = ref(10)
const bookmarkTotalPages = ref(0)
const errors = ref({})
const isLoading = ref(false)
const isDeleteConfirmOpen = ref(false)
const successMessage = ref('')
const activeTab = ref('info')

onMounted(async () => {
  await loadUserInfo()
  await loadMyBoards()
})

const loadUserInfo = async () => {
  try {
    const response = await memberApi.getByUsername(username.value)
    const member = response.data

    userData.value = {
      name: member.name,
      username: member.username,
      age: member.age,
      phone: member.phone,
      email: member.email
    }

    // 수정 폼도 초기화
    editFormData.value = {
      name: member.name,
      password: '',
      passwordConfirm: '',
      age: member.age,
      phone: member.phone
    }
  } catch (err) {
    console.error('회원 정보 조회 실패:', err)
    alert('회원 정보를 불러올 수 없습니다.')
    router.push('/boards')
  }
}

const loadMyBoards = async () => {
  try {
    // 모든 게시글을 조회한 후 현재 사용자의 게시글만 필터링
    const response = await boardApi.getList(0, 1000)
    myBoards.value = response.data.content.filter(board => board.memberName == memberName.value)
  } catch (err) {
    console.error('내 게시글 조회 실패:', err)
  }
}

const loadMyBookmarks = async () => {
  try {
    const response = await bookmarkApi.getMyBookmarks(bookmarkPage.value, bookmarkPageSize.value)
    myBookmarks.value = response.data.content || []
    bookmarkTotalPages.value = response.data.totalPages || 0
  } catch (err) {
    console.error('북마크 조회 실패:', err)
  }
}

const handleDeleteBookmark = async (boardId) => {
  if (!confirm('북마크를 삭제하시겠습니까?')) return

  try {
    await bookmarkApi.deleteBookmark(boardId)
    await loadMyBookmarks()
  } catch (err) {
    alert(err.response?.data?.message || '북마크 삭제에 실패했습니다')
  }
}

const goToBookmarkedBoard = (boardId) => {
  router.push(`/boards/${boardId}`)
}

const goToNextBookmarkPage = () => {
  if (bookmarkPage.value < bookmarkTotalPages.value - 1) {
    bookmarkPage.value++
    loadMyBookmarks()
  }
}

const goToPreviousBookmarkPage = () => {
  if (bookmarkPage.value > 0) {
    bookmarkPage.value--
    loadMyBookmarks()
  }
}

const toggleEditMode = () => {
  editMode.value = !editMode.value
  if (editMode.value) {
    errors.value = {}
    successMessage.value = ''
  }
}

const validateForm = () => {
  errors.value = {}

  if (!editFormData.value.name || editFormData.value.name.trim().length < 2) {
    errors.value.name = '이름은 2자 이상이어야 합니다'
  }

  if (editFormData.value.password) {
    if (editFormData.value.password.length < 8) {
      errors.value.password = '비밀번호는 8자 이상이어야 합니다'
    }
    if (editFormData.value.password !== editFormData.value.passwordConfirm) {
      errors.value.passwordConfirm = '비밀번호가 일치하지 않습니다'
    }
  }

  if (editFormData.value.age && (editFormData.value.age < 18 || editFormData.value.age > 150)) {
    errors.value.age = '나이는 18~150세여야 합니다'
  }

  if (editFormData.value.phone && !/^01[0-9]-\d{3,4}-\d{4}$/.test(editFormData.value.phone)) {
    errors.value.phone = '휴대폰 형식: 010-1234-5678'
  }

  return Object.keys(errors.value).length === 0
}

const handleSaveEdit = async () => {
  if (!validateForm()) {
    return
  }

  isLoading.value = true
  successMessage.value = ''

  try {
    const updateData = {}
    if (editFormData.value.name) updateData.name = editFormData.value.name
    if (editFormData.value.password) updateData.password = editFormData.value.password
    if (editFormData.value.age) updateData.age = editFormData.value.age
    if (editFormData.value.phone) updateData.phone = editFormData.value.phone

    const response = await memberApi.updateByUsername(username.value, updateData)

    // 로컬스토리지 업데이트
    localStorage.setItem('memberName', response.data.name)
    memberName.value = response.data.name

    userData.value = {
      name: response.data.name,
      username: response.data.username,
      age: response.data.age,
      phone: response.data.phone,
      email: response.data.email
    }

    successMessage.value = '정보가 수정되었습니다.'
    editFormData.value.password = ''
    editFormData.value.passwordConfirm = ''

    setTimeout(() => {
      editMode.value = false
    }, 1500)
  } catch (err) {
    const errorMsg = err.response?.data?.message || '정보 수정에 실패했습니다'
    alert(errorMsg)
  } finally {
    isLoading.value = false
  }
}

const handleDeleteAccountClick = () => {
  isDeleteConfirmOpen.value = true
}

const handleDeleteAccount = async () => {
  try {
    await memberApi.deleteByUsername(username.value)
    alert('계정이 삭제되었습니다.')

    // 로그아웃 처리
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('username')
    localStorage.removeItem('memberName')
    localStorage.removeItem('role')

    router.push('/login')
  } catch (err) {
    const errorMsg = err.response?.data?.message || '계정 삭제에 실패했습니다'
    alert(errorMsg)
  } finally {
    isDeleteConfirmOpen.value = false
  }
}

const handleCancelDelete = () => {
  isDeleteConfirmOpen.value = false
}

const goToBoard = (boardId) => {
  router.push(`/boards/${boardId}`)
}

const goBack = () => {
  router.push('/boards')
}

const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}.${month}.${day}`
}
</script>

<template>
  <div class="mypage-container">
    <div class="mypage-header">
      <h1>마이페이지</h1>
      <button @click="goBack" class="back-btn">← 돌아가기</button>
    </div>

    <div class="mypage-wrapper">
      <!-- 왼쪽 사이드바 메뉴 -->
      <aside class="sidebar-menu">
        <h3>메뉴</h3>
        <nav class="menu-list">
          <button
            class="menu-item"
            :class="{ active: activeTab === 'info' }"
            @click="activeTab = 'info'"
          >
            계정 정보
          </button>
          <button
            class="menu-item"
            :class="{ active: activeTab === 'boards' }"
            @click="activeTab = 'boards'"
          >
            내 게시글
          </button>
          <button
            class="menu-item"
            :class="{ active: activeTab === 'bookmarks' }"
            @click="() => { activeTab = 'bookmarks'; loadMyBookmarks() }"
          >
            📌 북마크
          </button>
          <button
            class="menu-item"
            :class="{ active: activeTab === 'delete' }"
            @click="activeTab = 'delete'"
          >
            계정 삭제
          </button>
        </nav>
      </aside>

      <div class="mypage-content">
      <!-- 사용자 정보 섹션 -->
      <div v-if="activeTab === 'info'" class="user-info-section">
        <div class="section-header">
          <h2>계정 정보</h2>
          <button v-if="!editMode" @click="toggleEditMode" class="edit-toggle-btn">
            수정
          </button>
          <button v-else @click="toggleEditMode" class="edit-toggle-btn cancel">
            취소
          </button>
        </div>

        <!-- 정보 표시 모드 -->
        <div v-if="!editMode" class="user-info-display">
          <div class="info-item">
            <label>사용자명</label>
            <span>{{ userData.username }}</span>
          </div>
          <div class="info-item">
            <label>이름</label>
            <span>{{ userData.name }}</span>
          </div>
          <div class="info-item">
            <label>이메일</label>
            <span>{{ userData.email }}</span>
          </div>
          <div class="info-item">
            <label>나이</label>
            <span>{{ userData.age || '-' }}</span>
          </div>
          <div class="info-item">
            <label>휴대폰</label>
            <span>{{ userData.phone || '-' }}</span>
          </div>
        </div>

        <!-- 정보 수정 모드 -->
        <div v-else class="user-info-edit">
          <div class="form-group">
            <label for="edit-name">이름</label>
            <input
              id="edit-name"
              v-model="editFormData.name"
              type="text"
              class="input-field"
              placeholder="이름을 입력하세요"
            />
            <span v-if="errors.name" class="error-text">{{ errors.name }}</span>
          </div>

          <div class="form-group">
            <label for="edit-password">비밀번호 (변경하지 않으면 비워두세요)</label>
            <input
              id="edit-password"
              v-model="editFormData.password"
              type="password"
              class="input-field"
              placeholder="새 비밀번호"
            />
            <span v-if="errors.password" class="error-text">{{ errors.password }}</span>
          </div>

          <div class="form-group">
            <label for="edit-passwordConfirm">비밀번호 확인</label>
            <input
              id="edit-passwordConfirm"
              v-model="editFormData.passwordConfirm"
              type="password"
              class="input-field"
              placeholder="비밀번호 확인"
            />
            <span v-if="errors.passwordConfirm" class="error-text">{{ errors.passwordConfirm }}</span>
          </div>

          <div class="form-group">
            <label for="edit-age">나이</label>
            <input
              id="edit-age"
              v-model.number="editFormData.age"
              type="number"
              class="input-field"
              placeholder="나이를 입력하세요"
              min="18"
              max="150"
            />
            <span v-if="errors.age" class="error-text">{{ errors.age }}</span>
          </div>

          <div class="form-group">
            <label for="edit-phone">휴대폰</label>
            <input
              id="edit-phone"
              v-model="editFormData.phone"
              type="text"
              class="input-field"
              placeholder="010-1234-5678"
            />
            <span v-if="errors.phone" class="error-text">{{ errors.phone }}</span>
          </div>

          <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

          <button @click="handleSaveEdit" class="save-btn" :disabled="isLoading">
            {{ isLoading ? '저장 중...' : '저장' }}
          </button>
        </div>
      </div>

      <!-- 내 게시글 섹션 -->
      <div v-if="activeTab === 'boards'" class="my-boards-section">
        <div class="section-header">
          <h2>내가 작성한 게시글 ({{ myBoards.length }})</h2>
        </div>

        <div v-if="myBoards.length === 0" class="no-boards">
          작성한 게시글이 없습니다.
        </div>

        <table v-else class="board-table">
          <thead>
            <tr>
              <th class="col-title">제목</th>
              <th class="col-date">작성일</th>
              <th class="col-view">조회</th>
              <th class="col-like">좋아요</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="board in myBoards"
              :key="board.boardId"
              @click="goToBoard(board.boardId)"
              class="board-row"
            >
              <td class="col-title">{{ board.boardTitle }}</td>
              <td class="col-date">{{ formatDate(board.createTime) }}</td>
              <td class="col-view">{{ board.boardViewCount || 0 }}</td>
              <td class="col-like">
                <span v-if="board.boardLikeCount > 0">❤️ {{ board.boardLikeCount }}</span>
                <span v-else>-</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 북마크 섹션 -->
      <div v-if="activeTab === 'bookmarks'" class="bookmarks-section">
        <div class="section-header">
          <h2>저장한 북마크</h2>
        </div>

        <div v-if="myBookmarks.length === 0" class="no-bookmarks">
          저장한 북마크가 없습니다.
        </div>

        <table v-else class="bookmark-table">
          <thead>
            <tr>
              <th class="col-title">제목</th>
              <th class="col-author">작성자</th>
              <th class="col-date">작성일</th>
              <th class="col-view">조회</th>
              <th class="col-like">좋아요</th>
              <th class="col-saved">저장일</th>
              <th class="col-action">삭제</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="bookmark in myBookmarks"
              :key="bookmark.markId"
              @click="goToBookmarkedBoard(bookmark.boardId)"
              class="bookmark-row"
            >
              <td class="col-title">{{ bookmark.boardTitle }}</td>
              <td class="col-author">{{ bookmark.memberName }}</td>
              <td class="col-date">{{ formatDate(bookmark.createTime) }}</td>
              <td class="col-view">{{ bookmark.viewCount || 0 }}</td>
              <td class="col-like">
                <span v-if="bookmark.likeCount > 0">❤️ {{ bookmark.likeCount }}</span>
                <span v-else>-</span>
              </td>
              <td class="col-saved">{{ formatDate(bookmark.bookmarkedAt) }}</td>
              <td class="col-action">
                <button
                  @click.stop="handleDeleteBookmark(bookmark.boardId)"
                  class="btn-delete-small"
                  title="북마크 삭제"
                >
                  ✕
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- 페이지네이션 -->
        <div v-if="bookmarkTotalPages > 1" class="pagination">
          <button
            @click="goToPreviousBookmarkPage"
            :disabled="bookmarkPage === 0"
            class="btn-paging"
          >
            ← 이전
          </button>
          <span class="page-info">
            {{ bookmarkPage + 1 }} / {{ bookmarkTotalPages }}
          </span>
          <button
            @click="goToNextBookmarkPage"
            :disabled="bookmarkPage === bookmarkTotalPages - 1"
            class="btn-paging"
          >
            다음 →
          </button>
        </div>
      </div>

      <!-- 계정 삭제 섹션 -->
      <div v-if="activeTab === 'delete'" class="danger-section">
        <h3>계정 삭제</h3>
        <p class="danger-description">이 작업은 되돌릴 수 없습니다.</p>
        <button @click="handleDeleteAccountClick" class="delete-account-btn">
          계정 삭제
        </button>
      </div>
      </div>
    </div>

    <!-- 계정 삭제 확인 모달 -->
    <div v-if="isDeleteConfirmOpen" class="modal-overlay">
      <div class="modal-content">
        <h3>계정을 삭제하시겠습니까?</h3>
        <p class="warning-text">
          이 작업은 되돌릴 수 없습니다. 모든 게시글과 댓글이 유지되지만, 계정으로는 더 이상 로그인할 수 없습니다.
        </p>
        <div class="modal-actions">
          <button @click="handleCancelDelete" class="btn-modal-cancel">
            취소
          </button>
          <button @click="handleDeleteAccount" class="btn-modal-confirm">
            삭제
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mypage-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem;
}

.mypage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 2px solid #333;
}

.mypage-header h1 {
  font-size: 2rem;
  color: #333;
  margin: 0;
}

.back-btn {
  padding: 0.5rem 1rem;
  background-color: #95a5a6;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background-color: #7f8c8d;
}

.mypage-wrapper {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 2rem;
}

.sidebar-menu {
  padding: 1.5rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: fit-content;
  position: sticky;
  top: 2rem;
}

.sidebar-menu h3 {
  margin: 0 0 1.5rem 0;
  font-size: 1.1rem;
  color: #333;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e0e0e0;
}

.menu-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.menu-item {
  padding: 0.75rem 1rem;
  background-color: transparent;
  color: #666;
  border: none;
  border-left: 3px solid transparent;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  text-align: left;
  transition: all 0.3s;
}

.menu-item:hover {
  background-color: #f5f5f5;
  color: #333;
}

.menu-item.active {
  color: #3498db;
  background-color: #e3f2fd;
  border-left-color: #3498db;
}

.mypage-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #ddd;
}

.section-header h2 {
  margin: 0;
  font-size: 1.3rem;
  color: #333;
}

.edit-toggle-btn {
  padding: 0.5rem 1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-toggle-btn:hover {
  background-color: #2980b9;
}

.edit-toggle-btn.cancel {
  background-color: #95a5a6;
}

.edit-toggle-btn.cancel:hover {
  background-color: #7f8c8d;
}

.user-info-section {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-info-display {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.info-item label {
  font-weight: 600;
  color: #666;
  font-size: 0.9rem;
}

.info-item span {
  color: #333;
  font-size: 1rem;
  padding: 0.75rem;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.user-info-edit {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
}

.input-field {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.input-field:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.error-text {
  color: #e74c3c;
  font-size: 0.85rem;
}

.success-message {
  padding: 1rem;
  background-color: #d4edda;
  color: #155724;
  border-radius: 4px;
  text-align: center;
  font-weight: 500;
}

.save-btn {
  align-self: flex-start;
  padding: 0.75rem 1.5rem;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.save-btn:hover:not(:disabled) {
  background-color: #36a372;
}

.save-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.my-boards-section {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.no-boards {
  text-align: center;
  padding: 2rem;
  color: #999;
  font-size: 1rem;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.board-table thead {
  background-color: #f5f5f5;
  border-bottom: 2px solid #ddd;
}

.board-table th {
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
}

.board-table td {
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
  color: #333;
}

.board-table tbody tr {
  cursor: pointer;
  transition: all 0.2s;
}

.board-table tbody tr:hover {
  background-color: #f9f9f9;
}

.board-row:hover {
  background-color: #f0f8ff;
}

.col-title {
  width: 60%;
}

.col-date {
  width: 15%;
  font-size: 0.9rem;
  color: #666;
}

.col-view {
  width: 10%;
  text-align: center;
  font-size: 0.9rem;
}

.col-like {
  width: 15%;
  text-align: center;
  font-size: 0.9rem;
}

.bookmarks-section {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.no-bookmarks {
  text-align: center;
  padding: 2rem;
  color: #999;
  font-size: 1rem;
}

.bookmark-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.bookmark-table thead {
  background-color: #f5f5f5;
  border-bottom: 2px solid #ddd;
}

.bookmark-table th {
  padding: 1rem;
  text-align: left;
  font-weight: 600;
  color: #333;
  font-size: 0.95rem;
}

.bookmark-table td {
  padding: 1rem;
  border-bottom: 1px solid #e0e0e0;
  color: #333;
}

.bookmark-table tbody tr {
  cursor: pointer;
  transition: all 0.2s;
}

.bookmark-table tbody tr:hover {
  background-color: #f9f9f9;
}

.bookmark-row:hover {
  background-color: #f0f8ff;
}

.col-title {
  width: 35%;
}

.col-author {
  width: 15%;
  font-size: 0.9rem;
}

.col-date {
  width: 13%;
  font-size: 0.9rem;
  color: #666;
}

.col-view {
  width: 8%;
  text-align: center;
  font-size: 0.9rem;
}

.col-like {
  width: 8%;
  text-align: center;
  font-size: 0.9rem;
}

.col-saved {
  width: 13%;
  font-size: 0.9rem;
  color: #666;
}

.col-action {
  width: 8%;
  text-align: center;
}

.btn-delete-small {
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 3px;
  padding: 0.4rem 0.6rem;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 600;
  transition: background 0.2s;
}

.btn-delete-small:hover {
  background: #c0392b;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  padding: 1rem 0;
  border-top: 1px solid #e0e0e0;
}

.btn-paging {
  padding: 0.5rem 1rem;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-paging:hover:not(:disabled) {
  background-color: #2980b9;
}

.btn-paging:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 0.95rem;
  color: #666;
  font-weight: 500;
  min-width: 60px;
  text-align: center;
}

.danger-section {
  background-color: #fff5f5;
  border: 2px solid #ffcccb;
  padding: 2rem;
  border-radius: 8px;
}

.danger-section h3 {
  color: #c0392b;
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
}

.danger-description {
  color: #e74c3c;
  font-size: 0.95rem;
  margin: 0 0 1.5rem 0;
}

.delete-account-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #c0392b;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-account-btn:hover {
  background-color: #a93226;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 90%;
}

.modal-content h3 {
  margin: 0 0 1rem 0;
  color: #c0392b;
  font-size: 1.2rem;
}

.warning-text {
  color: #555;
  font-size: 0.95rem;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.btn-modal-cancel {
  flex: 1;
  padding: 0.75rem;
  background-color: #95a5a6;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-modal-cancel:hover {
  background-color: #7f8c8d;
}

.btn-modal-confirm {
  flex: 1;
  padding: 0.75rem;
  background-color: #c0392b;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-modal-confirm:hover {
  background-color: #a93226;
}

@media (max-width: 768px) {
  .mypage-container {
    padding: 1rem;
  }

  .mypage-header {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }

  .mypage-header h1 {
    font-size: 1.5rem;
  }

  .mypage-wrapper {
    grid-template-columns: 1fr;
  }

  .sidebar-menu {
    position: static;
    padding: 1.5rem;
    margin-bottom: 1rem;
  }

  .board-table {
    font-size: 0.9rem;
  }

  .board-table th,
  .board-table td {
    padding: 0.75rem;
  }

  .col-title {
    width: 50%;
  }

  .col-date {
    width: 20%;
  }

  .col-view {
    width: 15%;
  }

  .col-like {
    width: 15%;
  }

  .user-info-display {
    grid-template-columns: 1fr;
  }

  .bookmark-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .btn-delete-bookmark {
    align-self: flex-end;
    margin-top: -2rem;
  }

  .bookmark-meta {
    font-size: 0.85rem;
    gap: 0.5rem;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .edit-toggle-btn {
    width: 100%;
  }

  .save-btn {
    width: 100%;
  }
}
</style>
