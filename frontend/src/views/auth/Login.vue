<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../../api/auth'

const router = useRouter()

const formData = ref({
  username: '',
  password: ''
})

const error = ref('')
const isLoading = ref(false)

const handleLogin = async () => {
  if (!formData.value.username || !formData.value.password) {
    error.value = '아이디와 비밀번호를 입력해주세요'
    return
  }

  isLoading.value = true
  error.value = ''

  try {
    const response = await authApi.login(
      formData.value.username,
      formData.value.password
    )

    const { accessToken, refreshToken, username, memberName, role } = response.data

    // 토큰 저장
    localStorage.setItem('accessToken', accessToken)
    localStorage.setItem('refreshToken', refreshToken)
    localStorage.setItem('username', username)
    localStorage.setItem('memberName', memberName)
    localStorage.setItem('role', role)

    router.push('/boards')
  } catch (err) {
    error.value = err.response?.data?.message || '로그인에 실패했습니다'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <h2>로그인</h2>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">아이디</label>
          <input
            id="username"
            v-model="formData.username"
            type="text"
            placeholder="아이디를 입력하세요"
            class="input-field"
          />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            id="password"
            v-model="formData.password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            class="input-field"
          />
        </div>

        <div v-if="error" class="error-message">{{ error }}</div>

        <button
          type="submit"
          class="submit-btn"
          :disabled="isLoading"
        >
          {{ isLoading ? '로그인 중...' : '로그인' }}
        </button>
      </form>

      <div class="signup-link">
        아직 회원이 아니신가요?
        <router-link to="/signup">회원가입하기</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
body { color: #000; background-color: #f5f5f5; }

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.login-card {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-card h2 {
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

.signup-link {
  text-align: center;
  margin-top: 1.5rem;
  color: #7f8c8d;
}

.signup-link a {
  color: #42b883;
  text-decoration: none;
  font-weight: bold;
  margin-left: 0.5rem;
}

.signup-link a:hover {
  text-decoration: underline;
}
</style>
