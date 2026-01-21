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

  // 회원 정보 조회 (ID로)
  getByUsername: (username) =>
    client.get(`/member/by-username/${username}`),

  // 회원 정보 조회 (PK로)
  getById: (memberId) =>
    client.get(`/member/${memberId}`),

  // 회원 정보 수정
  update: (memberId, data) =>
    client.put(`/member/${memberId}`, data),

  // 회원 삭제
  delete: (memberId) =>
    client.delete(`/member/${memberId}`)
}
