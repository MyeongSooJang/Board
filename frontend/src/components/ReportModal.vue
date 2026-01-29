<script setup>
import { ref } from 'vue'
import { reportApi } from '../api/report'

const props = defineProps({
  boardId: {
    type: Number,
    required: true
  },
  isOpen: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'success'])

const reportType = ref('SPAM')
const reportContent = ref('')
const isSubmitting = ref(false)
const error = ref('')

const reportTypes = [
  { value: 'SPAM', label: '스팸/광고' },
  { value: 'ABUSE', label: '욕설/비난' },
  { value: 'INAPPROPRIATE', label: '부적절한 내용' },
  { value: 'COPYRIGHT', label: '저작권 침해' },
  { value: 'OTHER', label: '기타' }
]

const handleSubmit = async () => {
  if (!reportContent.value.trim()) {
    error.value = '신고 내용을 입력해주세요'
    return
  }

  isSubmitting.value = true
  error.value = ''

  try {
    await reportApi.submitReport(props.boardId, reportType.value, reportContent.value)
    emit('success')
    resetForm()
  } catch (err) {
    error.value = err.response?.data?.message || '신고 제출에 실패했습니다'
    console.error('신고 제출 실패:', err)
  } finally {
    isSubmitting.value = false
  }
}

const handleClose = () => {
  resetForm()
  emit('close')
}

const resetForm = () => {
  reportType.value = 'SPAM'
  reportContent.value = ''
  error.value = ''
}
</script>

<template>
  <div v-if="isOpen" class="modal-overlay" @click="handleClose">
    <div class="modal-container" @click.stop>
      <div class="modal-header">
        <h3>게시글 신고</h3>
        <button class="btn-close" @click="handleClose">✕</button>
      </div>

      <div class="modal-content">
        <div v-if="error" class="error-message">{{ error }}</div>

        <div class="form-group">
          <label for="report-type">신고 유형</label>
          <select v-model="reportType" id="report-type" class="form-control">
            <option v-for="type in reportTypes" :key="type.value" :value="type.value">
              {{ type.label }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="report-content">신고 내용</label>
          <textarea
            v-model="reportContent"
            id="report-content"
            class="form-control"
            placeholder="신고 내용을 자세히 작성해주세요"
            rows="5"
          ></textarea>
        </div>
      </div>

      <div class="modal-footer">
        <button @click="handleClose" class="btn btn-secondary" :disabled="isSubmitting">
          취소
        </button>
        <button @click="handleSubmit" class="btn btn-report" :disabled="isSubmitting">
          {{ isSubmitting ? '제출 중...' : '신고 제출' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #e9ecef;
}

.modal-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #3d414d;
}

.btn-close {
  background: none;
  border: none;
  color: #999;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
  transition: color 0.2s;
}

.btn-close:hover {
  color: #333;
}

.modal-content {
  padding: 20px;
}

.error-message {
  background-color: #fff3cd;
  color: #856404;
  border: 1px solid #ffeaa7;
  border-radius: 3px;
  padding: 10px 12px;
  margin-bottom: 16px;
  font-size: 13px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group:last-child {
  margin-bottom: 0;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #3d414d;
}

.form-control {
  width: 100%;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-family: inherit;
  font-size: 13px;
  line-height: 1.4;
  box-sizing: border-box;
}

.form-control:focus {
  outline: none;
  border-color: #3d414d;
  box-shadow: 0 0 0 2px rgba(61, 65, 77, 0.1);
}

textarea.form-control {
  resize: vertical;
  min-height: 120px;
}

.modal-footer {
  display: flex;
  gap: 8px;
  padding: 16px 20px;
  border-top: 1px solid #e9ecef;
  background: #f7f9fa;
  justify-content: flex-end;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.2s;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  background: #95a5a6;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background: #7f8c8d;
}

.btn-report {
  background: #e67e22;
  color: white;
}

.btn-report:hover:not(:disabled) {
  background: #d35400;
}
</style>
