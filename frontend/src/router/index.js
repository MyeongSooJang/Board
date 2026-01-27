import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/auth/Login.vue'
import Signup from '../views/auth/Signup.vue'
import BoardList from '../views/board/BoardList.vue'
import BoardDetail from '../views/board/BoardDetail.vue'
import BoardForm from '../views/board/BoardForm.vue'
import ReportList from '../views/admin/ReportList.vue'

const routes = [
  {
    path: '/',
    redirect: '/boards'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup,
    meta: { requiresAuth: false }
  },
  {
    path: '/boards',
    name: 'BoardList',
    component: BoardList,
    meta: { requiresAuth: false }
  },
  {
    path: '/boards/create',
    name: 'BoardCreate',
    component: BoardForm,
    meta: { requiresAuth: true }
  },
  {
    path: '/boards/:boardId/edit',
    name: 'BoardEdit',
    component: BoardForm,
    meta: { requiresAuth: true }
  },
  {
    path: '/boards/:boardId',
    name: 'BoardDetail',
    component: BoardDetail,
    meta: { requiresAuth: false }
  },
  {
    path: '/admin/reports',
    name: 'ReportList',
    component: ReportList,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 라우터 가드 - 인증 확인
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken')
  const requiresAuth = to.meta.requiresAuth

  if (requiresAuth && !token) {
    // 인증이 필요한 페이지인데 토큰이 없으면 로그인 페이지로
    next('/login')
  } else if ((to.path === '/login' || to.path === '/signup') && token) {
    // 로그인한 상태에서 로그인/회원가입 페이지 접근하면 메인 페이지로
    next('/boards')
  } else {
    next()
  }
})

export default router
