<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { reportApi } from '../../api/report'
import { boardApi } from '../../api/board'
import ReportDetailModal from '../../components/ReportDetailModal.vue'

const router = useRouter()

// 탭 관리
const activeTab = ref('reports')

// ========== 신고 관리 탭 상태 ==========
const reports = ref([])
const reportCurrentPage = ref(0)
const reportPageSize = ref(10)
const reportTotalElements = ref(0)
const reportTotalPages = ref(0)
const reportIsLoading = ref(false)
const reportError = ref('')
const reportSuccessMessage = ref('')

// 모달 상태
const isModalOpen = ref(false)
const selectedReport = ref(null)

const reportStatusMap = {
  SUBMITTED: { label: '대기중', color: '#f39c12', bgColor: '#fff3cd' },
  APPROVED: { label: '승인됨', color: '#27ae60', bgColor: '#d4edda' },
  REJECTED: { label: '거절됨', color: '#e74c3c', bgColor: '#f8d7da' }
}

const reportTypeMap = {
  SPAM: '스팸/광고',
  ABUSE: '욕설/비난',
  INAPPROPRIATE: '부적절한 내용',
  COPYRIGHT: '저작권 침해',
  OTHER: '기타'
}

// ========== 삭제된 게시글 탭 상태 ==========
const deletedBoards = ref([])
const deletedCurrentPage = ref(0)
const deletedPageSize = ref(15)
const deletedTotalElements = ref(0)
const deletedTotalPages = ref(0)
const deletedSort = ref('latest')
const deletedIsLoading = ref(false)
const deletedError = ref('')

// ========== 신고 관리 메서드 ==========

const fetchReports = async () => {
  reportIsLoading.value = true
  reportError.value = ''

  try {
    const response = await reportApi.getReportList(reportCurrentPage.value, reportPageSize.value)
    reports.value = response.data.content
    reportTotalElements.value = response.data.totalElements
    reportTotalPages.value = response.data.totalPages
  } catch (err) {
    reportError.value = '신고 목록을 불러오지 못했습니다'
    console.error('신고 목록 조회 실패:', err)
  } finally {
    reportIsLoading.value = false
  }
}

const approveReport = async (reportId) => {
  if (!confirm('이 신고를 승인하시겠습니까?')) {
    return
  }

  try {
    await reportApi.approveReport(reportId)
    reportSuccessMessage.value = '신고가 승인되었습니다'
    setTimeout(() => {
      reportSuccessMessage.value = ''
      fetchReports()
    }, 1500)
  } catch (err) {
    reportError.value = '신고 승인에 실패했습니다'
    console.error('신고 승인 실패:', err)
  }
}

const rejectReport = async (reportId) => {
  if (!confirm('이 신고를 거절하시겠습니까?')) {
    return
  }

  try {
    await reportApi.rejectReport(reportId)
    reportSuccessMessage.value = '신고가 거절되었습니다'
    setTimeout(() => {
      reportSuccessMessage.value = ''
      fetchReports()
    }, 1500)
  } catch (err) {
    reportError.value = '신고 거절에 실패했습니다'
    console.error('신고 거절 실패:', err)
  }
}

const goToReportPageIndex = (page) => {
  if (page >= 0 && page < reportTotalPages.value) {
    reportCurrentPage.value = page
    fetchReports()
  }
}

const previousReportPage = () => {
  goToReportPageIndex(reportCurrentPage.value - 1)
}

const nextReportPage = () => {
  goToReportPageIndex(reportCurrentPage.value + 1)
}

const openDetailModal = (report) => {
  selectedReport.value = report
  isModalOpen.value = true
}

const closeDetailModal = () => {
  isModalOpen.value = false
  selectedReport.value = null
}

const goToBoardDetail = (boardId, isDeleted = false) => {
  const path = isDeleted ? `/boards/${boardId}/admin` : `/boards/${boardId}`
  const route = router.resolve(path)
  window.open(route.href, '_blank')
}

const handleApproveFromModal = async (reportId) => {
  closeDetailModal()
  await approveReport(reportId)
}

const handleRejectFromModal = async (reportId) => {
  closeDetailModal()
  await rejectReport(reportId)
}

// ========== 삭제된 게시글 메서드 ==========

const fetchDeletedBoards = async () => {
  deletedIsLoading.value = true
  deletedError.value = ''

  try {
    const response = await boardApi.getDeletedBoards(deletedCurrentPage.value, deletedPageSize.value, deletedSort.value)
    deletedBoards.value = response.data.content
    deletedTotalElements.value = response.data.totalElements
    deletedTotalPages.value = response.data.totalPages
  } catch (err) {
    if (err.response?.status === 403) {
      deletedError.value = '관리자 권한이 필요합니다'
    } else {
      deletedError.value = '삭제된 게시글을 불러오지 못했습니다'
    }
    console.error('삭제된 게시글 조회 실패:', err)
  } finally {
    deletedIsLoading.value = false
  }
}

const handleDeletedSortChange = () => {
  deletedCurrentPage.value = 0
  fetchDeletedBoards()
}

const goToDeletedPageIndex = (page) => {
  if (page >= 0 && page < deletedTotalPages.value) {
    deletedCurrentPage.value = page
    fetchDeletedBoards()
  }
}

const previousDeletedPage = () => {
  goToDeletedPageIndex(deletedCurrentPage.value - 1)
}

const nextDeletedPage = () => {
  goToDeletedPageIndex(deletedCurrentPage.value + 1)
}

// ========== 탭 전환 ==========

const switchTab = (tab) => {
  activeTab.value = tab
  if (tab === 'deleted-boards' && deletedBoards.value.length === 0) {
    fetchDeletedBoards()
  }
}

// ========== 유틸리티 함수 ==========

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('ko-KR', {
    year: '2-digit',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).replace(/\. /g, '.').replace(/\.$/, '')
}

onMounted(() => {
  fetchReports()
})
</script>

<template>
  <div class="admin-dashboard">
    <div class="header">
      <h1>관리자 페이지</h1>
      <p class="subtitle">신고 관리 및 삭제된 게시글 조회</p>
    </div>

    <!-- 탭 메뉴 -->
    <nav class="tab-menu">
      <button
        class="tab-button"
        :class="{ active: activeTab === 'reports' }"
        @click="switchTab('reports')"
      >
        신고 관리
      </button>
      <button
        class="tab-button"
        :class="{ active: activeTab === 'deleted-boards' }"
        @click="switchTab('deleted-boards')"
      >
        삭제된 게시글
      </button>
    </nav>

    <!-- ========== 신고 관리 탭 ========== -->
    <div v-if="activeTab === 'reports'" class="tab-content">
      <!-- 메시지 -->
      <div v-if="reportError" class="alert alert-error">
        {{ reportError }}
        <button @click="reportError = ''" class="btn-close-alert">✕</button>
      </div>

      <div v-if="reportSuccessMessage" class="alert alert-success">
        {{ reportSuccessMessage }}
      </div>

      <!-- 로딩 -->
      <div v-if="reportIsLoading" class="loading">
        <span class="spinner"></span>
        <p>신고 목록을 불러오는 중...</p>
      </div>

      <!-- 신고 목록 -->
      <div v-else class="section">
        <div v-if="reports.length === 0" class="empty-state">
          <p>신고 내역이 없습니다</p>
        </div>

        <table v-else class="table">
          <thead>
            <tr>
              <th class="col-id">ID</th>
              <th class="col-status">상태</th>
              <th class="col-type">유형</th>
              <th class="col-reporter">신고자</th>
              <th class="col-title">게시글제목</th>
              <th class="col-date">날짜</th>
              <th class="col-actions">액션</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="report in reports"
              :key="report.reportId"
              @click="openDetailModal(report)"
              class="table-row"
            >
              <td class="col-id">{{ report.reportId }}</td>
              <td class="col-status">
                <span
                  class="status-badge"
                  :style="{
                    color: reportStatusMap[report.status].color,
                    backgroundColor: reportStatusMap[report.status].bgColor,
                    borderColor: reportStatusMap[report.status].color
                  }"
                >
                  {{ reportStatusMap[report.status].label }}
                </span>
              </td>
              <td class="col-type">{{ reportTypeMap[report.type] }}</td>
              <td class="col-reporter">{{ report.reporterName }}</td>
              <td class="col-title">
                <div class="title-wrapper">
                  <span class="title-text">{{ report.boardTitle }}</span>
                </div>
              </td>
              <td class="col-date">{{ formatDate(report.reportedDate) }}</td>
              <td class="col-actions" @click.stop>
                <div class="action-buttons">
                  <button
                    @click="goToBoardDetail(report.boardId)"
                    class="btn btn-view"
                    title="게시글 보기"
                  >
                    보기
                  </button>
                  <button
                    v-if="report.status === 'SUBMITTED'"
                    @click="approveReport(report.reportId)"
                    class="btn btn-approve"
                    title="신고 승인"
                  >
                    승인
                  </button>
                  <button
                    v-if="report.status === 'SUBMITTED'"
                    @click="rejectReport(report.reportId)"
                    class="btn btn-reject"
                    title="신고 거절"
                  >
                    거절
                  </button>
                  <span v-else class="processed-text">처리됨</span>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 페이지네이션 -->
      <div v-if="reportTotalPages > 1 && !reportIsLoading" class="pagination">
        <button
          @click="previousReportPage"
          :disabled="reportCurrentPage === 0"
          class="btn-nav"
        >
          이전
        </button>

        <div class="page-info">
          페이지 {{ reportCurrentPage + 1 }} / {{ reportTotalPages }}
          (총 {{ reportTotalElements }}개)
        </div>

        <button
          @click="nextReportPage"
          :disabled="reportCurrentPage === reportTotalPages - 1"
          class="btn-nav"
        >
          다음
        </button>
      </div>
    </div>

    <!-- ========== 삭제된 게시글 탭 ========== -->
    <div v-if="activeTab === 'deleted-boards'" class="tab-content">
      <!-- 에러 메시지 -->
      <div v-if="deletedError" class="alert alert-error">
        {{ deletedError }}
        <button @click="deletedError = ''" class="btn-close-alert">✕</button>
      </div>

      <!-- 정렬 옵션 -->
      <div class="sort-bar">
        <span class="sort-label">정렬:</span>
        <select v-model="deletedSort" @change="handleDeletedSortChange" class="sort-select">
          <option value="latest">최신 삭제순</option>
          <option value="viewcount">조회수순</option>
          <option value="likecount">좋아요순</option>
          <option value="commentcount">댓글수순</option>
        </select>
      </div>

      <!-- 로딩 -->
      <div v-if="deletedIsLoading" class="loading">
        <span class="spinner"></span>
        <p>삭제된 게시글을 불러오는 중...</p>
      </div>

      <!-- 삭제된 게시글 목록 -->
      <div v-else class="section">
        <div v-if="deletedBoards.length === 0" class="empty-state">
          <p>삭제된 게시글이 없습니다</p>
        </div>

        <table v-else class="table">
          <thead>
            <tr>
              <th class="col-id">ID</th>
              <th class="col-title">제목</th>
              <th class="col-author">작성자</th>
              <th class="col-delete-date">삭제일시</th>
              <th class="col-views">조회수</th>
              <th class="col-likes">좋아요</th>
              <th class="col-comments">댓글</th>
              <th class="col-actions">액션</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="board in deletedBoards" :key="board.boardId" class="table-row">
              <td class="col-id">{{ board.boardId }}</td>
              <td class="col-title">
                <span class="title-text">{{ board.boardTitle }}</span>
              </td>
              <td class="col-author">{{ board.memberName }}</td>
              <td class="col-delete-date">{{ formatDate(board.deleteTime) }}</td>
              <td class="col-views">{{ board.viewCount }}</td>
              <td class="col-likes">{{ board.likeCount }}</td>
              <td class="col-comments">{{ board.commentCount }}</td>
              <td class="col-actions">
                <button
                  @click="goToBoardDetail(board.boardId, true)"
                  class="btn btn-view"
                  title="게시글 상세보기"
                >
                  상세보기
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 페이지네이션 -->
      <div v-if="deletedTotalPages > 1 && !deletedIsLoading" class="pagination">
        <button
          @click="previousDeletedPage"
          :disabled="deletedCurrentPage === 0"
          class="btn-nav"
        >
          이전
        </button>

        <div class="page-info">
          페이지 {{ deletedCurrentPage + 1 }} / {{ deletedTotalPages }}
          (총 {{ deletedTotalElements }}개)
        </div>

        <button
          @click="nextDeletedPage"
          :disabled="deletedCurrentPage === deletedTotalPages - 1"
          class="btn-nav"
        >
          다음
        </button>
      </div>
    </div>

    <!-- 신고 상세 모달 -->
    <ReportDetailModal
      :report="selectedReport"
      :is-open="isModalOpen"
      @close="closeDetailModal"
      @approve="handleApproveFromModal"
      @reject="handleRejectFromModal"
    />
  </div>
</template>

<style scoped>
.admin-dashboard {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: #f8f9fa;
  min-height: 100vh;
}

.header {
  margin-bottom: 30px;
}

.header h1 {
  margin: 0 0 10px;
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
}

.subtitle {
  margin: 0;
  font-size: 14px;
  color: #7f8c8d;
}

/* 탭 메뉴 */
.tab-menu {
  display: flex;
  gap: 8px;
  margin-bottom: 30px;
  border-bottom: 2px solid #ecf0f1;
  background: white;
  padding: 0 0 0 0;
  border-radius: 6px 6px 0 0;
}

.tab-button {
  padding: 14px 24px;
  border: none;
  background: transparent;
  cursor: pointer;
  font-weight: 600;
  color: #7f8c8d;
  border-bottom: 3px solid transparent;
  transition: all 0.2s;
  font-size: 14px;
}

.tab-button:hover {
  color: #3498db;
}

.tab-button.active {
  color: #3498db;
  border-bottom-color: #3498db;
}

.tab-content {
  background: white;
  border-radius: 0 0 6px 6px;
  padding: 20px;
}

/* 알림 */
.alert {
  padding: 12px 16px;
  border-radius: 6px;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    transform: translateY(-10px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.alert-error {
  background-color: #fadbd8;
  border: 1px solid #f1948a;
  color: #922b21;
}

.alert-success {
  background-color: #d5f4e6;
  border: 1px solid #abebc6;
  color: #0b5345;
}

.btn-close-alert {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: inherit;
  padding: 0;
}

/* 로딩 */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: #7f8c8d;
}

.spinner {
  display: inline-block;
  width: 40px;
  height: 40px;
  border: 4px solid #ecf0f1;
  border-top-color: #3498db;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading p {
  margin: 0;
  font-size: 14px;
}

/* 빈 상태 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #7f8c8d;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px dashed #bdc3c7;
}

/* 섹션 */
.section {
  margin-bottom: 30px;
  border-radius: 6px;
  overflow: hidden;
}

/* 정렬 바 */
.sort-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 0;
}

.sort-label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.sort-select {
  padding: 8px 12px;
  border: 1px solid #bdc3c7;
  border-radius: 4px;
  background: white;
  cursor: pointer;
  font-size: 14px;
  color: #2c3e50;
}

.sort-select:hover {
  border-color: #3498db;
}

.sort-select:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.1);
}

/* 테이블 */
.table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.table thead {
  background: #f8f9fa;
  border-bottom: 2px solid #ecf0f1;
}

.table th {
  padding: 14px 12px;
  font-weight: 600;
  color: #2c3e50;
  text-align: center;
  border-bottom: 1px solid #ecf0f1;
  font-size: 13px;
}

.table tbody tr {
  border-bottom: 1px solid #ecf0f1;
  transition: background 0.15s ease;
}

.table tbody tr:hover {
  background: #f8f9fa;
}

.table tbody tr:last-child {
  border-bottom: none;
}

.table td {
  padding: 12px;
  color: #2c3e50;
  vertical-align: middle;
  text-align: center;
}

/* 테이블 칼럼 스타일 */
.col-id {
  width: 80px;
  font-weight: 600;
  color: #7f8c8d;
}

.col-status {
  width: 100px;
}

.status-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 600;
  border: 1px solid;
  white-space: nowrap;
}

.col-type {
  width: 110px;
  color: #2c3e50;
  font-size: 12px;
}

.col-reporter {
  width: 100px;
  color: #2c3e50;
}

.col-title {
  text-align: left;
  padding-left: 16px !important;
  max-width: 300px;
}

.title-wrapper,
.col-title {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.title-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #2c3e50;
}

.table-row:hover .title-text {
  color: #3498db;
  text-decoration: underline;
}

.col-date,
.col-delete-date {
  width: 140px;
  color: #7f8c8d;
  font-size: 12px;
}

.col-author {
  width: 100px;
  color: #2c3e50;
}

.col-views,
.col-likes,
.col-comments {
  width: 80px;
  color: #2c3e50;
  font-weight: 500;
}

.col-actions {
  width: 180px;
}

.action-buttons {
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}

.processed-text {
  font-size: 11px;
  color: #95a5a6;
  font-weight: 500;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-weight: 600;
  font-size: 11px;
  transition: all 0.2s;
  white-space: nowrap;
}

.btn-view {
  background: #3498db;
  color: white;
}

.btn-view:hover {
  background: #2980b9;
}

.btn-reject {
  background: #e74c3c;
  color: white;
}

.btn-reject:hover {
  background: #c0392b;
}

.btn-approve {
  background: #27ae60;
  color: white;
}

.btn-approve:hover {
  background: #229954;
}

/* 페이지네이션 */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
  margin-top: 20px;
}

.btn-nav {
  padding: 8px 16px;
  border: 1px solid #bdc3c7;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.2s;
}

.btn-nav:hover:not(:disabled) {
  background: #3498db;
  color: white;
  border-color: #3498db;
}

.btn-nav:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  color: #95a5a6;
}

.page-info {
  font-size: 13px;
  color: #7f8c8d;
  font-weight: 500;
}

/* 반응형 */
@media (max-width: 1024px) {
  .table {
    font-size: 12px;
  }

  .col-type {
    width: 90px;
  }

  .col-reporter,
  .col-author {
    width: 80px;
  }

  .col-actions {
    width: 150px;
  }

  .btn {
    padding: 5px 8px;
    font-size: 10px;
  }
}

@media (max-width: 768px) {
  .admin-dashboard {
    padding: 10px;
  }

  .table {
    font-size: 11px;
  }

  .col-type,
  .col-date,
  .col-delete-date,
  .col-views,
  .col-likes,
  .col-comments {
    display: none;
  }

  .col-id {
    width: 50px;
  }

  .col-status {
    width: 80px;
  }

  .col-reporter,
  .col-author {
    width: 70px;
    font-size: 11px;
  }

  .col-actions {
    width: 120px;
  }

  .action-buttons {
    flex-direction: column;
    gap: 2px;
  }

  .btn {
    width: 100%;
    padding: 4px 6px;
    font-size: 10px;
  }

  .header h1 {
    font-size: 22px;
  }

  .pagination {
    flex-wrap: wrap;
  }
}
</style>
