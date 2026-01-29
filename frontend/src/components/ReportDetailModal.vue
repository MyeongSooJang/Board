<script setup>
import { computed } from 'vue'

const props = defineProps({
  report: {
    type: Object,
    default: null
  },
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'approve', 'reject'])

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

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('ko-KR')
}

const isSubmitted = computed(() => {
  return props.report?.status === 'SUBMITTED'
})

const handleApprove = () => {
  if (confirm('이 신고를 승인하시겠습니까?')) {
    emit('approve', props.report.reportId)
  }
}

const handleReject = () => {
  if (confirm('이 신고를 거절하시겠습니까?')) {
    emit('reject', props.report.reportId)
  }
}

const handleClose = () => {
  emit('close')
}
</script>

<template>
  <div v-if="isOpen && report" class="modal-overlay" @click="handleClose">
    <div class="modal-container" @click.stop>
      <!-- 헤더 -->
      <div class="modal-header">
        <div class="header-content">
          <h3>신고 상세 정보</h3>
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
        </div>
        <button class="btn-close" @click="handleClose">✕</button>
      </div>

      <!-- 컨텐츠 -->
      <div class="modal-content">
        <!-- 신고 기본 정보 -->
        <div class="info-section">
          <h4 class="section-title">신고 정보</h4>
          <div class="info-grid">
            <div class="info-row">
              <span class="label">신고 ID:</span>
              <span class="value">{{ report.reportId }}</span>
            </div>
            <div class="info-row">
              <span class="label">신고 유형:</span>
              <span class="value">{{ reportTypeMap[report.type] }}</span>
            </div>
            <div class="info-row">
              <span class="label">신고일시:</span>
              <span class="value">{{ formatDate(report.reportedDate) }}</span>
            </div>
          </div>
        </div>

        <!-- 신고 내용 -->
        <div class="info-section">
          <h4 class="section-title">신고 내용</h4>
          <div class="content-box">
            {{ report.content }}
          </div>
        </div>

        <!-- 신고자 정보 -->
        <div class="info-section">
          <h4 class="section-title">신고자 정보</h4>
          <div class="info-grid">
            <div class="info-row">
              <span class="label">이름:</span>
              <span class="value">{{ report.reporterName }}</span>
            </div>
            <div class="info-row">
              <span class="label">아이디:</span>
              <span class="value">{{ report.reporterUsername }}</span>
            </div>
          </div>
        </div>

        <!-- 신고된 게시글 정보 -->
        <div class="info-section">
          <h4 class="section-title">신고된 게시글</h4>
          <div class="info-grid">
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
      </div>

      <!-- 푸터 (액션 버튼) -->
      <div class="modal-footer">
        <button @click="handleClose" class="btn btn-secondary">
          닫기
        </button>
        <div v-if="isSubmitted" class="action-group">
          <button @click="handleReject" class="btn btn-reject">
            거절
          </button>
          <button @click="handleApprove" class="btn btn-approve">
            승인
          </button>
        </div>
        <div v-else class="processed-info">
          <span class="processed-text">이미 처리된 신고입니다</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 모달 오버레이 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  animation: fadeIn 0.2s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 모달 컨테이너 */
.modal-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  max-width: 700px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

/* 헤더 */
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px 24px;
  border-bottom: 1px solid #ecf0f1;
  background: #f8f9fa;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  border: 1px solid;
}

.btn-close {
  background: none;
  border: none;
  color: #7f8c8d;
  font-size: 24px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
  transition: color 0.2s;
}

.btn-close:hover {
  color: #2c3e50;
}

/* 컨텐츠 */
.modal-content {
  padding: 24px;
  max-height: calc(90vh - 180px);
  overflow-y: auto;
}

.info-section {
  margin-bottom: 24px;
}

.info-section:last-child {
  margin-bottom: 0;
}

.section-title {
  margin: 0 0 12px;
  font-size: 14px;
  font-weight: 700;
  color: #2c3e50;
  padding-bottom: 8px;
  border-bottom: 2px solid #ecf0f1;
}

.info-grid {
  display: grid;
  gap: 12px;
}

.info-row {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 16px;
  font-size: 14px;
  align-items: center;
}

.info-row .label {
  font-weight: 600;
  color: #7f8c8d;
}

.info-row .value {
  color: #2c3e50;
  word-break: break-word;
}

.content-box {
  padding: 16px;
  background: #f8f9fa;
  border: 1px solid #ecf0f1;
  border-radius: 6px;
  line-height: 1.6;
  color: #2c3e50;
  font-size: 14px;
  min-height: 80px;
  white-space: pre-wrap;
}

/* 푸터 */
.modal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #ecf0f1;
  background: #f8f9fa;
}

.action-group {
  display: flex;
  gap: 8px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.2s;
  white-space: nowrap;
}

.btn-secondary {
  background: #95a5a6;
  color: white;
}

.btn-secondary:hover {
  background: #7f8c8d;
}

.btn-approve {
  background: #27ae60;
  color: white;
}

.btn-approve:hover {
  background: #229954;
}

.btn-reject {
  background: #e74c3c;
  color: white;
}

.btn-reject:hover {
  background: #c0392b;
}

.processed-info {
  display: flex;
  align-items: center;
}

.processed-text {
  font-size: 14px;
  color: #95a5a6;
  font-weight: 500;
}

/* 반응형 */
@media (max-width: 768px) {
  .modal-container {
    width: 95%;
    max-height: 95vh;
  }

  .info-row {
    grid-template-columns: 100px 1fr;
    font-size: 13px;
  }

  .modal-footer {
    flex-direction: column;
    gap: 8px;
  }

  .action-group {
    width: 100%;
  }

  .btn {
    flex: 1;
    font-size: 13px;
  }
}
</style>
