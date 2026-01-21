<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { memberApi } from '../../api/member'

const router = useRouter()

const formData = ref({
  username: '',
  name: '',
  password: '',
  passwordConfirm: '',
  age: '',
  email: '',
  phone: '',
  role: 'GUEST'
})

const error = ref('')
const fieldErrors = ref({})
const isLoading = ref(false)

const validateForm = () => {
  if (!formData.value.username) return '아이디를 입력해주세요'
  if (!formData.value.name) return '이름을 입력해주세요'
  if (!formData.value.password) return '비밀번호를 입력해주세요'
  if (formData.value.password !== formData.value.passwordConfirm) {
    return '비밀번호가 일치하지 않습니다'
  }
  if (!formData.value.age) return '나이를 입력해주세요'
  if (isNaN(formData.value.age)) return '나이는 숫자여야 합니다'
  if (!formData.value.email) return '이메일을 입력해주세요'
  if (!formData.value.email.includes('@')) return '올바른 이메일 형식이 아닙니다'
  if (!formData.value.phone) return '전화번호를 입력해주세요'
  return ''
}

const clearFieldError = (fieldName) => {
  delete fieldErrors.value[fieldName]
}

const handleSignup = async () => {
  const validationError = validateForm()
  if (validationError) {
    error.value = validationError
    fieldErrors.value = {}
    return
  }

  isLoading.value = true
  error.value = ''
  fieldErrors.value = {}

  try {
    await memberApi.signup(
      formData.value.username,
      formData.value.name,
      formData.value.password,
      parseInt(formData.value.age),
      formData.value.email,
      formData.value.phone,
      formData.value.role
    )

    alert('회원가입이 완료되었습니다. 로그인해주세요.')
    router.push('/login')
  } catch (err) {
    // 백엔드에서 받은 에러 응답 처리
    if (err.response?.data?.errors) {
      // 검증 실패 (필드별 에러) - 필드별 에러만 표시
      fieldErrors.value = err.response.data.errors
      error.value = ''
    } else {
      // 다른 에러 (중복, 기타) - 상단 메시지만 표시
      fieldErrors.value = {}
      error.value = err.response?.data?.message || '회원가입에 실패했습니다'
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="signup-container">
    <div class="signup-card">
      <h2>회원가입</h2>

      <form @submit.prevent="handleSignup">
        <div class="form-group">
          <label for="memberId">아이디</label>
          <input
            id="memberId"
            v-model="formData.username"
            type="text"
            placeholder="아이디를 입력하세요"
            :class="['input-field', { error: fieldErrors.username }]"
            @input="clearFieldError('username')"
          />
          <span v-if="fieldErrors.username" class="field-error">
            {{ fieldErrors.username }}
          </span>
        </div>

        <div class="form-group">
          <label for="memberName">이름</label>
          <input
            id="memberName"
            v-model="formData.name"
            type="text"
            placeholder="이름을 입력하세요"
            :class="['input-field', { error: fieldErrors.name }]"
            @input="clearFieldError('name')"
          />
          <span v-if="fieldErrors.name" class="field-error">
            {{ fieldErrors.name }}
          </span>
        </div>

        <div class="form-group">
          <label for="memberPwd">비밀번호</label>
          <input
            id="memberPwd"
            v-model="formData.password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            :class="['input-field', { error: fieldErrors.password }]"
            @input="clearFieldError('password')"
          />
          <span v-if="fieldErrors.password" class="field-error">
            {{ fieldErrors.password }}
          </span>
        </div>

        <div class="form-group">
          <label for="memberPwdConfirm">비밀번호 확인</label>
          <input
            id="memberPwdConfirm"
            v-model="formData.passwordConfirm"
            type="password"
            placeholder="비밀번호를 다시 입력하세요"
            class="input-field"
          />
        </div>

        <div class="form-group">
          <label for="memberAge">나이</label>
          <input
            id="memberAge"
            v-model="formData.age"
            type="number"
            placeholder="나이를 입력하세요"
            :class="['input-field', { error: fieldErrors.age }]"
            @input="clearFieldError('age')"
          />
          <span v-if="fieldErrors.age" class="field-error">
            {{ fieldErrors.age }}
          </span>
        </div>

        <div class="form-group">
          <label for="memberEmail">이메일</label>
          <input
            id="memberEmail"
            v-model="formData.email"
            type="email"
            placeholder="이메일을 입력하세요"
            :class="['input-field', { error: fieldErrors.email }]"
            @input="clearFieldError('email')"
          />
          <span v-if="fieldErrors.email" class="field-error">
            {{ fieldErrors.email }}
          </span>
        </div>

        <div class="form-group">
          <label for="memberPhone">전화번호</label>
          <input
            id="memberPhone"
            v-model="formData.phone"
            type="tel"
            placeholder="010-xxxx-xxxx"
            :class="['input-field', { error: fieldErrors.phone }]"
            @input="clearFieldError('phone')"
          />
          <span v-if="fieldErrors.phone" class="field-error">
            {{ fieldErrors.phone }}
          </span>
        </div>

        <div class="form-group">
          <label for="memberRole">회원 역할</label>
          <select
            id="memberRole"
            v-model="formData.role"
            class="input-field"
          >
            <option value="GUEST">일반 사용자</option>
            <option value="ADMIN">관리자</option>
          </select>
        </div>

        <div v-if="error" class="error-message">{{ error }}</div>

        <button
          type="submit"
          class="submit-btn"
          :disabled="isLoading"
        >
          {{ isLoading ? '가입 중...' : '회원가입' }}
        </button>
      </form>

      <div class="login-link">
        이미 회원이신가요?
        <router-link to="/login">로그인하기</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
body { color: #000; background-color: #f5f5f5; }

.signup-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem 0;
}

.signup-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.signup-card h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #2c3e50;
}

.form-group {
  margin-bottom: 1.2rem;
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

.input-field.error {
  border-color: #dc3545;
  border-width: 2px;
  background-color: #fff;
}

.input-field.error:focus {
  border-color: #dc3545;
  box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.1);
}

.field-error {
  display: block;
  color: #dc3545;
  font-size: 0.875rem;
  margin-top: 0.25rem;
  margin-bottom: 0.5rem;
}

.error-message {
  background-color: #f8d7da;
  color: #721c24;
  padding: 0.75rem;
  border-radius: 4px;
  margin-bottom: 1rem;
  text-align: center;
}

.submit-btn {
  width: 100%;
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

.login-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #7f8c8d;
}

.login-link a {
  color: #42b883;
  text-decoration: none;
  font-weight: bold;
  margin-left: 0.5rem;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
