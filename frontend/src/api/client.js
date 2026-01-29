import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

const client = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// refresh 요청용 별도 인스턴스 (인터셉터 없음)
const refreshClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 로그아웃 중임을 나타내는 flag
let isLoggingOut = false

// 요청 인터셉터 - JWT 토큰 자동 추가
client.interceptors.request.use(
  config => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 응답 인터셉터 - 에러 처리
client.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      const refreshToken = localStorage.getItem('refreshToken')

      // 이미 로그아웃 중이면 더 이상 처리하지 않음
      if (isLoggingOut) {
        return Promise.reject(error)
      }

      // refreshToken이 있으면 새 accessToken 발급 시도
      if (refreshToken && !error.config._retry) {
        error.config._retry = true

        return refreshClient.post('/auth/refresh', { refreshToken })
          .then(res => {
            // 새 토큰 저장
            localStorage.setItem('accessToken', res.data.accessToken)
            localStorage.setItem('refreshToken', res.data.refreshToken)

            // 원래 요청의 Authorization 헤더 업데이트
            error.config.headers.Authorization = `Bearer ${res.data.accessToken}`

            // 원래 요청 재시도
            return client(error.config)
          })
          .catch(() => {
            // refreshToken도 만료되었을 때
            if (!isLoggingOut) {
              isLoggingOut = true
              localStorage.removeItem('accessToken')
              localStorage.removeItem('refreshToken')
              localStorage.removeItem('memberId')
              localStorage.removeItem('memberName')
              alert('로그인이 만료되었습니다. 다시 로그인해주세요.')
              window.location.href = '/login'
            }
            return Promise.reject(error)
          })
      } else {
        // refreshToken이 없거나 이미 재시도했을 때
        if (!isLoggingOut) {
          isLoggingOut = true
          localStorage.removeItem('accessToken')
          localStorage.removeItem('refreshToken')
          localStorage.removeItem('memberId')
          localStorage.removeItem('memberName')
          alert('로그인이 만료되었습니다. 다시 로그인해주세요.')
          window.location.href = '/login'
        }
      }
    }
    return Promise.reject(error)
  }
)

export default client
