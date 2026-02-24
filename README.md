# 🎯 커뮤니티 게시판 (Community Board)

> **취업 포트폴리오용 풀스택 커뮤니티 게시판** (디시인사이드 스타일)
>
> Spring Boot + Vue.js 3를 활용한 현대적인 웹 애플리케이션

---

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [설치 및 실행](#설치-및-실행)
- [API 문서](#api-문서)
- [주요 구현 내용](#주요-구현-내용)
- [학습 포인트](#학습-포인트)
- [향후 계획](#향후-계획)

---

## 프로젝트 개요

### 목적
신입 백엔드 개발자(Spring Boot) 취직 포트폴리오용 프로젝트로, **단순 CRUD를 넘어 실무 기술 스택을 직접 구현**하고 이력서에 기술할 수 있는 수준으로 완성합니다.

### 핵심 전략
> "단순 CRUD 개발자는 더 이상 경쟁력이 없다. 트래픽을 버티는 구조, 장애 대응 전략, 확장 가능한 아키텍처를 이해하는 사람을 선호한다."

2025년 신입 채용 공고 분석 기반으로 **자격요건 필수 스택 + 우대사항**을 모두 구현합니다.

---

## 기술 스택

### 백엔드
| 항목 | 기술 | 버전 |
|------|------|------|
| **언어** | Java | 21 |
| **프레임워크** | Spring Boot | 4.0.1 |
| **데이터베이스** | MySQL | 8.0 |
| **ORM** | JPA (Hibernate) | - |
| **인증** | JWT | jjwt 0.12.3 |
| **API 문서** | Swagger/OpenAPI | springdoc 3.0.1 |
| **클라우드** | AWS S3 | SDK v2 2.20.0 |

### 프론트엔드
| 항목 | 기술 | 버전 |
|------|------|------|
| **프레임워크** | Vue.js | 3.5.24 |
| **라우팅** | Vue Router | 4.6.4 |
| **HTTP 클라이언트** | Axios | 1.13.2 |
| **빌드 도구** | Vite | 7.2.4 |
| **에디터** | Toast UI Editor | 3.2.2 |
| **XSS 방어** | DOMPurify | - |

### DevOps (예정)
- **CI/CD**: GitHub Actions
- **컨테이너**: Docker
- **배포**: AWS EC2 + RDS
- **캐싱**: Redis
- **CDN**: CloudFront

---

## 주요 기능

### ✅ Phase 0: 기본 기능 (완료)
- [x] 회원가입 & 로그인 (JWT 기반)
- [x] 게시글 CRUD (Create, Read, Update, Delete)
- [x] 게시글 검색 & 정렬 (최신순, 조회순, 좋아요순)
- [x] 댓글 & 대댓글 시스템
- [x] 게시글 좋아요/싫어요 (중복 방지)
- [x] 신고 시스템 & 관리자 중재
- [x] 북마크 기능
- [x] 소프트 삭제 (논리적 삭제)
- [x] API 문서 (Swagger)

### 🚀 Phase 1: S3 이미지 업로드 (완료)
- [x] **AWS S3 Presigned URL 방식** 이미지 업로드
  - 서버를 거치지 않고 클라이언트가 S3로 직접 업로드
  - 서버 메모리 사용 0% 달성
- [x] **Toast UI Editor** 통합
  - 게시글 본문에 이미지 직접 삽입
  - WYSIWYG 편집 환경
- [x] **XSS 방어** (DOMPurify)
  - HTML 렌더링 시 악성 스크립트 필터링
- [x] **S3 URL 화이트리스트 검증**
  - 게시글 저장 시 이미지 URL이 우리 S3 버킷 URL인지 확인

### 📅 Phase 2: 테스트 코드 (예정)
- [ ] JUnit5 단위 테스트
- [ ] 통합 테스트 (@SpringBootTest)
- [ ] Repository 테스트

### 📊 Phase 3: QueryDSL 도입 (예정)
- [ ] 동적 쿼리 구현
- [ ] 복잡한 검색/정렬 조건 타입 세이프하게 처리
- [ ] EXPLAIN 분석 & 인덱스 최적화

### ⚡ Phase 4: Redis 캐싱 (예정)
- [ ] 조회수/좋아요 캐싱 (Write-Back 전략)
- [ ] 인기 게시글 목록 캐싱
- [ ] Race Condition 해결

### 🌐 Phase 5: AWS 배포 (예정)
- [ ] EC2 + RDS + S3 인프라 구성
- [ ] CloudFront CDN 연결
- [ ] Route 53 도메인 설정

### 🔄 Phase 6: CI/CD 파이프라인 (예정)
- [ ] GitHub Actions 워크플로우
- [ ] Docker 컨테이너화
- [ ] 자동 테스트 & 배포

---

## 프로젝트 구조

### 백엔드 디렉토리 구조
```
src/main/java/com/project/community/
├── auth/                      # 인증/인가
│   ├── controller/
│   └── ...
├── board/                     # 게시판
│   ├── entity/
│   ├── dto/
│   ├── service/
│   ├── controller/
│   └── repository/
├── comment/                   # 댓글
│   └── ...
├── member/                    # 회원
│   └── ...
├── boardlike/                 # 게시글 좋아요/싫어요
│   └── ...
├── commentlike/               # 댓글 좋아요
│   └── ...
├── boardfile/                 # 게시글 파일/이미지
│   └── ...
├── image/                     # S3 이미지 업로드 (Phase 1)
│   ├── controller/ImageController.java
│   ├── service/S3PresignedUrlService.java
│   └── dto/
├── report/                    # 신고 시스템
│   └── ...
├── bookmark/                  # 북마크
│   └── ...
├── config/                    # 설정
│   ├── SecurityConfig.java
│   ├── S3Config.java         # Phase 1
│   └── ...
├── exception/                 # 예외 처리
│   └── ...
└── security/                  # JWT 필터
    └── JwtAuthenticationFilter.java
```

### 프론트엔드 디렉토리 구조
```
frontend/src/
├── api/                       # API 클라이언트
│   ├── board.js
│   ├── auth.js
│   ├── comment.js
│   ├── image.js              # Phase 1: S3 이미지 API
│   └── ...
├── components/                # 재사용 컴포넌트
│   ├── BookmarkButton.vue
│   ├── CommentNode.vue
│   ├── ReportModal.vue
│   └── ...
├── views/                     # 페이지 컴포넌트
│   ├── auth/
│   │   ├── Login.vue
│   │   └── Signup.vue
│   ├── board/
│   │   ├── BoardList.vue
│   │   ├── BoardDetail.vue    # Phase 1: HTML 렌더링
│   │   └── BoardForm.vue      # Phase 1: Toast UI Editor
│   ├── admin/
│   └── ...
├── router/                    # 라우팅
│   └── index.js
└── App.vue
```

---

## 설치 및 실행

### 사전 요구사항
- **Java 21** 이상
- **Node.js 16** 이상, **npm 8** 이상
- **MySQL 8.0** 이상
- **Git**

### 1️⃣ 프로젝트 클론

```bash
git clone https://github.com/your-username/Board.git
cd Board
```

### 2️⃣ 백엔드 설정

#### 환경변수 설정 (.env 파일)
```bash
# 프로젝트 루트에 .env 파일 생성
cat > .env << EOF
# Database
DB_USERNAME=root
DB_PASSWORD=your-password

# JWT
JWT_SECRET=your-very-long-random-jwt-secret-key-min-256-bits

# AWS S3 (Phase 1)
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_S3_BUCKET_NAME=your-bucket-name
AWS_REGION=ap-northeast-2
AWS_S3_PRESIGNED_URL_EXPIRATION=900
EOF
```

#### 데이터베이스 생성
```bash
mysql -u root -p
> CREATE DATABASE board_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
> EXIT;
```

#### 백엔드 실행
```bash
./gradlew bootRun
# 또는 IDE에서 CommunityApplication 실행
```

- 포트: `http://localhost:8080`
- Swagger API 문서: `http://localhost:8080/swagger-ui/index.html`

### 3️⃣ 프론트엔드 설정

```bash
cd frontend

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev
```

- 포트: `http://localhost:5173`

### 4️⃣ 브라우저에서 접속

```
http://localhost:5173
```

---

## API 문서

### Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

### 주요 엔드포인트

#### 인증 (Auth)
| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/auth/login` | 로그인 |
| POST | `/auth/refresh` | 토큰 갱신 |
| POST | `/auth/logout` | 로그아웃 |

#### 게시판 (Board)
| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/boards` | 게시글 목록 |
| GET | `/boards/{boardId}` | 게시글 상세 |
| POST | `/boards` | 게시글 작성 |
| PUT | `/boards/{boardId}` | 게시글 수정 |
| DELETE | `/boards/{boardId}` | 게시글 삭제 |
| POST | `/boards/{boardId}/like` | 좋아요 토글 |

#### 댓글 (Comment)
| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/boards/{boardId}/comments` | 댓글 목록 |
| POST | `/boards/{boardId}/comments` | 댓글 작성 |
| PUT | `/boards/{boardId}/comments/{commentId}` | 댓글 수정 |
| DELETE | `/boards/{boardId}/comments/{commentId}` | 댓글 삭제 |

#### 이미지 (Image - Phase 1)
| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/api/images/presigned-url` | Presigned URL 발급 |

---

## 주요 구현 내용

### 1. JWT 기반 인증/인가 체계 🔐

**왜 JWT를 선택했나?**
- Stateless: 서버에 세션 저장 필요 없음 → 메모리 효율
- 프론트/백 독립적 배포 가능
- 마이크로서비스 환경에 적합

**구현 방식**
```
Access Token (1시간) + Refresh Token (7일)
├─ Access Token: 단기 인증
├─ Refresh Token: DB에 저장 (강제 만료 가능)
└─ 악의적 접근 시 토큰 블랙리스트 처리
```

**관련 파일**
- `JwtAuthenticationFilter.java`: JWT 검증 필터
- `JwtTokenProvider.java`: JWT 생성/검증 로직
- `SecurityConfig.java`: Spring Security 설정

---

### 2. S3 Presigned URL 기반 이미지 업로드 (Phase 1) 🖼️

**기존 방식의 문제점**
```
프론트 → 백엔드(메모리 사용) → S3 저장
❌ 서버 메모리 부하
❌ 대역폭 낭비
```

**개선된 방식 (Presigned URL)**
```
프론트 → [Presigned URL 요청] → 백엔드
       → [S3로 직접 업로드] → S3 저장
✅ 서버 메모리 사용 0%
✅ 업로드 속도 향상
✅ AWS가 자동 관리
```

**구현 내용**
- `S3Config.java`: AWS SDK 설정 (S3Client, S3Presigner Bean)
- `S3PresignedUrlService.java`: Presigned URL 생성 로직
- `ImageController.java`: `/api/images/presigned-url` API
- `BoardForm.vue`: Toast UI Editor 통합 & 이미지 훅
- `BoardDetail.vue`: DOMPurify로 HTML 안전 렌더링

**보안 조치**
- ✅ Presigned URL 15분 만료
- ✅ S3 URL 화이트리스트 검증 (게시글 저장 시)
- ✅ DOMPurify로 XSS 공격 방어
- ✅ AWS Access Key는 백엔드에만 보유

---

### 3. 소프트 삭제 (Soft Delete) 전략 🗑️

**물리 삭제 vs 논리 삭제**
```
물리 삭제: DELETE → 복구 불가, 빠름
논리 삭제: UPDATE deleteTime → 복구 가능, 느림
```

**우리의 선택: 논리 삭제**
- 실수로 삭제된 글 복구 가능
- 신고 처리 흐름 명확 (사용자 삭제 vs 관리자 삭제)
- 모든 쿼리에 `WHERE deleteTime IS NULL` 필터 적용

**관련 파일**
- `BaseEntity.java`: `deleteTime` 필드 정의
- 모든 Repository: WHERE 절에 deleteTime 필터 추가

---

### 4. 동시성 제어 (Race Condition 방지) ⚡

**문제 상황**
```
사용자 A: 좋아요 클릭 (count: 5 → 6)
사용자 B: 좋아요 클릭 (count: 5 → 6)
결과: count = 6 (5 → 7이 되어야 함) ❌
```

**현재 해결책: 낙관적 업데이트 + 디바운싱**
```javascript
// UI 즉시 반영 (낙관적)
liked.value = !liked.value
likeCount.value += 1

// 500ms 후 서버 동기화
setTimeout(() => toggleLikeDebounced(), 500)

// 실패 시 롤백
catch (err) {
  liked.value = wasLiked
  likeCount.value = previousCount
}
```

**향후 개선: Redis INCR (Phase 4)**
```
Redis INCR은 원자적 연산 → Race Condition 원천 차단
```

---

### 5. 에러 처리 및 커스텀 예외 🚨

**전략**
- RuntimeException 사용 금지
- 프로젝트 전용 예외 클래스 정의
- 전역 예외 핸들러로 일관된 응답

**예외 클래스**
```
NotFoundException           : 리소스 없음
DuplicateException        : 중복 요청
UnauthorizedException     : 인증 실패
ValidationException       : 검증 실패
TokenException           : JWT 토큰 오류
```

**응답 형식**
```json
{
  "errorCode": "MEMBER_NOT_FOUND",
  "message": "사용자를 찾을 수 없습니다",
  "timestamp": "2026-02-24T13:30:00Z"
}
```

---

## 학습 포인트

### 백엔드
- ✅ Spring Security + JWT 실전 구현
- ✅ AWS SDK를 활용한 클라우드 통합
- ✅ N+1 쿼리 문제 해결 (Fetch Join)
- ✅ 소프트 삭제를 통한 데이터 무결성 관리
- ✅ 동시성 제어 전략 (낙관적 업데이트)
- ✅ 커스텀 예외 처리 및 전역 에러 핸들링

### 프론트엔드
- ✅ Vue 3 Composition API 활용
- ✅ 에디터 라이브러리 통합 (Toast UI)
- ✅ XSS 방어 (DOMPurify)
- ✅ HTTP 클라이언트 설정 (Axios 인터셉터)
- ✅ Vue Router를 활용한 SPA 라우팅

### 클라우드/DevOps
- ✅ AWS S3 Presigned URL 개념 이해
- ✅ IAM 정책 및 S3 버킷 정책 설정
- ✅ CORS 정책 이해 및 설정

---

## 향후 계획

### Phase 2: 테스트 코드 (1주)
```
JUnit5 단위/통합 테스트로 핵심 비즈니스 로직 검증
```

### Phase 3: QueryDSL 도입 (1주)
```
동적 쿼리 구현 + EXPLAIN 분석 + 인덱스 최적화
```

### Phase 4: Redis 캐싱 (1주)
```
조회수/좋아요 캐싱으로 DB 부하 감소
```

### Phase 5: AWS 배포 (1주)
```
EC2 + RDS + S3 + CloudFront 구성
```

### Phase 6: CI/CD 파이프라인 (1주)
```
GitHub Actions + Docker 기반 자동 배포
```

---

## 면접 대비 핵심 답변

### Q1: S3 Presigned URL 방식을 왜 선택했나요?

> A: 서버가 이미지 데이터를 처리하지 않으므로 메모리 사용과 네트워크 비용을 줄일 수 있습니다. 또한 AWS Access Key를 프론트엔드에 노출하지 않으면서도 안전하게 업로드할 수 있습니다.

### Q2: 동시성 문제는 어떻게 해결했나요?

> A: 현재는 낙관적 업데이트와 디바운싱으로 UI 반응성을 높이고, 실패 시 롤백합니다. 향후 Redis INCR 명령어를 사용하면 원자적 연산으로 Race Condition을 완벽히 차단할 수 있습니다.

### Q3: XSS 공격은 어떻게 방어했나요?

> A: DOMPurify로 HTML을 렌더링하기 전에 필터링하고, 백엔드에서는 이미지 URL이 우리 S3 버킷인지 검증합니다. 이중 방어로 악의적인 스크립트 삽입을 원천 차단합니다.

---

## 라이센스

MIT License

---

## 연락처

- **GitHub**: [your-github-profile](https://github.com/your-username)
- **이메일**: your-email@example.com
- **포트폴리오**: [your-portfolio](https://yourportfolio.com)

---

**Last Updated**: 2026-02-24
**Phase**: 1 (S3 이미지 업로드 완료)
