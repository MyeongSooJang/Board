<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { memberApi } from '../../api/member'

const router = useRouter()

const memberId = ref(localStorage.getItem('memberId'))
const formData = ref({
  name: '',
  password: '',
  passwordConfirm: '',
  age: '',
  phone: ''
})

const errors = ref({})
const isLoading = ref(false)
const successMessage = ref('')

onMounted(async () => {
  // 현재 사용자 정보 조회
  try {
    const response = await memberApi.getById(memberId.value)
    const member = response.data

    formData.value = {
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
})

const validateForm = () => {
  errors.value = {}

  if (!formData.value.name || formData.value.name.trim().length < 2) {
    errors.value.name = '이름은 2자 이상이어야 합니다'
  }

  if (formData.value.password) {
    if (formData.value.password.length < 8) {
      errors.value.password = '비밀번호는 8자 이상이어야 합니다'
    }
    if (formData.value.password !== formData.value.passwordConfirm) {
      errors.value.passwordConfirm = '비밀번호가 일치하지 않습니다'
    }
  }

  if (formData.value.age && (formData.value.age < 18 || formData.value.age > 150)) {
    errors.value.age = '나이는 18~150세여야 합니다'
  }

  if (formData.value.phone && !/^01[0-9]-\d{3,4}-\d{4}$/.test(formData.value.phone)) {
    errors.value.phone = '휴대폰 형식: 010-1234-5678'
  }

  return Object.keys(errors.value).length === 0
}

const handleSubmit = async () => {
  if (!validateForm()) {
    return
  }

  isLoading.value = true
  successMessage.value = ''

  try {
    // 비어있는 필드는 전송하지 않음
    const updateData = {}
    if (formData.value.name) updateData.name = formData.value.name
    if (formData.value.password) updateData.password = formData.value.password
    if (formData.value.age) updateData.age = formData.value.age
    if (formData.value.phone) updateData.phone = formData.value.phone

    const response = await memberApi.update(memberId.value, updateData)

    // 로컬스토리지의 회원명 업데이트
    localStorage.setItem('memberName', response.data.name)

    successMessage.value = '회원 정보가 수정되었습니다.'
    formData.value.password = ''
    formData.value.passwordConfirm = ''

    setTimeout(() => {
      router.push('/boards')
    }, 1500)
  } catch (err) {
    const errorMsg = err.response?.data?.message || '정보 수정에 실패했습니다'
    alert(errorMsg)
  } finally {
    isLoading.value = false
  }
}

const handleCancel = () => {
  router.push('/boards')
}
</script>

<template>
  <div class="profile-container">
    <div class="profile-card">
      <h2>프로필 수정</h2>

      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="name">이름</label>
          <input
            id="name"
            v-model="formData.name"
            type="text"
            class="input-field"
            placeholder="이름을 입력하세요"
          />
          <span v-if="errors.name" class="error-text">{{ errors.name }}</span>
        </div>

        <div class="form-group">
          <label for="password">비밀번호 (변경하지 않으면 비워두세요)</label>
          <input
            id="password"
            v-model="formData.password"
            type="password"
            class="input-field"
            placeholder="새 비밀번호"
          />
          <span v-if="errors.password" class="error-text">{{ errors.password }}</span>
        </div>

        <div class="form-group">
          <label for="passwordConfirm">비밀번호 확인</label>
          <input
            id="passwordConfirm"
            v-model="formData.passwordConfirm"
            type="password"
            class="input-field"
            placeholder="비밀번호 확인"
          />
          <span v-if="errors.passwordConfirm" class="error-text">{{ errors.passwordConfirm }}</span>
        </div>

        <div class="form-group">
          <label for="age">나이</label>
          <input
            id="age"
            v-model.number="formData.age"
            type="number"
            class="input-field"
            placeholder="나이를 입력하세요"
            min="18"
            max="150"
          />
          <span v-if="errors.age" class="error-text">{{ errors.age }}</span>
        </div>

        <div class="form-group">
          <label for="phone">휴대폰</label>
          <input
            id="phone"
            v-model="formData.phone"
            type="text"
            class="input-field"
            placeholder="010-1234-5678"
          />
          <span v-if="errors.phone" class="error-text">{{ errors.phone }}</span>
        </div>

        <div v-if="successMessage" class="success-message">{{ successMessage }}</div>

        <div class="button-group">
          <button type="submit" class="submit-btn" :disabled="isLoading">
            {{ isLoading ? '수정 중...' : '수정 완료' }}
          </button>
          <button type="button" class="cancel-btn" @click="handleCancel">
            취소
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  width: 100%;
}

.profile-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.profile-card h2 {
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

.input-field {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.input-field:focus {
  outline: none;
  border-color: #42b883;
  box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
}

.error-text {
  display: block;
  color: #e74c3c;
  font-size: 0.85rem;
  margin-top: 0.3rem;
}

.success-message {
  background-color: #d4edda;
  color: #155724;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  text-align: center;
}

.button-group {
  display: flex;
  gap: 1rem;
}

.submit-btn {
  flex: 1;
  padding: 0.75rem;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-btn:hover:not(:disabled) {
  background-color: #36a372;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.cancel-btn {
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

.cancel-btn:hover {
  background-color: #7f8c8d;
}
</style>
