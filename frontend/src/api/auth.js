import client from './client'

export const authApi = {
  // 로그인
  login: (memberId, memberPwd) =>
    client.post('/auth/login', { memberId, password: memberPwd }),

  // 로그아웃
  logout: () =>
    client.post('/auth/logout'),

  // 토큰 갱신
  refresh: () =>
    client.post('/auth/refresh')
}
