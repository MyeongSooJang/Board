import client from './client'

export const memberApi = {
  // 회원 가입
  signup: (username, name, password, age, email, phone, role = 'GUEST') =>
    client.post('/member', {
      username,
      name,
      password,
      age,
      email,
      phone,
      role
    }),

  // 회원 정보 조회
  getByUsername: (username) =>
    client.get(`/member/${username}`),

  // 회원 정보 수정
  updateByUsername: (username, data) =>
    client.put(`/member/${username}`, data),

  // 회원 삭제
  deleteByUsername: (username) =>
    client.delete(`/member/${username}`)
}
