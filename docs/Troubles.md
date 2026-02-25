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

---

## 2월 19일 - 파일 첨부 기능 구현

### 요구사항
게시글 작성 시 프론트엔드에서 파일을 첨부할 수 있는 기능 구현

### 문제점 및 해결 과정

#### 1. **파일 업로드 크기 제한 초과 (Max Upload Size Exceeded)**

**증상:**
```
org.springframework.web.multipart.MaxUploadSizeExceededException: Maximum upload size exceeded
```
1.41MB의 이미지 업로드 시 에러 발생

**원인:**
Spring Boot 기본 multipart 설정의 최대 파일 크기가 1MB로 제한되어 있었음

**해결책:**
`application.yml` 및 `application-local.yml`에 multipart 설정 추가:
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
```

**학습점:**
- Spring Boot의 기본 multipart 크기 제한이 1MB임을 알게 됨
- 프로덕션에서는 필요에 따라 적절히 조정 필요 (현재 100MB로 설정)

---

#### 2. **YAML 설정 구조 오류 (데이터베이스 스키마 미생성)**

**증상:**
```
Unknown column 'create_time' in 'field list'
```
database 초기화 시 `create_time`, `update_time` 컬럼이 없음

**원인:**
`application-local.yml` 파일의 YAML 들여쓰기 구조가 잘못되어 있었음:
```yaml
# ❌ 잘못된 구조
server:
  servlet:
    # 여기에 spring.jpa와 spring.sql이 있었음 (들여쓰기 오류)
    jpa:
      hibernate:
        ddl-auto: validate
```

결과적으로 `init_schema.sql`이 실행되지 않아 테이블 컬럼이 생성되지 않음

**해결책:**
YAML 구조를 바로잡아 모든 Spring 설정이 `spring` 노드 하위에 올바르게 배치:
```yaml
spring:
  datasource:
    # ...
  jpa:
    hibernate:
      ddl-auto: validate
    # ...
  sql:
    init:
      mode: always
      schema-locations: classpath:db/init_schema.sql
      # ...
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
```

**학습점:**
- YAML 설정의 올바른 들여쓰기가 얼마나 중요한지 확인
- 설정이 적용되지 않을 때 YAML 구조를 먼저 확인해야 함

---

#### 3. **파일 선택 버튼 작동하지 않음**

**증상:**
파일 선택 버튼을 클릭해도 파일 선택 대화창이 나타나지 않음

**원인:**
`<label for="fileInput">` 태그의 `for` 속성이 `<input id="fileInput">` 요소와 연결되지 않았음
(Vue 템플릿에서 `id` 바인딩이 누락됨)

**해결책:**
BoardForm.vue의 파일 입력 요소에 `id` 속성 추가:
```vue
<input
  ref="fileInput"
  id="fileInput"
  type="file"
  multiple
  accept="image/*"
  @change="onFileSelected"
  hidden
/>
```

---

#### 4. **업로드된 이미지가 흰색으로 표시됨**

**증상:**
파일을 업로드하고 게시글 상세보기로 이동해도 이미지가 하얀색으로만 표시됨

**원인:**
프론트엔드에서 이미지 경로를 상대 경로로 사용:
```vue
<!-- ❌ 잘못된 방식 -->
<img :src="file.url" />
<!-- /uploads/uuid.jpg는 localhost:5173/uploads/uuid.jpg로 해석됨 -->
```

백엔드에서 파일을 제공하는 것은 `localhost:8080/uploads/uuid.jpg`이므로,
프론트엔드 서버(localhost:5173)에서는 해당 파일이 없어 로드 실패

**해결책:**
프론트엔드에서 `API_BASE_URL` 상수를 정의하고 이미지 경로에 추가:
```vue
<!-- script -->
const API_BASE_URL = 'http://localhost:8080';

<!-- template -->
<img :src="API_BASE_URL + file.url" alt="attached image" />
```

BoardDetail.vue에도 동일하게 적용:
```vue
<div class="attached-image">
  <img :src="API_BASE_URL + file.url" :alt="file.originalName" />
</div>
```

**학습점:**
- 프론트엔드와 백엔드가 다른 포트(5173 vs 8080)에서 실행될 때 절대 URL 필요
- 프로덕션에서는 동일한 도메인 또는 프록시 설정으로 해결

---

#### 5. **게시글 수정/삭제 버튼이 나타나지 않음**

**증상:**
로그인한 사용자가 자신이 작성한 게시글을 봐도 수정/삭제 버튼이 보이지 않음

**원인:**
로그인 정보와 게시글 작성자 정보 불일치:
```javascript
// 로그인 정보: localStorage.username = "tester123" (계정 ID)
// 게시글 정보: board.memberName = "김테스터" (실제 이름)

// ❌ 항상 false
if (currentUsername === board.memberName) { /* 버튼 표시 */ }
```

BoardResponse에 memberUsername 필드가 없어서 계정 ID 기반 비교 불가능

**해결책:**

1. **BoardResponse.java** - memberUsername 필드 추가:
```java
@Getter
public class BoardResponse {
    // ... 다른 필드들 ...
    private String memberUsername;    // 새로 추가
    private String memberName;

    public static BoardResponse from(Board board) {
        return new BoardResponse(
            // ...
            board.getMember().getUsername(),  // memberUsername
            board.getMember().getName()        // memberName
        );
    }
}
```

2. **BoardDetail.vue** - 계정 ID 기반 비교:
```vue
<div v-if="currentUsername === board.memberUsername" class="button-group">
  <button @click="editBoard">수정</button>
  <button @click="deleteBoard">삭제</button>
</div>
```

**학습점:**
- 사용자 식별은 username(고유한 계정 ID) 기반이어야 함
- displayName(실제 이름)과는 구분이 필요

---

#### 6. **파일 엔티티와 데이터베이스 스키마 불일치**

**증상:**
BoardFile 엔티티가 BaseEntity를 상속받아 `deleteTime` 필드를 가지고 있으나,
스키마에는 해당 컬럼이 없음

**원인:**
초기 설계 단계에서 파일도 소프트 삭제를 지원해야 하는지 명확히 하지 않았고,
엔티티와 스키마가 동기화되지 않음

**문제점:**
- `deleteTime` 필드는 자동 생성되어 모든 파일이 `deleteTime = null` 상태가 됨
- 파일 조회 쿼리에서 소프트 삭제 필터링을 해야 하나 현재 불필요함
- 엔티티 구조가 명확하지 않음

**해결책:**

**결정: 파일은 소프트 삭제하지 않음**

1. **BoardFile.java** 수정:
```java
@Entity
@Table(name = "board_file")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BoardFile {  // ❌ BaseEntity 상속 제거
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String savedName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String url;

    @CreatedDate
    protected LocalDateTime createTime;

    @LastModifiedDate
    protected LocalDateTime updateTime;

    // ❌ @Setter 제거
    // ❌ deleteTime 필드 제거
    // ❌ uploadTime 필드 제거

    // ✅ 생성자 추가 (생성 시점에만 필요한 필드)
    public BoardFile(Board board, String originalName, String savedName, Long size, String url) {
        this.board = board;
        this.originalName = originalName;
        this.savedName = savedName;
        this.size = size;
        this.url = url;
    }
}
```

2. **init_schema.sql** 수정:
```sql
CREATE TABLE IF NOT EXISTS board_file (
    file_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    saved_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    create_time DATETIME(6),
    update_time DATETIME(6),
    -- ❌ delete_time 제거
    -- ❌ upload_time 제거
    FOREIGN KEY (board_id) REFERENCES board(board_id)
);
```

**학습점:**
- 모든 엔티티가 BaseEntity를 상속받을 필요는 없음
- 도메인의 특성에 따라 필요한 필드만 사용해야 함
- 파일은 생성되고 삭제되기만 하므로 수정 불가능하게 설계하는 것이 맞음

---

#### 7. **BoardFileRepository의 @Transactional 어노테이션**

**증상:**
BoardFileRepository에 `@Transactional` 어노테이션이 있으나, 다른 모든 Repository에는 없음

**원인:**
Repository 레이어의 역할에 대한 일관되지 않은 이해

**해결책:**
모든 Repository에서 `@Transactional` 제거:
```java
@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findAllByBoard_BoardId(Long boardId);
    void deleteByBoard_BoardId(Long boardId);
    boolean existsByBoard_BoardId(Long boardId);
}
```

**이유:**
- Repository는 데이터 접근 계층으로 트랜잭션 관리 책임이 없음
- Service 레이어에서 `@Transactional` 어노테이션으로 트랜잭션 경계를 정의
- Repository는 순수한 데이터 접근 로직만 담당
- 프로젝트의 다른 모든 Repository와 일관성 유지

**학습점:**
- 일관된 아키텍처 패턴 유지의 중요성
- Repository는 데이터 접근만, Service는 비즈니스 로직과 트랜잭션 관리

---

#### 8. **BoardFile 객체 생성 시 Setter 사용**

**증상:**
BoardFile 객체를 생성할 때 `@Setter`를 사용하여 필드 설정

**문제점:**
- Lombok의 `@Setter` 남용으로 객체 불변성 위반
- 의도하지 않은 필드 변경 가능
- 생성 후 상태 변경을 명확히 할 수 없음

**해결책:**

BoardFile.java에서 `@Setter` 제거하고 생성자 추가:
```java
@Entity
@Table(name = "board_file")
@Getter
@NoArgsConstructor
// ❌ @Setter 제거
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BoardFile {
    // ... 필드들 ...

    // ✅ 커스텀 생성자 추가 (필요한 필드만)
    public BoardFile(Board board, String originalName, String savedName, Long size, String url) {
        this.board = board;
        this.originalName = originalName;
        this.savedName = savedName;
        this.size = size;
        this.url = url;
    }
}
```

BoardFileService.java에서 생성자 사용:
```java
private BoardFileResponse saveFile(Board board, MultipartFile file, String savedName) {
    String fileUrl = "/uploads/" + savedName;

    // ❌ 기존: Setter 사용
    // BoardFile boardFile = new BoardFile();
    // boardFile.setBoard(board);
    // boardFile.setOriginalName(file.getOriginalFilename());
    // ...

    // ✅ 개선: 생성자 사용
    BoardFile boardFile = new BoardFile(
        board,
        file.getOriginalFilename(),
        savedName,
        file.getSize(),
        fileUrl
    );

    BoardFile saved = boardFileRepository.save(boardFile);
    return new BoardFileResponse(saved.getFileId(), saved.getUrl(), saved.getCreateTime());
}
```

**학습점:**
- 객체는 생성 시 필요한 모든 상태를 가져야 함
- Setter는 특수한 경우를 제외하고 피할 것
- 불변성 원칙이 코드 품질을 높임

---

### 보안 및 설정 관련

#### SecurityConfig - 파일 업로드/다운로드 설정

**문제점:**
파일을 저장소에서 직접 제공해야 하는데, JWT 인증이 필요 없어야 함

**해결책:**

1. **WebMvcConfigurer를 통한 정적 리소스 매핑**
```java
@Bean
public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + File.separator + "uploads" + File.separator)
                .setCachePeriod(0);
        }
    };
}
```

**작동 방식:**
1. `/uploads/uuid.jpg` 요청이 들어옴
2. WebMvcConfigurer가 이 요청을 `user.dir/uploads/uuid.jpg` 파일로 매핑
3. 파일 시스템에서 직접 파일을 로드하여 반환
4. **중요: Controller가 존재하지 않으므로 JWT 필터를 거치지 않음**

2. **SecurityConfig에서 `/uploads/**` 허용**
```java
.requestMatchers("/uploads/**").permitAll()
```

**작동 방식:**
1. `/uploads/**`로 시작하는 모든 요청은 인증 없이 허용
2. JWT 검증을 건너뜀
3. 공개적으로 파일 다운로드 가능

**3가지 방식의 비교:**

| 방식 | 동작 | 사용 시점 |
|------|------|---------|
| **Controller** | API 로직 처리 후 응답 | 파일 검증, 권한 확인 필요 |
| **WebMvcConfigurer** | 파일 시스템에서 직접 제공 | 정적 파일, JWT 불필요 |
| **@EnableWebSecurity** | HTTP 요청 필터링 | 경로별 인증 규칙 설정 |

**학습점:**
- WebMvcConfigurer로 파일을 직접 제공하면 Controller 없이도 가능
- `/uploads/**`는 SecurityConfig에서 permitAll() 처리
- 절대 경로 사용으로 JAR 실행 시에도 일관된 동작 보장

---

### 구현된 기능

#### 프론트엔드 파일 선택 및 업로드

**BoardForm.vue:**
```vue
<script setup>
const files = ref([]);
const fileInput = ref(null);

const onFileSelected = (event) => {
  const selectedFiles = Array.from(event.target.files);
  files.value = [...files.value, ...selectedFiles];
};

const removeFile = (index) => {
  files.value.splice(index, 1);
};

const uploadFiles = async (boardId) => {
  for (const file of files.value) {
    await uploadFile(boardId, file);
  }
  files.value = [];
};

const createPost = async () => {
  // 게시글 생성
  const board = await createBoard(formData);

  // 파일 업로드
  if (files.value.length > 0) {
    await uploadFiles(board.boardId);
  }

  // 페이지 이동
  router.push(`/board/${board.boardId}`);
};
</script>
```

**파일 API (frontend/src/api/file.js):**
```javascript
import client from './client';

export const uploadFile = async (boardId, file) => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await client.post(
    `/boardfile/upload/${boardId}`,
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  );
  return response.data;
};

export const getFiles = async (boardId) => {
  const response = await client.get(`/boardfile/list/${boardId}`);
  return response.data;
};

export const deleteFile = async (boardId, fileId) => {
  const response = await client.delete(`/boardfile/${fileId}`);
  return response.data;
};
```

**Axios 자동 설정:**
```javascript
// client.js에서 자동으로 multipart 처리됨
// FormData를 전달하면 axios가 Content-Type을 자동으로 설정
```

---

### 수정된 파일 목록

| 파일 | 수정 내용 |
|------|---------|
| **frontend/src/views/board/BoardForm.vue** | 파일 선택 UI, 업로드 로직 추가 |
| **frontend/src/views/board/BoardDetail.vue** | API_BASE_URL 추가, 이미지 경로 수정, 계정명 비교 로직 수정 |
| **frontend/src/api/file.js** | 파일 업로드/조회/삭제 API 함수 구현 |
| **src/main/java/.../boardfile/entity/BoardFile.java** | BaseEntity 상속 제거, 생성자 추가, @Setter 제거 |
| **src/main/java/.../boardfile/service/BoardFileService.java** | 생성자 기반 객체 생성, 디버그 로그 제거 |
| **src/main/java/.../boardfile/repository/BoardFileRepository.java** | @Transactional 제거 |
| **src/main/java/.../board/dto/BoardResponse.java** | memberUsername 필드 추가 |
| **src/main/java/.../config/SecurityConfig.java** | WebMvcConfigurer 설정 추가, `/uploads/**` permitAll 설정 |
| **src/main/resources/db/init_schema.sql** | board_file 테이블 컬럼 수정 (delete_time, upload_time 제거) |
| **src/main/resources/application.yml** | multipart 설정 추가 |
| **src/main/resources/application-local.yml** | YAML 구조 수정, multipart 설정 추가 |

---

### 핵심 학습 내용

1. **멀티파트 파일 업로드**
   - Spring Boot multipart 설정의 크기 제한
   - FormData와 Content-Type 자동 처리
   - Axios의 multipart 자동 감지

2. **YAML 설정 구조**
   - 들여쓰기의 중요성
   - Spring Boot 프로필별 설정 override
   - 설정 미적용 시 YAML 구조부터 확인

3. **정적 리소스 제공**
   - WebMvcConfigurer를 통한 파일 매핑
   - 절대 경로 사용의 필요성
   - Controller 없는 파일 서빙

4. **아키텍처 일관성**
   - Repository에서 @Transactional 제거
   - 엔티티 상속 구조 명확화
   - 객체 불변성을 위한 생성자 패턴

5. **프론트-백 통신**
   - 다른 포트 간 절대 URL 필요
   - 사용자 식별은 username 기반
   - API_BASE_URL 상수화의 필요성

---

### 이전 버전과의 차이점

| 항목 | 이전 | 현재 |
|------|------|------|
| 파일 업로드 크기 제한 | 1MB (기본값) | 100MB (설정) |
| 파일 엔티티 상속 | BaseEntity (deleteTime 포함) | 직접 정의 (audit fields만) |
| 파일 객체 생성 | Setter 사용 | 생성자 사용 |
| Repository 트랜잭션 | @Transactional 있음 | 제거됨 |
| BoardResponse | memberName만 | memberName + memberUsername |
| 이미지 경로 | 상대 경로 | 절대 경로 (API_BASE_URL) |
| 파일 서빙 | 구현 안 됨 | WebMvcConfigurer + SecurityConfig |

---

## 2월 25일 - AWS S3 Presigned URL 설정

### 요구사항
프론트엔드에서 이미지를 S3에 직접 업로드할 수 있도록 Presigned URL 방식 구현

### 문제점 및 해결 과정

#### 1. **환경변수 로드 실패 (Could not resolve placeholder)**

**증상:**
```
org.springframework.beans.factory.BeanCreationException:
Could not resolve placeholder 'AWS_ACCESS_KEY_ID' in value "${AWS_ACCESS_KEY_ID}"
```

**시도 1 (실패): io.github.cdimascio:java-dotenv 라이브러리**
```gradle
❌ implementation 'io.github.cdimascio:java-dotenv:5.4.1'
```
→ Could not find io.github.cdimascio:java-dotenv:5.4.1 (의존성 버전 미존재)

**시도 2 (실패): CommunityApplication.java에서 수동 로드**
```java
❌ Dotenv dotenv = Dotenv.load();
   dotenv.entries().forEach(entry ->
     System.setProperty(entry.getKey(), entry.getValue())
   );
```
→ io.github.cdimascio 패키지를 찾을 수 없음 (컴파일 실패)

**최종 해결책 (성공): application.yml에 spring.config.import 설정**
```gradle
✅ implementation 'me.paulschwarz:spring-dotenv:4.0.0'  # 원래대로 유지
```
```yaml
✅ spring:
  config:
    import: optional:file:${user.dir}/.env[.properties]
  profiles:
    active: local
```

**동작 원리:**
- `me.paulschwarz:spring-dotenv`는 classpath에서 설정되면, Spring이 자동으로 PropertySource 등록
- `spring.config.import: optional:file:${user.dir}/.env[.properties]` 설정으로 .env 파일 명시적 로드
- `${user.dir}`: 현재 실행 디렉토리 (프로젝트 루트)
- `[.properties]`: .env 파일을 Properties 형식으로 파싱
- application.yml에서 `${AWS_ACCESS_KEY_ID}` 등의 변수 해석 가능

**학습점:**
- spring-dotenv는 "완전 자동"이 아니라, application.yml에 import 설정이 필요
- `spring.config.import` 문법: `optional:file:{절대경로}[.properties]`
- IDE 캐시/재시작 후 반드시 `gradle clean bootRun` 실행

---

#### 2. **.env 파일 보안 (이미 해결됨)**

**상태:** .gitignore에 `.env` 이미 등록됨
```
## 환경변수
.env  ← 올바르게 설정됨
```

**주의사항:**
- AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY 등 민감한 정보 포함
- **절대 GitHub에 푸시되지 않도록 확인** (`git status`에서 `.env` 안 보임)
- 프로덕션 배포 시 환경변수를 시스템 환경변수로 관리

---

### 구현 완료 항목

#### Backend (완료)
- ✅ S3Config Bean 등록
  - S3Client: S3 작업 직접 수행용
  - S3Presigner: Presigned URL 생성용
- ✅ ImageController: POST /api/images/presigned-url 엔드포인트
- ✅ S3PresignedUrlService: Presigned URL 발급 로직
  - UUID로 원본 파일명 숨김 (보안)
  - 15분 유효한 임시 업로드 URL 생성
  - 영구 접근 가능한 S3 이미지 URL 생성
- ✅ application.yml: AWS S3 설정 추가

#### 아직 미구현 (다음 단계)
- [ ] 프론트엔드: Toast UI Editor 통합
- [ ] 프론트엔드: Presigned URL로 S3 직접 업로드
- [ ] 백엔드: imageUrl 검증 로직 (URL이 우리 S3인지 확인)
- [ ] 백엔드: BoardFile 엔티티 연동

---

### API 명세

**엔드포인트:** `POST /api/images/presigned-url`

**요청:**
```json
{
  "fileName": "photo.jpg",
  "contentType": "image/jpeg"
}
```

**응답:**
```json
{
  "uploadUrl": "https://myeongsoo-community-images.s3.ap-northeast-2.amazonaws.com/images/550e8400-e29b-41d4-a716-446655440000.jpg?X-Amz-Algorithm=...",
  "imageUrl": "https://myeongsoo-community-images.s3.ap-northeast-2.amazonaws.com/images/550e8400-e29b-41d4-a716-446655440000.jpg",
  "s3Key": "images/550e8400-e29b-41d4-a716-446655440000.jpg"
}
```

**클라이언트 플로우:**
1. `uploadUrl`에 PUT 요청으로 파일 직접 S3 업로드 (서버 거치지 않음)
2. `imageUrl`을 에디터에 `<img src>` 삽입
3. 게시글 저장 시 HTML 전체를 백엔드로 전송
4. 백엔드는 `imageUrl`이 우리 S3 도메인인지만 검증 후 저장

---

### 수정된 파일 목록

| 파일 | 수정 내용 |
|------|---------|
| **build.gradle** | `me.paulschwarz:spring-dotenv:4.0.0` 유지 |
| **src/main/resources/application.yml** | `spring.config.import: optional:file:${user.dir}/.env[.properties]` 라인 추가 |
| **src/main/java/.../config/S3Config.java** | S3Client, S3Presigner Bean 등록 |
| **src/main/java/.../image/controller/ImageController.java** | POST /api/images/presigned-url 엔드포인트 |
| **src/main/java/.../image/service/S3PresignedUrlService.java** | Presigned URL 발급 로직 |
| **src/main/java/.../image/dto/PresignedUrlRequest.java** | fileName, contentType 필드 |
| **src/main/java/.../image/dto/PresignedUrlResponse.java** | uploadUrl, imageUrl, s3Key 필드 |

