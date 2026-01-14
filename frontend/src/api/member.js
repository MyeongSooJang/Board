import client from './client'

export const memberApi = {
  // 회원 가입
  signup: (memberId, memberName, memberPwd, memberAge, memberEmail, memberPhone, memberRole = 'GUEST') =>
    client.post('/member', {
      memberId,
      memberName,
      memberPwd,
      memberAge,
      memberEmail,
      memberPhone,
      memberRole
    }),

  // 회원 정보 조회 (ID로)
  getById: (memberId) =>
    client.get(`/member/id/${memberId}`),

  // 회원 정보 조회 (번호로)
  getByNo: (memberNo) =>
    client.get(`/member/${memberNo}`),

  // 회원 정보 수정
  update: (memberNo, data) =>
    client.put(`/member/${memberNo}`, data),

  // 회원 삭제
  delete: (memberNo) =>
    client.delete(`/member/${memberNo}`)
}
