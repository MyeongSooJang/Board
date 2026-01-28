<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const isLoggedIn = ref(!!localStorage.getItem('accessToken'))
const memberName = ref(localStorage.getItem('memberName') || '')
const memberId = ref(localStorage.getItem('memberId') || '')

const searchType = ref('title')
const searchQuery = ref('')

const updateAuthState = () => {
  isLoggedIn.value = !!localStorage.getItem('accessToken')
  memberName.value = localStorage.getItem('memberName') || ''
  memberId.value = localStorage.getItem('memberId') || ''
}

const handleLogout = () => {
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  localStorage.removeItem('memberId')
  localStorage.removeItem('memberName')
  isLoggedIn.value = false
  memberName.value = ''
  memberId.value = ''
  router.push('/')
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({
      path: '/boards',
      query: {
        searchType: searchType.value,
        searchQuery: searchQuery.value
      }
    })
  }
}

const handleSearchKeydown = (event) => {
  if (event.key === 'Enter') {
    handleSearch()
  }
}

const goHome = () => {
  router.push('/')
}

// 라우터 변경 시 로그인 상태 업데이트
watch(
  () => router.currentRoute.value.path,
  () => {
    updateAuthState()
  }
)

// 다른 탭에서 변경할 때
window.addEventListener('storage', () => {
  updateAuthState()
})
</script>

<template>
  <div id="app">
    <header class="app-header">
      <div class="header-container">
        <div class="header-left">
          <button class="logo-btn" @click="goHome" title="홈으로 이동">
            <span class="logo-icon">📋</span>
            <span class="logo-text">Board</span>
          </button>
        </div>

        <div class="header-center">
          <div class="search-container">
            <select v-model="searchType" class="search-select">
              <option value="title">제목</option>
              <option value="content">내용</option>
              <option value="author">작성자</option>
            </select>
            <input
              v-model="searchQuery"
              type="text"
              class="search-input"
              placeholder="검색어를 입력하세요..."
              @keydown="handleSearchKeydown"
            />
            <button class="search-btn" @click="handleSearch">
              🔍 검색
            </button>
          </div>
        </div>

        <div class="header-right">
          <template v-if="isLoggedIn">
            <div class="user-info">
              <span class="user-name">{{ memberName || memberId }}</span>
              <button class="logout-btn" @click="handleLogout">
                로그아웃
              </button>
            </div>
          </template>
          <template v-else>
            <router-link to="/login" class="auth-link">
              로그인
            </router-link>
            <router-link to="/signup" class="auth-link auth-link-primary">
              회원가입
            </router-link>
          </template>
        </div>
      </div>
    </header>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
body {  background-color: #f5f5f5;  color: #000;  -webkit-color-scheme: light;  color-scheme: light;}html {  background-color: #f5f5f5;  color: #000;}
* {
  color: #000;
  background-color: transparent;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background-color: #343a40;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 1rem 1.5rem;
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 2rem;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo-btn {
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.25rem;
  font-weight: bold;
  color: white;
  transition: all 0.3s ease;
  padding: 0.5rem 1rem;
  border-radius: 6px;
}

.logo-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.logo-icon {
  font-size: 1.5rem;
}

.logo-text {
  font-size: 1.25rem;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.header-center {
  display: flex;
  justify-content: center;
}

.search-container {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  width: 100%;
  max-width: 500px;
}

.search-select {
  padding: 0.6rem 0.8rem;
  border: 1px solid #495057;
  border-radius: 6px 0 0 6px;
  background-color: white;
  color: #333;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.2s ease;
  border-right: none;
}

.search-select:hover {
  border-color: #007bff;
}

.search-select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.search-input {
  flex: 1;
  padding: 0.6rem 1rem;
  border: 1px solid #495057;
  background-color: white;
  color: #333;
  font-size: 0.95rem;
  transition: all 0.2s ease;
}

.search-input::placeholder {
  color: #999;
}

.search-input:hover {
  border-color: #007bff;
}

.search-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.search-btn {
  padding: 0.6rem 1.2rem;
  background-color: #007bff;
  color: white;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
  border: none;
  border-radius: 0 6px 6px 0;
}

.search-btn:hover {
  background-color: #0056b3;
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 123, 255, 0.3);
}

.search-btn:active {
  transform: translateY(0);
  box-shadow: 0 1px 3px rgba(0, 123, 255, 0.2);
}

.header-right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 1rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-name {
  color: white;
  font-weight: 600;
  font-size: 0.95rem;
}

.logout-btn {
  padding: 0.5rem 1rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.logout-btn:hover {
  background-color: #c82333;
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(220, 53, 69, 0.3);
}

.logout-btn:active {
  transform: translateY(0);
}

.auth-link {
  padding: 0.5rem 1rem;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-weight: 600;
  transition: all 0.2s ease;
  font-size: 0.9rem;
}

.auth-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-1px);
}

.auth-link-primary {
  background-color: #28a745;
}

.auth-link-primary:hover {
  background-color: #218838;
  box-shadow: 0 2px 6px rgba(40, 167, 69, 0.3);
}

.main-content {
  background-color: #f5f5f5;
  flex: 1;
  max-width: 1400px;
  width: 100%;
  margin: 0 auto;
  padding: 2rem 1.5rem;
}

@media (max-width: 1024px) {
  .header-container {
    grid-template-columns: 1fr;
    gap: 1rem;
    padding: 1rem;
  }

  .header-left {
    order: 1;
    justify-content: center;
  }

  .header-center {
    order: 2;
  }

  .search-container {
    flex-direction: column;
    max-width: 100%;
  }

  .search-select {
    border-radius: 6px;
    width: 100%;
    border: 1px solid #495057;
  }

  .search-input {
    width: 100%;
  }

  .search-btn {
    width: 100%;
    border-radius: 6px;
  }

  .header-right {
    order: 3;
    justify-content: center;
    width: 100%;
  }

  .user-info {
    flex-direction: column;
    width: 100%;
    gap: 0.5rem;
  }

  .auth-link {
    width: 100%;
    text-align: center;
  }
}

@media (max-width: 768px) {
  .logo-text {
    display: none;
  }

  .logo-btn {
    font-size: 1.5rem;
    padding: 0.5rem;
  }

  .logo-icon {
    font-size: 1.5rem;
  }

  .main-content {
  background-color: #f5f5f5;
    padding: 1rem;
  }
}
</style>
