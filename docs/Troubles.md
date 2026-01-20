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