<script setup>
import { ref, onMounted } from 'vue'
import { reportApi } from '../../api/report'

const reports = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const totalElements = ref(0)
const totalPages = ref(0)
const isLoading = ref(false)
const error = ref('')
const successMessage = ref('')

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

// 포맷팅 함수
const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('ko-KR')
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

      <div v-else class="report-grid">
        <div v-for="report in reports" :key="report.reportId" class="report-card">
          <!-- 헤더 -->
          <div class="card-header">
            <div class="report-id">신고 ID: {{ report.reportId }}</div>
            <div
              class="status-badge"
              :style="{
                color: reportStatusMap[report.status].color,
                backgroundColor: reportStatusMap[report.status].bgColor,
                borderColor: reportStatusMap[report.status].color
              }"
            >
              {{ reportStatusMap[report.status].label }}
            </div>
          </div>

          <!-- 신고 정보 -->
          <div class="card-content">
            <!-- 신고 기본 정보 -->
            <div class="info-row">
              <span class="label">신고 유형:</span>
              <span class="value">{{ reportTypeMap[report.type] }}</span>
            </div>

            <div class="info-row">
              <span class="label">신고일시:</span>
              <span class="value">{{ formatDate(report.reportedDate) }}</span>
            </div>

            <!-- 신고 내용 -->
            <div class="info-row full-width">
              <span class="label">신고 내용:</span>
              <p class="report-content">{{ report.content }}</p>
            </div>

            <!-- 신고자 정보 -->
            <div class="info-section">
              <h4>신고자</h4>
              <div class="info-row">
                <span class="label">이름:</span>
                <span class="value">{{ report.reporterName }}</span>
              </div>
              <div class="info-row">
                <span class="label">아이디:</span>
                <span class="value">{{ report.reporterUsername }}</span>
              </div>
            </div>

            <!-- 피신고 게시글 정보 -->
            <div class="info-section">
              <h4>신고된 게시글</h4>
              <div class="info-row">
                <span class="label">게시글 ID:</span>
                <span class="value">{{ report.boardId }}</span>
              </div>
              <div class="info-row">
                <span class="label">제목:</span>
                <span class="value">{{ report.boardTitle }}</span>
              </div>
              <div class="info-row">
                <span class="label">작성자:</span>
                <span class="value">{{ report.reportedName }} ({{ report.reportedUsername }})</span>
              </div>
            </div>
          </div>

          <!-- 액션 버튼 -->
          <div
            v-if="report.status === 'SUBMITTED'"
            class="card-footer"
          >
            <button
              @click="rejectReport(report.reportId)"
              class="btn btn-reject"
            >
              거절
            </button>
            <button
              @click="approveReport(report.reportId)"
              class="btn btn-approve"
            >
              승인
            </button>
          </div>

          <div v-else class="card-footer disabled">
            <span class="processed-text">이미 처리된 신고입니다</span>
          </div>
        </div>
      </div>
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

/* 신고 카드 그리드 */
.report-section {
  margin-bottom: 30px;
}

.report-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(700px, 1fr));
  gap: 20px;
}

.report-card {
  background: white;
  border: 1px solid #ecf0f1;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: box-shadow 0.3s ease;
}

.report-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #f8f9fa;
  border-bottom: 1px solid #ecf0f1;
}

.report-id {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  border: 1px solid;
}

.card-content {
  padding: 16px;
}

.info-row {
  display: grid;
  grid-template-columns: 100px 1fr;
  gap: 12px;
  margin-bottom: 12px;
  font-size: 13px;
  align-items: center;
}

.info-row.full-width {
  grid-template-columns: 1fr;
}

.info-row .label {
  font-weight: 600;
  color: #7f8c8d;
}

.info-row .value {
  color: #2c3e50;
  word-break: break-word;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.report-content {
  margin: 8px 0 0;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  line-height: 1.5;
  color: #2c3e50;
}

.info-section {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ecf0f1;
}

.info-section h4 {
  margin: 0 0 12px;
  font-size: 13px;
  font-weight: 700;
  color: #2c3e50;
}

.card-footer {
  display: flex;
  gap: 8px;
  padding: 12px 16px;
  background: #f8f9fa;
  border-top: 1px solid #ecf0f1;
  flex-wrap: nowrap;
}

.card-footer.disabled {
  justify-content: center;
}

.processed-text {
  font-size: 13px;
  color: #95a5a6;
  font-weight: 500;
}

.btn {
  flex: 1;
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  font-size: 13px;
  transition: all 0.2s;
  white-space: nowrap;
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
@media (max-width: 768px) {
  .report-grid {
    grid-template-columns: 1fr;
  }

  .info-row {
    grid-template-columns: 80px 1fr;
    font-size: 12px;
  }

  .btn {
    font-size: 12px;
    padding: 6px 10px;
  }

  .header h1 {
    font-size: 22px;
  }

  .pagination {
    flex-wrap: wrap;
  }
}
</style>
