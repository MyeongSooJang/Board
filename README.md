# 게시판 프로젝트

## Spring Boot + Vue.js를 사용한 풀스택 게시판 웹 어플리케이션

### 프로젝트 동기 & 목표
 - 프론트엔드 - 백엔드 - 데이터베이스가 어떻게 연결되는지 이론만 알고 있는 것이
   아닌 전체 흐름을 직접 구현하며 END-TO-END 개발 역량을 쌓고자 한다.

### 핵심 기능

### 1. JWT 기반 인증/인가 시스템 🔐

**왜 JWT를 선택한 이유?**
- Session 방식 대비 서버 메모리 부담 없음 (Stateless)
- 프론트엔드와 백엔드를 독립적으로 배포 가능

**핵심 구현**
- Access Token (1시간) + Refresh Token (7일)
- Refresh Token은 DB에 저장하여 강제 만료 가능
- Spring Security + JWT 라이브러리 (jjwt 0.12.3)

#### 2. 게시판 CRUD 


