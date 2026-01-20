# JWT + Spring Security 구현 계획

## 📋 구현 요구사항
- 로그인: memberId 또는 이메일로 가능
- 토큰: Access Token + Refresh Token
- 회원가입 기본 역할: GUEST
- 공개 API: 회원가입/로그인, 게시글 조회, 댓글 조회, Swagger UI

---

## 🗂️ 생성할 파일 목록

### 1. JWT 관련 (security 패키지)
- `JwtTokenProvider.java` - JWT 토큰 생성 및 검증
- `JwtAuthenticationFilter.java` - 요청마다 토큰 검증하는 필터
- `CustomUserDetailsService.java` - 사용자 정보 조회

### 2. 인증 관련 DTO (member/dto 패키지)
- `LoginRequestDTO.java` - 로그인 요청 (아이디/이메일, 비밀번호)
- `LoginResponseDTO.java` - 로그인 응답 (토큰 정보)
- `TokenRefreshRequestDTO.java` - 토큰 갱신 요청
- `TokenRefreshResponseDTO.java` - 토큰 갱신 응답

### 3. 엔티티 추가
- `RefreshToken.java` - Refresh Token 저장용 엔티티

### 4. Repository 추가
- `RefreshTokenRepository.java`

### 5. Service 확장
- `AuthService.java` - 로그인, 토큰 갱신 로직

### 6. Controller 추가
- `AuthController.java` - 인증 엔드포인트

### 7. 수정할 파일
- `SecurityConfig.java` - 보안 설정 수정
- `MemberService.java` - 비밀번호 암호화 추가

---

## 📝 단계별 구현 가이드

### Step 1: JwtTokenProvider 클래스 생성

**위치**: `src/main/java/com/project/community/security/JwtTokenProvider.java`

**필요한 필드**:
```
- secret: application.yml에서 주입받을 비밀키
- accessTokenExpiration: Access Token 만료 시간 (예: 1시간)
- refreshTokenExpiration: Refresh Token 만료 시간 (예: 7일)
```

**필요한 메서드**:
1. `generateAccessToken(String memberId, String role)`: Access Token 생성
2. `generateRefreshToken(String memberId)`: Refresh Token 생성
3. `validateToken(String token)`: 토큰 유효성 검증
4. `getMemberIdFromToken(String token)`: 토큰에서 memberId 추출
5. `getRoleFromToken(String token)`: 토큰에서 role 추출

**주요 로직**:
- JWT 라이브러리(jjwt) 사용
- Claims에 memberId, role 저장
- HS256 알고리즘 사용
- 만료 시간 설정

**의존성**: `@Value`로 application.yml에서 값 주입

---

### Step 2: RefreshToken 엔티티 생성

**위치**: `src/main/java/com/project/community/domain/member/entity/RefreshToken.java`

**필요한 필드**:
```
- id (Long): Primary Key
- memberId (String): 회원 아이디
- token (String): Refresh Token 값
- expiryDate (LocalDateTime): 만료 시간
```

**주의사항**:
- BaseEntity를 상속하지 않아도 됨 (간단한 토큰 저장용)
- `@Entity`, `@Table` 사용

---

### Step 3: RefreshTokenRepository 생성

**위치**: `src/main/java/com/project/community/domain/member/repository/RefreshTokenRepository.java`

**필요한 메서드**:
```
- findByMemberId(String memberId): 회원의 Refresh Token 조회
- findByToken(String token): 토큰으로 조회
- deleteByMemberId(String memberId): 로그아웃 시 토큰 삭제
```

---

### Step 4: CustomUserDetailsService 구현

**위치**: `src/main/java/com/project/community/security/CustomUserDetailsService.java`

**구현 내용**:
- `UserDetailsService` 인터페이스 구현
- `loadUserByUsername(String username)` 메서드 오버라이드
    - username은 memberId 또는 이메일일 수 있음
    - MemberRepository에서 `findByMemberId` 또는 `findByMemberEmail` 시도
    - 찾으면 Spring Security의 User 객체로 변환하여 반환
    - 없으면 `UsernameNotFoundException` 발생

**의존성**:
- MemberRepository 주입 필요
- `@Service` 어노테이션

---

### Step 5: JwtAuthenticationFilter 생성

**위치**: `src/main/java/com/project/community/security/JwtAuthenticationFilter.java`

**구현 내용**:
- `OncePerRequestFilter` 상속
- `doFilterInternal` 메서드 오버라이드

**필터 로직**:
1. Request의 Authorization 헤더에서 토큰 추출 ("Bearer " 제거)
2. 토큰이 있으면 JwtTokenProvider로 검증
3. 유효하면 memberId, role 추출
4. Spring Security의 Authentication 객체 생성
5. SecurityContext에 Authentication 저장
6. 다음 필터로 진행

**의존성**:
- JwtTokenProvider 주입

---

### Step 6: 로그인 DTO 생성

**LoginRequestDTO** (`src/main/java/com/project/community/domain/member/dto/LoginRequestDTO.java`):
```
- username (String): memberId 또는 이메일
- password (String): 비밀번호
```
- `@NotBlank` 검증 추가

**LoginResponseDTO** (`src/main/java/com/project/community/domain/member/dto/LoginResponseDTO.java`):
```
- accessToken (String)
- refreshToken (String)
- tokenType (String): "Bearer"
- expiresIn (Long): Access Token 만료 시간(초)
```

**TokenRefreshRequestDTO**:
```
- refreshToken (String)
```

**TokenRefreshResponseDTO**:
```
- accessToken (String)
- refreshToken (String)
- tokenType (String)
- expiresIn (Long)
```

---

### Step 7: AuthService 생성

**위치**: `src/main/java/com/project/community/domain/member/service/AuthService.java`

**필요한 메서드**:

1. **login(LoginRequestDTO request)**:
    - username으로 Member 조회 (memberId 또는 이메일)
    - 비밀번호 검증 (PasswordEncoder.matches() 사용)
    - 일치하면 Access Token, Refresh Token 생성
    - Refresh Token을 DB에 저장
    - LoginResponseDTO 반환

2. **refreshToken(TokenRefreshRequestDTO request)**:
    - Refresh Token 유효성 검증
    - DB에 저장된 토큰과 일치하는지 확인
    - 새로운 Access Token 생성 (선택적으로 Refresh Token도 갱신)
    - TokenRefreshResponseDTO 반환

3. **logout(String memberId)**:
    - DB에서 해당 회원의 Refresh Token 삭제

**의존성**:
- MemberRepository
- RefreshTokenRepository
- JwtTokenProvider
- PasswordEncoder

---

### Step 8: AuthController 생성

**위치**: `src/main/java/com/project/community/domain/member/controller/AuthController.java`

**엔드포인트**:
1. `POST /auth/login` - 로그인
2. `POST /auth/refresh` - 토큰 갱신
3. `POST /auth/logout` - 로그아웃

**참고**:
- `@Valid` 사용하여 DTO 검증
- ResponseEntity로 적절한 상태 코드 반환

---

### Step 9: MemberService 수정

**enrollMember 메서드 수정**:
- 회원가입 시 비밀번호를 PasswordEncoder로 암호화
- memberRole 기본값을 "GUEST"로 설정

**수정 전**:
```java
return MemberResponseDTO.from(memberRepository.save(createMember(request)));
```

**수정 후**:
```java
Member member = createMember(request);
member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));
if (member.getMemberRole() == null || member.getMemberRole().isEmpty()) {
    member.setMemberRole("GUEST");
}
return MemberResponseDTO.from(memberRepository.save(member));
```

**updateMember 메서드 수정**:
- 비밀번호 변경 시 암호화 필요
- Member 엔티티의 updateMember도 수정 필요

**의존성 추가**:
- PasswordEncoder 주입

---

### Step 10: Member Entity에 setter 추가 또는 생성자 수정

**문제점**: 현재 Member는 Lombok의 @Getter만 있고 setter가 없음

**해결방법**:
1. 비밀번호 암호화를 위한 메서드 추가
2. 또는 createMember에서 암호화된 비밀번호를 받도록 수정

**권장 방법**:
- Member 엔티티에 `encodeMemberPwd(String encodedPwd)` 같은 메서드 추가
- 또는 MemberService에서 DTO를 직접 수정 후 Entity 생성

---

### Step 11: SecurityConfig 수정

**위치**: `src/main/java/com/project/community/config/SecurityConfig.java`

**현재 문제**: 모든 경로가 permitAll

**수정 내용**:

1. **JwtAuthenticationFilter 등록**:
    - UsernamePasswordAuthenticationFilter 앞에 추가

2. **경로별 권한 설정**:
   ```
   공개 (permitAll):
   - POST /member (회원가입)
   - POST /auth/login
   - POST /auth/refresh
   - GET /board/**
   - GET /comment/**
   - /swagger-ui/**, /v3/api-docs/**, /api-docs/**

   인증 필요 (authenticated):
   - POST /board/** (게시글 작성)
   - PUT /board/** (게시글 수정)
   - DELETE /board/** (게시글 삭제)
   - POST /comment/** (댓글 작성)
   - PUT /comment/** (댓글 수정)
   - DELETE /comment/** (댓글 삭제)
   - PUT /member/** (회원정보 수정)
   - DELETE /member/** (회원 탈퇴)

   나머지: authenticated
   ```

3. **CORS 설정** (필요시):
    - 프론트엔드가 별도 포트에서 실행되면 CORS 설정 추가

4. **세션 관리**:
    - STATELESS로 설정 (JWT 사용하므로 세션 불필요)

5. **의존성 주입**:
    - JwtAuthenticationFilter를 Bean으로 등록하고 주입

---

### Step 12: MemberRepository에 이메일 조회 메서드 확인

**현재**: `findByMemberId(String memberId)` 만 있음

**추가 필요**: 이메일로 로그인하려면
- `findByMemberEmail(String email)` 메서드 추가 (이미 있는지 확인)

---

### Step 13: application.yml에 Refresh Token 만료시간 추가

**추가할 설정**:
```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 3600000  # Access Token 1시간
  refresh-expiration: 604800000  # Refresh Token 7일
```

---

## 🎯 구현 순서 요약

1. DTO 생성 (LoginRequestDTO, LoginResponseDTO 등)
2. RefreshToken 엔티티 & Repository
3. JwtTokenProvider (토큰 생성/검증)
4. CustomUserDetailsService (사용자 조회)
5. JwtAuthenticationFilter (요청마다 토큰 검증)
6. AuthService (로그인 로직)
7. AuthController (인증 엔드포인트)
8. MemberService 수정 (비밀번호 암호화)
9. MemberRepository 확인/수정 (이메일 조회 메서드)
10. SecurityConfig 수정 (필터 등록, 경로 권한 설정)
11. 테스트

---

## 🔍 주의사항

### 1. Member 엔티티 수정 고려
현재 Member는 불변성이 강한 구조인데, 비밀번호 암호화를 위해 수정이 필요할 수 있습니다.

### 2. MemberRepository에 이메일 조회 메서드 추가
`Optional<Member> findByMemberEmail(String memberEmail);`

### 3. CustomUserDetailsService의 username 처리
로그인 시 아이디 또는 이메일을 받으므로, 둘 다 시도해야 합니다:
- 먼저 memberId로 조회
- 없으면 email로 조회
- 둘 다 없으면 예외

### 4. 역할(Role) 형식
Spring Security는 보통 "ROLE_" 접두사를 사용하는데, 필요시 "ROLE_GUEST", "ROLE_USER"로 저장하거나, SecurityConfig에서 처리 가능합니다.

### 5. 예외 처리
- 로그인 실패 시: `BadCredentialsException` 또는 커스텀 예외
- 토큰 만료/무효 시: `ExpiredJwtException`, `MalformedJwtException` 등

### 6. 비밀번호 업데이트 시 암호화
updateMember에서 비밀번호를 변경할 때도 암호화해야 합니다.

---

## 📊 테스트 순서

1. **회원가입 테스트**:
    - POST /member
    - 비밀번호가 암호화되어 DB에 저장되는지 확인
    - 기본 role이 "GUEST"인지 확인

2. **로그인 테스트**:
    - POST /auth/login (memberId로)
    - POST /auth/login (email로)
    - 토큰이 반환되는지 확인

3. **인증 필요 API 테스트**:
    - Authorization 헤더 없이 POST /board → 401 또는 403
    - Authorization 헤더와 함께 POST /board → 200

4. **토큰 갱신 테스트**:
    - POST /auth/refresh
    - 새로운 Access Token 발급 확인

5. **로그아웃 테스트**:
    - POST /auth/logout
    - Refresh Token이 DB에서 삭제되는지 확인

---

## 🔐 보안 체크리스트

- [ ] 비밀번호가 평문으로 저장되지 않는지
- [ ] JWT secret key가 환경변수로 관리되는지
- [ ] CSRF 보호가 필요한지 검토 (JWT 사용 시 보통 disable)
- [ ] 토큰 만료 시간이 적절한지
- [ ] Refresh Token이 DB에 안전하게 저장되는지
- [ ] 로그아웃 시 토큰이 무효화되는지
- [ ] SQL Injection 방지 (JPA 사용하므로 기본적으로 안전)
- [ ] 민감한 정보가 로그에 출력되지 않는지

---

## 💡 추가 개선 사항 (선택)

1. **역할 기반 접근 제어 (RBAC)**:
    - GUEST는 읽기만 가능
    - USER는 작성/수정/삭제 가능
    - ADMIN은 모든 권한

2. **이메일 인증**:
    - 회원가입 후 이메일 인증 필요

3. **비밀번호 찾기**:
    - 이메일로 임시 비밀번호 발송

4. **소셜 로그인**:
    - OAuth2 (구글, 카카오 등)

5. **로그인 시도 제한**:
    - 5회 실패 시 계정 잠금

6. **토큰 블랙리스트**:
    - 로그아웃된 Access Token을 블랙리스트에 추가

---

이 계획을 따라 단계별로 구현하면 JWT + Spring Security 인증 시스템을 완성할 수 있습니다!
