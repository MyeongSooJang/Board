import client from './client'

export const authApi = {
  // 로그인
  login: (username, password) =>
    client.post('/auth/login', { username, password }),

  // 로그아웃
  logout: () =>
    client.post('/auth/logout'),

  // 토큰 갱신
  refresh: () =>
    client.post('/auth/refresh')
}
