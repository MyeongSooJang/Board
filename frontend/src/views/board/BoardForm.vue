<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { boardApi } from '../../api/board'

const router = useRouter()
const route = useRoute()

const isEdit = ref(!!route.params.boardId)
const boardId = ref(route.params.boardId || null)

const formData = ref({
  boardTitle: '',
  boardContent: ''
})

const isLoading = ref(false)
const error = ref('')

const loadBoard = async () => {
  if (!isEdit.value) return

  isLoading.value = true
  error.value = ''

  try {
    const response = await boardApi.getDetail(boardId.value)
    formData.value.boardTitle = response.data.boardTitle
    formData.value.boardContent = response.data.boardContent
  } catch (err) {
    error.value = err.response?.data?.message || '게시물을 불러오지 못했습니다'
  } finally {
    isLoading.value = false
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
      const memberId = localStorage.getItem('memberId')
      const response = await boardApi.create(
        formData.value.boardTitle,
        formData.value.boardContent,
        memberId
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
</style>
