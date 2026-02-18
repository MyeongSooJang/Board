<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { boardApi } from '../../api/board'
import { uploadFile, deleteFile, getFiles } from '../../api/file'

const router = useRouter()
const route = useRoute()

const isEdit = ref(!!route.params.boardId)
const boardId = ref(route.params.boardId || null)

const formData = ref({
  boardTitle: '',
  boardContent: ''
})

const files = ref([])
const selectedFile = ref(null)
const fileInputRef = ref(null)
const isLoading = ref(false)
const isUploadingFile = ref(false)
const error = ref('')

const MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB

const loadBoard = async () => {
  if (!isEdit.value) return

  isLoading.value = true
  error.value = ''

  try {
    const response = await boardApi.getDetail(boardId.value)
    formData.value.boardTitle = response.data.boardTitle
    formData.value.boardContent = response.data.boardContent

    // 기존 파일 목록 로드
    await loadFiles()
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
  }
}

const loadFiles = async () => {
  try {
    files.value = await getFiles(boardId.value)
  } catch (err) {
    console.error('파일 목록 로드 실패:', err)
  }
}

const handleFileSelect = (event) => {
  const file = event.target.files?.[0]
  if (!file) return

  error.value = ''

  // 파일 검증
  if (file.size > MAX_FILE_SIZE) {
    error.value = `파일 크기가 10MB를 초과합니다 (현재: ${(file.size / 1024 / 1024).toFixed(2)}MB)`
    fileInputRef.value.value = ''
    return
  }

  if (!file.type.startsWith('image/')) {
    error.value = '이미지 파일만 업로드 가능합니다'
    fileInputRef.value.value = ''
    return
  }

  selectedFile.value = file
}

const handleUploadFile = async () => {
  if (!selectedFile.value) {
    error.value = '업로드할 파일을 선택해주세요'
    return
  }

  if (!isEdit.value || !boardId.value) {
    error.value = '먼저 게시물을 작성해주세요'
    return
  }

  isUploadingFile.value = true
  error.value = ''

  try {
    const response = await uploadFile(boardId.value, selectedFile.value)
    files.value.push(response)
    selectedFile.value = null
    fileInputRef.value.value = ''
    alert('파일이 업로드되었습니다')
  } catch (err) {
    error.value = err.response?.data?.message || '파일 업로드에 실패했습니다'
  } finally {
    isUploadingFile.value = false
  }
}

const handleDeleteFile = async (fileId) => {
  if (!confirm('파일을 삭제하시겠습니까?')) return

  isUploadingFile.value = true
  error.value = ''

  try {
    await deleteFile(boardId.value, fileId)
    files.value = files.value.filter(f => f.fileId !== fileId)
    alert('파일이 삭제되었습니다')
  } catch (err) {
    error.value = err.response?.data?.message || '파일 삭제에 실패했습니다'
  } finally {
    isUploadingFile.value = false
  }
}

const handleSubmit = async () => {
  if (!formData.value.boardTitle.trim()) {
    error.value = '제목을 입력해주세요'
    return
  }

  if (!formData.value.boardContent.trim()) {
    error.value = '내용을 입력해주세요'
    return
  }

  isLoading.value = true
  error.value = ''

  try {
    if (isEdit.value) {
      await boardApi.update(
        boardId.value,
        formData.value.boardTitle,
        formData.value.boardContent
      )
      alert('게시물이 수정되었습니다')
    } else {
      const username = localStorage.getItem('username')
      const response = await boardApi.create(
        formData.value.boardTitle,
        formData.value.boardContent,
        username
      )
      alert('게시물이 작성되었습니다')
      boardId.value = response.data.boardId
    }

    router.push(`/boards/${boardId.value}`)
  } catch (err) {
    error.value = err.response?.data?.message || `게시물 ${isEdit.value ? '수정' : '작성'}에 실패했습니다`
  } finally {
    isLoading.value = false
  }
}

const handleCancel = () => {
  if (isEdit.value) {
    router.push(`/boards/${boardId.value}`)
  } else {
    router.push('/boards')
  }
}

onMounted(() => {
  loadBoard()
})
</script>

<template>
  <div class="board-form-container">
    <div class="form-card">
      <h2>{{ isEdit ? '게시물 수정' : '게시물 작성' }}</h2>

      <div v-if="error" class="error-message">{{ error }}</div>

      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="boardTitle">제목</label>
          <input
            id="boardTitle"
            v-model="formData.boardTitle"
            type="text"
            placeholder="제목을 입력하세요"
            class="input-field"
            :disabled="isLoading"
          />
        </div>

        <div class="form-group">
          <label for="boardContent">내용</label>
          <textarea
            id="boardContent"
            v-model="formData.boardContent"
            placeholder="내용을 입력하세요"
            class="textarea-field"
            rows="15"
            :disabled="isLoading"
          ></textarea>
        </div>

        <!-- 파일 업로드 섹션 (수정 모드에서만) -->
        <div v-if="isEdit" class="form-group">
          <label>파일 첨부</label>
          <div class="file-upload-section">
            <div class="file-input-wrapper">
              <input
                ref="fileInputRef"
                type="file"
                accept="image/*"
                class="file-input"
                @change="handleFileSelect"
                :disabled="isUploadingFile"
              />
              <label for="fileInput" class="file-label">
                파일 선택 (이미지만 가능, 최대 10MB)
              </label>
            </div>

            <button
              v-if="selectedFile"
              type="button"
              @click="handleUploadFile"
              class="btn btn-upload"
              :disabled="isUploadingFile"
            >
              {{ isUploadingFile ? '업로드 중...' : '업로드' }}
            </button>
          </div>

          <!-- 업로드된 파일 목록 -->
          <div v-if="files.length > 0" class="uploaded-files">
            <h4>첨부된 파일</h4>
            <div class="files-grid">
              <div v-for="file in files" :key="file.fileId" class="file-item">
                <img :src="file.url" :alt="file.url" class="file-preview" />
                <button
                  type="button"
                  @click="handleDeleteFile(file.fileId)"
                  class="btn btn-delete"
                  :disabled="isUploadingFile"
                >
                  삭제
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="form-actions">
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="isLoading"
          >
            {{ isLoading ? '처리 중...' : (isEdit ? '수정하기' : '작성하기') }}
          </button>
          <button
            type="button"
            @click="handleCancel"
            class="btn btn-secondary"
            :disabled="isLoading"
          >
            취소
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
body { color: #000; background-color: #f5f5f5; }

.board-form-container {
  display: flex;
  justify-content: center;
  min-height: 70vh;
  padding: 2rem 0;
}

.form-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 800px;
}

.form-card h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #34495e;
  font-weight: 500;
}

.input-field,
.textarea-field {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  font-family: inherit;
  transition: border-color 0.3s;
}

.input-field:focus,
.textarea-field:focus {
  outline: none;
  border-color: #42b883;
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.input-field:disabled,
.textarea-field:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  text-align: center;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primary {
  background-color: #42b883;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background-color: #36a372;
}

.btn-secondary {
  background-color: #95a5a6;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #7f8c8d;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 파일 업로드 섹션 */
.file-upload-section {
  display: flex;
  gap: 0.5rem;
  align-items: flex-start;
}

.file-input-wrapper {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.file-input {
  display: none;
}

.file-label {
  display: block;
  padding: 0.75rem;
  border: 2px dashed #42b883;
  border-radius: 4px;
  text-align: center;
  cursor: pointer;
  background-color: #f0fdf4;
  color: #16a34a;
  font-weight: 500;
  transition: all 0.3s;
}

.file-label:hover {
  background-color: #dcfce7;
  border-color: #16a34a;
}

.btn-upload {
  background-color: #2563eb;
  color: white;
  padding: 0.75rem 1rem;
  white-space: nowrap;
}

.btn-upload:hover:not(:disabled) {
  background-color: #1d4ed8;
}

/* 업로드된 파일 목록 */
.uploaded-files {
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid #ddd;
}

.uploaded-files h4 {
  margin-bottom: 1rem;
  color: #34495e;
  font-weight: 600;
}

.files-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.file-item {
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f5f5f5;
  transition: transform 0.3s;
}

.file-item:hover {
  transform: scale(1.05);
}

.file-preview {
  width: 100%;
  height: 150px;
  object-fit: cover;
  display: block;
}

.btn-delete {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  background-color: rgba(239, 68, 68, 0.9);
  color: white;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
  transform: translateY(100%);
  transition: transform 0.3s;
}

.file-item:hover .btn-delete {
  transform: translateY(0);
}

.btn-delete:hover:not(:disabled) {
  background-color: #dc2626;
}
</style>
