<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { reportApi } from '../../api/report'
import ReportDetailModal from '../../components/ReportDetailModal.vue'

const router = useRouter()

const reports = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const totalElements = ref(0)
const totalPages = ref(0)
const isLoading = ref(false)
const error = ref('')
const successMessage = ref('')

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

// 신고 목록 조회
const fetchReports = async () => {
  isLoading.value = true
  error.value = ''

  try {
    const response = await reportApi.getReportList(currentPage.value, pageSize.value)
    reports.value = response.data.content
    totalElements.value = response.data.totalElements
    totalPages.value = response.data.totalPages
  } catch (err) {
    error.value = '신고 목록을 불러오지 못했습니다'
    console.error('신고 목록 조회 실패:', err)
  } finally {
    isLoading.value = false
  }
}

// 신고 승인
const approveReport = async (reportId) => {
  if (!confirm('이 신고를 승인하시겠습니까?')) {
    return
  }

  try {
    await reportApi.approveReport(reportId)
    successMessage.value = '신고가 승인되었습니다'
    setTimeout(() => {
      successMessage.value = ''
      fetchReports()
    }, 1500)
  } catch (err) {
    error.value = '신고 승인에 실패했습니다'
    console.error('신고 승인 실패:', err)
  }
}

// 신고 거절
const rejectReport = async (reportId) => {
  if (!confirm('이 신고를 거절하시겠습니까?')) {
    return
  }

  try {
    await reportApi.rejectReport(reportId)
    successMessage.value = '신고가 거절되었습니다'
    setTimeout(() => {
      successMessage.value = ''
      fetchReports()
    }, 1500)
  } catch (err) {
    error.value = '신고 거절에 실패했습니다'
    console.error('신고 거절 실패:', err)
  }
}

// 페이지 변경
const goToPage = (page) => {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page
    fetchReports()
  }
}

// 이전 페이지
const previousPage = () => {
  goToPage(currentPage.value - 1)
}

// 다음 페이지
const nextPage = () => {
  goToPage(currentPage.value + 1)
}

// 포맷팅 함수 (테이블용 - 간략 형식)
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('ko-KR', {
    year: '2-digit',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).replace(/\. /g, '.').replace(/\.$/, '')
}

// 모달 열기 - 행 클릭 시
const openDetailModal = (report) => {
  selectedReport.value = report
  isModalOpen.value = true
}

// 모달 닫기
const closeDetailModal = () => {
  isModalOpen.value = false
  selectedReport.value = null
}

// 게시글 상세 페이지로 이동 (새 탭)
const goToBoardDetail = (boardId) => {
  const route = router.resolve(`/boards/${boardId}`)
  window.open(route.href, '_blank')
}

// 모달에서 승인
const handleApproveFromModal = async (reportId) => {
  closeDetailModal()
  await approveReport(reportId)
}

// 모달에서 거절
const handleRejectFromModal = async (reportId) => {
  closeDetailModal()
  await rejectReport(reportId)
}

onMounted(() => {
  fetchReports()
})
</script>

<template>
  <div class="report-list-container">
    <div class="header">
      <h1>신고 관리</h1>
      <p class="subtitle">사용자 신고를 검토하고 승인 또는 거절하세요</p>
    </div>

    <!-- 메시지 -->
    <div v-if="error" class="alert alert-error">
      {{ error }}
      <button @click="error = ''" class="btn-close-alert">✕</button>
    </div>

    <div v-if="successMessage" class="alert alert-success">
      {{ successMessage }}
    </div>

    <!-- 로딩 -->
    <div v-if="isLoading" class="loading">
      <span class="spinner"></span>
      <p>신고 목록을 불러오는 중...</p>
    </div>

    <!-- 신고 목록 -->
    <div v-else class="report-section">
      <div v-if="reports.length === 0" class="empty-state">
        <p>신고 내역이 없습니다</p>
      </div>

      <table v-else class="report-table">
        <thead>
          <tr>
            <th class="col-id">ID</th>
            <th class="col-status">상태</th>
            <th class="col-type">유형</th>
            <th class="col-reporter">신고자</th>
            <th class="col-board-title">게시글제목</th>
            <th class="col-date">날짜</th>
            <th class="col-actions">액션</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="report in reports"
            :key="report.reportId"
            @click="openDetailModal(report)"
            class="report-row"
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
            <td class="col-board-title">
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
    <div v-if="totalPages > 1 && !isLoading" class="pagination">
      <button
        @click="previousPage"
        :disabled="currentPage === 0"
        class="btn-nav"
      >
        이전
      </button>

      <div class="page-info">
        페이지 {{ currentPage + 1 }} / {{ totalPages }}
        (총 {{ totalElements }}개)
      </div>

      <button
        @click="nextPage"
        :disabled="currentPage === totalPages - 1"
        class="btn-nav"
      >
        다음
      </button>
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
.report-list-container {
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
  background: white;
  border-radius: 6px;
  border: 1px dashed #bdc3c7;
}

/* 신고 테이블 섹션 */
.report-section {
  margin-bottom: 30px;
  background: white;
  border: 1px solid #ecf0f1;
  border-radius: 6px;
  overflow: hidden;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.report-table thead {
  background: #f8f9fa;
  border-bottom: 2px solid #ecf0f1;
}

.report-table th {
  padding: 14px 12px;
  font-weight: 600;
  color: #2c3e50;
  text-align: center;
  border-bottom: 1px solid #ecf0f1;
  font-size: 13px;
}

.report-table tbody tr {
  border-bottom: 1px solid #ecf0f1;
  cursor: pointer;
  transition: background 0.15s ease;
}

.report-table tbody tr:hover {
  background: #f8f9fa;
}

.report-table tbody tr:last-child {
  border-bottom: none;
}

.report-table td {
  padding: 12px;
  color: #2c3e50;
  vertical-align: middle;
}

/* 컬럼별 스타일 */
.col-id {
  width: 80px;
  text-align: center;
  font-weight: 600;
  color: #7f8c8d;
}

.col-status {
  width: 100px;
  text-align: center;
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
  text-align: center;
  color: #2c3e50;
  font-size: 12px;
}

.col-reporter {
  width: 100px;
  text-align: center;
  color: #2c3e50;
}

.col-board-title {
  text-align: left;
  padding-left: 16px !important;
  max-width: 300px;
}

.title-wrapper {
  display: flex;
  align-items: center;
}

.title-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #2c3e50;
}

.report-row:hover .title-text {
  color: #3498db;
  text-decoration: underline;
}

.col-date {
  width: 140px;
  text-align: center;
  color: #7f8c8d;
  font-size: 12px;
}

.col-actions {
  width: 210px;
  text-align: center;
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
  background: white;
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
  .report-table {
    font-size: 12px;
  }

  .col-type {
    width: 90px;
  }

  .col-reporter {
    width: 80px;
  }

  .col-actions {
    width: 180px;
  }

  .btn {
    padding: 5px 8px;
    font-size: 10px;
  }
}

@media (max-width: 768px) {
  .report-list-container {
    padding: 10px;
  }

  .report-table {
    font-size: 11px;
  }

  .col-type,
  .col-date {
    display: none;
  }

  .col-id {
    width: 50px;
  }

  .col-status {
    width: 80px;
  }

  .col-reporter {
    width: 70px;
    font-size: 11px;
  }

  .col-actions {
    width: 150px;
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
