<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const isLoggedIn = ref(!!localStorage.getItem('accessToken'))
const memberId = ref(localStorage.getItem('memberId') || '')

const handleLogout = () => {
  localStorage.removeItem('accessToken')
  localStorage.removeItem('refreshToken')
  localStorage.removeItem('memberId')
  isLoggedIn.value = false
  router.push('/login')
}

// 로그인 상태 감시
window.addEventListener('storage', () => {
  isLoggedIn.value = !!localStorage.getItem('accessToken')
  memberId.value = localStorage.getItem('memberId') || ''
})
</script>

<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-container">
        <router-link to="/boards" class="nav-logo">
          📋 게시판
        </router-link>
        <div class="nav-menu">
          <router-link to="/boards" class="nav-item">목록</router-link>
          <template v-if="isLoggedIn">
            <router-link to="/boards/create" class="nav-item">작성하기</router-link>
            <span class="nav-user">{{ memberId }}</span>
            <button @click="handleLogout" class="nav-logout">로그아웃</button>
          </template>
          <template v-else>
            <router-link to="/login" class="nav-item">로그인</router-link>
            <router-link to="/signup" class="nav-item">회원가입</router-link>
          </template>
        </div>
      </div>
    </nav>

    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f5f5f5;
}

#app {
  min-height: 100vh;
}

.navbar {
  background-color: #2c3e50;
  color: white;
  padding: 1rem 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.nav-menu {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-item {
  color: white;
  text-decoration: none;
  transition: color 0.3s;
}

.nav-item:hover {
  color: #42b883;
}

.nav-user {
  color: #42b883;
  font-weight: bold;
}

.nav-logout {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.nav-logout:hover {
  background-color: #c0392b;
}

.main-content {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 0 1rem;
  min-height: calc(100vh - 100px);
}
</style>
