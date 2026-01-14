# 게시판 프론트엔드 실행 가이드

## 📋 프로젝트 구조

```
frontend/
├── src/
│   ├── api/                    # API 통신 모듈
│   │   ├── client.js          # Axios 인스턴스 (인터셉터 설정)
│   │   ├── auth.js            # 인증 API
│   │   ├── member.js          # 회원 API
│   │   ├── board.js           # 게시판 API
│   │   └── comment.js         # 댓글 API
│   ├── router/
│   │   └── index.js           # Vue Router 설정
│   ├── views/                 # 페이지 컴포넌트
│   │   ├── auth/
│   │   │   ├── Login.vue      # 로그인 페이지
│   │   │   └── Signup.vue     # 회원가입 페이지
│   │   └── board/
│   │       ├── BoardList.vue  # 게시판 목록
│   │       ├── BoardDetail.vue # 게시물 상세
│   │       └── BoardForm.vue  # 게시물 작성/수정
│   ├── App.vue                # 루트 컴포넌트 (네비게이션바)
│   └── main.js                # 애플리케이션 진입점
└── package.json
```

## 🚀 실행 방법

### 1. 백엔드 서버 실행
먼저 Spring Boot 백엔드 서버를 실행해야 합니다.
```bash
# IDE에서 CommunityApplication.java 실행
# 또는 콘솔에서
gradle bootRun
```

- 백엔드는 `http://localhost:8080`에서 실행됩니다
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### 2. 프론트엔드 개발 서버 실행
```bash
cd frontend
npm run dev
```

- 프론트엔드는 `http://localhost:5173`에서 실행됩니다
- 자동으로 브라우저가 열립니다

### 3. 프로덕션 빌드
```bash
cd frontend
npm run build
```

- `dist/` 디렉토리에 빌드 결과물이 생성됩니다

## 📱 주요 기능

### 인증 (Auth)
- **로그인** (`/login`): 아이디/비밀번호로 로그인
- **회원가입** (`/signup`): 새 계정 생성
- **로그아웃**: 네비게이션바의 로그아웃 버튼
- JWT 토큰 자동 관리 (localStorage 사용)

### 게시판 (Board)
- **목록 조회** (`/boards`): 모든 게시물 페이징 조회
- **검색**: 제목, 작성자, 키워드(제목+내용)로 검색 가능
- **상세 조회** (`/boards/:boardNo`): 게시물 상세 및 댓글
- **작성** (`/boards/create`): 새 게시물 작성 (로그인 필수)
- **수정** (`/boards/:boardNo/edit`): 본인 게시물만 수정 가능
- **삭제**: 본인 게시물만 삭제 가능

### 댓글 (Comment)
- **조회**: 게시물 상세 페이지에서 댓글 조회
- **작성**: 로그인한 사용자만 작성 가능
- **삭제**: 본인 댓글만 삭제 가능

## 🔐 보안 기능

- JWT 토큰 기반 인증
- AccessToken (1시간) + RefreshToken (7일)
- 토큰 자동 헤더 추가 (Authorization: Bearer <token>)
- 401 에러 시 자동 로그인 페이지 이동
- 로그인 필요한 페이지 자동 리디렉션

## 🛠️ API 통신

모든 API는 `src/api/` 모듈을 통해 관리됩니다.

### 예시: 게시물 목록 조회
```javascript
import { boardApi } from '@/api/board'

// 페이징 조회
const response = await boardApi.getList(page, size)

// 제목으로 검색
const response = await boardApi.searchByTitle(title, page, size)
```

### API 클라이언트 설정 (`src/api/client.js`)
- 기본 URL: `http://localhost:8080`
- 모든 요청에 JWT 토큰 자동 추가
- 401 에러 자동 처리

## 📝 주요 데이터 구조

### Member (회원)
```javascript
{
  memberNo: 1,
  memberId: "user1",
  memberName: "김철수",
  memberAge: 25,
  memberEmail: "user@example.com",
  memberPhone: "010-1234-5678",
  createTime: "2024-01-13T15:30:00",
  updateTime: "2024-01-13T15:30:00"
}
```

### Board (게시물)
```javascript
{
  boardNo: 1,
  boardTitle: "첫 번째 게시물",
  boardContent: "내용입니다",
  member: { memberNo, memberId, memberName },
  createTime: "2024-01-13T15:30:00",
  updateTime: "2024-01-13T15:30:00"
}
```

### Comment (댓글)
```javascript
{
  commentNo: 1,
  commentContent: "좋은 글입니다",
  commentParentNo: null, // 대댓글이면 부모 댓글 번호
  board: { boardNo, ... },
  member: { memberNo, memberId, memberName },
  createTime: "2024-01-13T15:30:00"
}
```

## 🎨 스타일링

- 순수 CSS (외부 라이브러리 없음)
- 반응형 디자인 (모바일 대응)
- 색상 팔레트:
  - 주색: #42b883 (초록색)
  - 배경: #2c3e50 (진회색)
  - 텍스트: #34495e (검은색)

## 🐛 자주 발생하는 문제

### 1. CORS 오류
**증상**: "Access to XMLHttpRequest blocked by CORS policy"
**해결**:
- 백엔드가 `http://localhost:8080`에서 실행 중인지 확인
- SecurityConfig의 CORS 설정 확인

### 2. 401 Unauthorized
**증상**: "로그인 페이지로 자동 이동"
**해결**:
- 토큰이 만료되었을 수 있음
- 다시 로그인하세요

### 3. 포트 이미 사용 중
**증상**: "EADDRINUSE: address already in use"
**해결**:
```bash
# 포트 5173 사용 중이면, 다른 포트 사용
npm run dev -- --port 3000
```

## 📦 의존성

```json
{
  "vue": "^3.x",
  "vue-router": "^4.x",
  "axios": "^1.x"
}
```

## 💡 개발 팁

### 1. API 응답 구조 확인
```
http://localhost:8080/swagger-ui/index.html
```
Swagger UI에서 모든 API의 요청/응답 형식을 확인할 수 있습니다.

### 2. 로컬 스토리지 확인
브라우저 개발자 도구 → Application → Local Storage
- `accessToken`: JWT 액세스 토큰
- `refreshToken`: JWT 리프레시 토큰
- `memberId`: 현재 로그인한 사용자 ID

### 3. 네트워크 트래픽 확인
브라우저 개발자 도구 → Network 탭에서 모든 API 요청 확인

## 📚 추가 학습 자료

- [Vue.js 공식 문서](https://vuejs.org/)
- [Vue Router 가이드](https://router.vuejs.org/)
- [Axios 문서](https://axios-http.com/)
- [JWT 이해하기](https://jwt.io/)

## 🤝 문제 해결

문제가 발생하면:
1. 백엔드 서버가 실행 중인지 확인
2. 콘솔 에러 메시지 확인 (F12 → Console)
3. 네트워크 탭에서 API 요청 확인
4. Swagger UI에서 API 직접 테스트

---

**프론트엔드 개발 서버 실행**:
```bash
cd frontend && npm run dev
```

**백엔드 서버 확인**:
```
http://localhost:8080/swagger-ui/index.html
```
