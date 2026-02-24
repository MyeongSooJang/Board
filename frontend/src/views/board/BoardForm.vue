<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { boardApi } from '../../api/board'
import { Editor } from '@toast-ui/vue-editor'
import '@toast-ui/editor/dist/toastui-editor.css'
import { getPresignedUrl, uploadToS3 } from '../../api/image'

const router = useRouter()
const route = useRoute()

const isEdit = ref(!!route.params.boardId)
const boardId = ref(route.params.boardId || null)

const API_BASE_URL = 'http://localhost:8080'

const formData = ref({
  boardTitle: '',
  boardContent: ''
})

const editorRef = ref(null)
const isLoading = ref(false)
const error = ref('')

const MAX_FILE_SIZE = 10 * 1024 * 1024 // 10MB

const loadBoard = async () => {
  if (!isEdit.value) return

  isLoading.value = true
  error.value = ''

  try {
    const response = await boardApi.getDetail(boardId.value)
    formData.value.boardTitle = response.data.boardTitle

    // 에디터 인스턴스에 HTML 직접 설정
    if (editorRef.value) {
      editorRef.value.invoke('setHTML', response.data.boardContent)
    }
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
  }
}

/**
 * Toast UI Editor의 이미지 업로드 훅
 * 사용자가 이미지를 삽입할 때마다 자동 호출됨
 *
 * @param {Blob} blob - 이미지 Blob 객체
 * @param {Function} callback - 업로드 완료 후 이미지 URL 전달
 */
const handleImageUpload = async (blob, callback) => {
  try {
    // 1. 파일 타입 검증
    if (!blob.type.startsWith('image/')) {
      alert('이미지 파일만 업로드 가능합니다')
      callback('', '') // 빈 URL = 업로드 실패
      return
    }

    // 2. 파일 크기 검증 (10MB)
    const MAX_SIZE = 10 * 1024 * 1024
    if (blob.size > MAX_SIZE) {
      alert('이미지 크기는 10MB를 초과할 수 없습니다')
      callback('', '')
      return
    }

    // 3. Presigned URL 발급
    const fileName = `image_${Date.now()}.${blob.type.split('/')[1]}`
    const { uploadUrl, imageUrl } = await getPresignedUrl(fileName, blob.type)

    // 4. S3에 직접 업로드
    await uploadToS3(uploadUrl, blob, blob.type)

    // 5. 에디터에 이미지 삽입 (imageUrl 사용)
    callback(imageUrl, fileName)

    console.log('이미지 업로드 성공:', imageUrl)
  } catch (error) {
    console.error('이미지 업로드 실패:', error)
    alert('이미지 업로드에 실패했습니다')
    callback('', '')
  }
}

const handleSubmit = async () => {
  if (!formData.value.boardTitle.trim()) {
    error.value = '제목을 입력해주세요'
    return
  }

  // 에디터에서 HTML 가져오기
  const editorContent = editorRef.value ? editorRef.value.invoke('getHTML') : ''

  if (!editorContent.trim() || editorContent === '<p><br></p>') {
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
        editorContent
      )
      alert('게시물이 수정되었습니다')
    } else {
      const username = localStorage.getItem('username')
      const response = await boardApi.create(
        formData.value.boardTitle,
        editorContent,
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
          <Editor
            ref="editorRef"
            :initialValue="formData.boardContent"
            initialEditType="wysiwyg"
            :options="{
              hideModeSwitch: true,
              height: '500px',
              placeholder: '내용을 입력하세요',
              hooks: {
                addImageBlobHook: handleImageUpload
              }
            }"
            :disabled="isLoading"
          />
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

/* 선택된 파일 목록 */
.selected-files {
  margin-top: 1.5rem;
  padding: 1rem;
  background-color: #f9f9f9;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.selected-files h4 {
  margin: 0 0 0.75rem 0;
  color: #34495e;
  font-weight: 600;
  font-size: 0.95rem;
}

.files-list {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.file-list-item {
  display: flex;
  align-items: center;
  padding: 0.5rem;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  justify-content: space-between;
}

.file-name {
  flex: 1;
  font-weight: 500;
  color: #2c3e50;
  word-break: break-word;
}

.file-size {
  color: #7f8c8d;
  font-size: 0.9rem;
  margin: 0 0.5rem;
}

.btn-remove {
  background-color: #e74c3c;
  color: white;
  padding: 0.4rem 0.8rem;
  font-size: 0.9rem;
  white-space: nowrap;
}

.btn-remove:hover:not(:disabled) {
  background-color: #c0392b;
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
