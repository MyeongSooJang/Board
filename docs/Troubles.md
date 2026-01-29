# Troubles

## 1월 19일

### 회원가입 시 비밀번호를 기준에 맞지 않게 입력시 -> 403 Forbidden
### Request Payload
```
{memberId: "mmsgod0427", memberName: "장명수", memberPwd: "1234", memberAge: 27,…}
memberAge: 27
memberEmail: "mmsgod0427@gmail.com"
memberId: "mmsgod0427"
memberName: "장명수"
memberPhone: "010-8310-9608"
memberPwd: "1234"memberRole: "GUEST"
```

### 403 Forbidden 이유 -> 예외처리가 없으면 Spring 적절하지 않은 HTTP반환
1. @Valid 검증 실패
2. MethodArgumentNotValidException 발생
3. @ExceptionHandler가 존재 하지 않음
4. Spring의 기본 예외 처리 -> DefaultHandlerExceptionResolver
5. 403 반환 

### 예외를 처리하는 ExceptionHandler가 필요하다.

---

## 해결 방법: 회원가입 예외 처리 시스템 구축

### 1. ErrorCode 생성
**파일**: `exception/dto/ErrorCode.java`

회원가입에 필요한 ErrorCode 추가:
- 400 Bad Request: VALIDATION_ERROR, INVALID_MEMBER_ID, INVALID_PASSWORD 등
- 409 Conflict: DUPLICATE_MEMBER_ID, DUPLICATE_EMAIL, DUPLICATE_PHONE

### 2. 예외 계층 구조 설계
**추상 클래스**: `exception/CustomException.java`

모든 비즈니스 예외의 부모 클래스:
- RuntimeException 상속
- ErrorCode 필드(errorCode) 보유
- 생성자에서 super(errorCode.getMessage()) 전달

**구체적인 Exception 클래스:**
- `ValidationException` - 입력값 검증 실패 (400)
- `DuplicateException` - 중복 에러 (409)

### 3. GlobalExceptionHandler 구성
**파일**: `exception/handler/GlobalExceptionHandler.java`

@RestControllerAdvice로 3개의 @ExceptionHandler 작성:

1. **CustomException 처리** (ValidationException, DuplicateException)
   - ErrorCode에서 HTTP 상태 코드 가져옴
   - ErrorResponse 생성해서 반환

2. **MethodArgumentNotValidException 처리** (@Valid 검증 실패)
   - 필드별 에러 수집해서 errors 맵에 저장
   - VALIDATION_ERROR 코드 사용
   - 모든 필드 에러를 한 번에 클라이언트에게 반환

### 4. MemberService 중복 체크 로직 추가
**파일**: `domain/member/service/MemberService.java`

enrollMember() 메서드에서 3가지 중복 체크:
- `existsByMemberId()` - 아이디 중복 확인
- `existsByMemberEmail()` - 이메일 중복 확인
- `existsByMemberPhone()` - 전화번호 중복 확인

각각 DuplicateException 던진다.

### 결과
-  사용자가 회원가입 실패 시 각 필드별로 뭐가 잘못되었는지 정확히 알 수 있음
-  입력 필드 수정 시 에러 메시지 자동 제거
-  검증 실패와 중복 에러를 구분해서 표시
-  일관된 에러 응답 형식

## 1월 20일
### Swagger 사이트에 들어갈때 NoSuchMethodError
~~~
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
~~~
- java.lang.NoSuchMethodError: 'void org.springframework.web.method.ControllerAdviceBean.<init>(java.lang.Object)'
발생 -
### 발생하는 주요 원인
- 의존성 버전이 맞지 않음
- 클래스로더 문제 
  - 라이브러리 최신버전 메소드를 사용 -> 옛날버전로드
- 재배포 불일치 
~~~
   public interface OrderService{
      void createOrder(Long userId);
   }
  public class OrderImpl implements OrderService{
      void createOrder { ... }
   }
  
   // 수정 
   public interface OrderService{
      void createOrder(Long userId, Integer count);
   }
~~~
구현체만 올리게 되고, 수정된 interface는 올리지 않은 체로, createOrder(Long userId, Integer count)
-> NoSuchMethodError

## 1월 21일

### 컬럼명 네이밍 컨벤션 정리

#### Primary Key(기본키)
- 모든 엔티티의 PK는 ID로 통일
- MEMBER_NO, BOARD_NO 이런식으로 사용하지 않음
- 그냥 ID를 사용

#### 일반 컬럼
- 엔티티명 접두사를 불필요
- member_name, member_email 이 아닌 email, name

#### 로그인 ID
- 국제표준 : username
- member_id로 하면(PK로 헷갈림)

## 1월 22일 

### 좋아요 기능 -> 할때마다 SAVE를 날린다..?

### 1월 28일
#### 토큰 만료 시 401 응답 처리

**문제**: 토큰이 만료되면 TokenException 발생하지만 프론트엔드에 401 응답이 가지 않음

**원인**: JwtAuthenticationFilter에서 TokenException을 제대로 처리하지 않음

#### JwtAuthenticationFilter - 현재 코드 (문제 있음)
```java
if (token != null) {
    jwtTokenProvider.validateToken(token);  // TokenException 발생 가능
    try {
        String username = jwtTokenProvider.getUsername(token);
        String memberRole = jwtTokenProvider.getRole(token);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + memberRole);
        List<GrantedAuthority> authorities = List.of(authority);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (Exception e) {
        e.printStackTrace();  // 나중에 구현하기 위해서 console 출력만하는 상황
    }
}
filterChain.doFilter(request, response);  // 401 응답을 못 받음
```

#### 해결 방법: Filter에서 TokenException 처리
```java
 catch (TokenException e) {
    throw e;
    return;  // 여기서 응답 종료

```

#### 핵심 학습: Filter vs Controller 예외 처리

**Filter 단계의 예외는 GlobalExceptionHandler로 전파되지 않음!**

```
요청 → [Filter] ← 여기서 예외 발생하면 Filter에서 직접 처리해야 함
       ↓
    [Controller] → GlobalExceptionHandler (여기서만 예외 처리 가능)
```

- Filter: 요청이 Controller에 도달하기 전에 가로챔
- GlobalExceptionHandler: Controller 단계의 예외만 처리
- **결론**: Filter에서 발생한 예외는 Filter 내에서 직접 처리해야 함

---

#### 백엔드 수정 사항

**1. TokenException을 CustomException으로 상속 (완료)**
```java
public class TokenException extends CustomException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
```

**2. JwtTokenProvider에서 ErrorCode로 throw (완료)**
```java
public void validateToken(String token) {
    try {
        // ...
    } catch (ExpiredJwtException e) {
        throw new TokenException(ErrorCode.TOKEN_EXPIRED);
    } catch (SignatureException e) {
        throw new TokenException(ErrorCode.INVALID_TOKEN);
    } catch (MalformedJwtException e) {
        throw new TokenException(ErrorCode.MALFORMED_TOKEN);
    } catch (IllegalArgumentException e) {
        throw new TokenException(ErrorCode.EMPTY_TOKEN);
    }
}
```

**3. GlobalExceptionHandler에 TokenException 핸들러 추가 (완료)**
```java
@ExceptionHandler(TokenException.class)
public ResponseEntity<ErrorResponse> handleTokenException(TokenException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    ErrorResponse response = new ErrorResponse(errorCode);
    return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(response);
}
```

**4. JwtAuthenticationFilter에서 TokenException 직접 처리 (필요함)**

Filter에서 직접 401 응답을 작성해야 함:
```java
if (token != null) {
    try {
        jwtTokenProvider.validateToken(token);
        String username = jwtTokenProvider.getUsername(token);
        String memberRole = jwtTokenProvider.getRole(token);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + memberRole);
        List<GrantedAuthority> authorities = List.of(authority);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (TokenException e) {
        // Filter에서 직접 401 응답 작성
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":\"" + e.getErrorCode().name() + "\",\"message\":\"" + e.getMessage() + "\"}");
        return;  // 여기서 응답 종료
    } catch (Exception e) {
        e.printStackTrace();
    }
}
filterChain.doFilter(request, response);
```

---

#### 프론트엔드 수정 사항 (완료)

**client.js의 응답 인터셉터 수정:**
```javascript
client.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      const errorData = error.response?.data
      const message = errorData?.message || '로그인이 만료되었습니다. 다시 로그인해주세요.'

      localStorage.removeItem('accessToken')
      localStorage.removeItem('refreshToken')

      alert(message)  // 사용자에게 메시지 표시
      window.location.href = '/login'  // 로그인 페이지로 이동
    }
    return Promise.reject(error)
  }
)
```

---

#### 백엔드 응답 예시

토큰 만료 시:
```json
{
  "code": "TOKEN_EXPIRED",
  "message": "토큰이 만료되었습니다",
  "httpStatus": 401,
  "timestamp": "2026-01-28T..."
}
```

---

#### Security Config -> requestMatcher(HttpMethod.POST,"/auth/refresh").permitAll())
- accessToken이 만료 -> 401 에러 발생이 됨
- Client: RefreshToken으로 /auth/refresh 호출해서 새로 발급을 해야함.
- Server: 요청이 들어오면 RefreshToken을 검증하고 accessToken을 발급
- Client: 요청을 시도 하는 순으로 진행

#### 요청시에 토큰이 만료되었기 때문에 Header에 accessToken이 없기 때문에 인증이 안됨
- JWT토큰 없이도 요청을 하도록 해주어야 한다.

