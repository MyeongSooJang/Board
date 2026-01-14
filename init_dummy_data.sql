-- ===============================================
-- 보드 게시판 더미 데이터 초기화
-- ===============================================

-- Safe Update Mode 비활성화
SET SQL_SAFE_UPDATES = 0;

-- 기존 데이터 전부 삭제
DELETE FROM comment;
DELETE FROM board;
DELETE FROM member;

-- AUTO_INCREMENT 초기화
ALTER TABLE member AUTO_INCREMENT = 1;
ALTER TABLE board AUTO_INCREMENT = 1;
ALTER TABLE comment AUTO_INCREMENT = 1;

-- ===============================================
-- 1. 회원(MEMBER) 5명 생성
-- ===============================================
INSERT INTO member (member_id, member_name, member_pwd, member_age, member_email, member_phone, member_role, create_time, update_time)
VALUES
('user1', '김철수', '$2a$10$oowGMEwIvLGy4PeC4Xqq5O1H3c1bP3K6h5Z2x4yQ8p1n2x7r6z8', 25, 'user1@example.com', '010-1111-1111', 'GUEST', NOW(), NOW()),
('user2', '이영희', '$2a$10$oowGMEwIvLGy4PeC4Xqq5O1H3c1bP3K6h5Z2x4yQ8p1n2x7r6z8', 28, 'user2@example.com', '010-2222-2222', 'GUEST', NOW(), NOW()),
('user3', '박민준', '$2a$10$oowGMEwIvLGy4PeC4Xqq5O1H3c1bP3K6h5Z2x4yQ8p1n2x7r6z8', 30, 'user3@example.com', '010-3333-3333', 'GUEST', NOW(), NOW()),
('user4', '최지은', '$2a$10$oowGMEwIvLGy4PeC4Xqq5O1H3c1bP3K6h5Z2x4yQ8p1n2x7r6z8', 26, 'user4@example.com', '010-4444-4444', 'GUEST', NOW(), NOW()),
('user5', '정수현', '$2a$10$oowGMEwIvLGy4PeC4Xqq5O1H3c1bP3K6h5Z2x4yQ8p1n2x7r6z8', 27, 'user5@example.com', '010-5555-5555', 'GUEST', NOW(), NOW());

-- ===============================================
-- 2. 게시물(BOARD) 15개 생성
-- ===============================================
INSERT INTO board (board_title, board_content, member_no, create_time, update_time)
VALUES
('Spring Boot 학습 가이드', 'Spring Boot를 처음 배우시는 분들을 위한 완벽한 가이드입니다. 기본 개념부터 실제 프로젝트까지 다룹니다.', 1, NOW(), NOW()),
('JWT 토큰 인증 구현하기', 'JWT 토큰을 사용한 인증 시스템을 구현하는 방법을 상세하게 설명합니다. 보안이 중요합니다!', 2, NOW(), NOW()),
('데이터베이스 설계 팁', '좋은 데이터베이스 설계가 프로젝트의 성능을 좌우합니다. 정규화와 최적화 팁을 공유합니다.', 3, NOW(), NOW()),
('REST API 개발 Best Practice', 'RESTful API를 개발할 때 따라야 할 Best Practice들을 정리했습니다.', 4, NOW(), NOW()),
('Vue.js 3 시작하기', 'Vue.js 3의 Composition API를 사용한 현대적인 개발 방식을 배워봅시다.', 5, NOW(), NOW()),
('React vs Vue 비교', 'React와 Vue를 비교 분석하고 각각의 장단점을 설명합니다.', 1, NOW(), NOW()),
('Docker를 이용한 배포', 'Docker를 사용하여 애플리케이션을 배포하는 과정을 정리했습니다.', 2, NOW(), NOW()),
('Kubernetes 기초', 'Kubernetes의 기본 개념과 배포 방법을 설명합니다.', 3, NOW(), NOW()),
('마이크로서비스 아키텍처', '마이크로서비스 아키텍처의 장단점과 구현 방법입니다.', 4, NOW(), NOW()),
('클린 코드 작성법', '읽기 쉽고 유지보수하기 좋은 코드를 작성하는 원칙들입니다.', 5, NOW(), NOW()),
('테스트 주도 개발(TDD)', '테스트 주도 개발의 개념과 실제 적용 방법을 배웁니다.', 1, NOW(), NOW()),
('Design Pattern 이해하기', '디자인 패턴을 이해하고 실제 프로젝트에 적용해봅시다.', 2, NOW(), NOW()),
('성능 최적화 전략', '애플리케이션 성능을 최적화하기 위한 다양한 전략들입니다.', 3, NOW(), NOW()),
('보안 검사 리스트', '보안 검사 체크리스트를 따라 안전한 애플리케이션을 개발합시다.', 4, NOW(), NOW()),
('개발자 면접 준비', '개발자 면접에서 자주 나오는 질문들과 답변을 정리했습니다.', 5, NOW(), NOW());

-- ===============================================
-- 3. 댓글(COMMENT) 45개 생성 (게시물당 3개)
-- ===============================================

-- 게시물 1번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('정말 좋은 글입니다! 많이 배워갑니다.', NULL, 1, 2, NOW(), NOW()),
('이 부분이 이해가 안 가는데 더 설명해주실 수 있나요?', NULL, 1, 3, NOW(), NOW()),
('저도 비슷한 경험이 있습니다. 공감됩니다!', NULL, 1, 4, NOW(), NOW());

-- 게시물 2번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('정확한 정보 감사합니다.', NULL, 2, 1, NOW(), NOW()),
('이렇게 하면 더 간단할 것 같아요.', NULL, 2, 5, NOW(), NOW()),
('다음 글도 기대됩니다!', NULL, 2, 2, NOW(), NOW());

-- 게시물 3번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('매우 유용한 정보네요.', NULL, 3, 3, NOW(), NOW()),
('시간내서 읽어봐야겠어요.', NULL, 3, 4, NOW(), NOW()),
('혹시 예제 코드를 공유해주실 수 있을까요?', NULL, 3, 5, NOW(), NOW());

-- 게시물 4번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('좋은 팁 감사합니다!', NULL, 4, 1, NOW(), NOW()),
('정말 도움이 됐습니다.', NULL, 4, 2, NOW(), NOW()),
('더 자세한 설명 부탁드립니다.', NULL, 4, 3, NOW(), NOW());

-- 게시물 5번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('Vue 시작할 때 정말 유용할 것 같아요.', NULL, 5, 4, NOW(), NOW()),
('실제 프로젝트에서 어떻게 적용하나요?', NULL, 5, 5, NOW(), NOW()),
('예제가 있으면 더 좋을 것 같습니다.', NULL, 5, 1, NOW(), NOW());

-- 게시물 6번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('둘 다 좋은 라이브러리 네요.', NULL, 6, 2, NOW(), NOW()),
('React를 추천하세요?', NULL, 6, 3, NOW(), NOW()),
('Vue가 더 배우기 쉬운 것 같아요.', NULL, 6, 4, NOW(), NOW());

-- 게시물 7번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('Docker 처음이라 도움이 돼요.', NULL, 7, 5, NOW(), NOW()),
('설치 과정이 쉬워 보이네요.', NULL, 7, 1, NOW(), NOW()),
('실제로 사용해봐야겠습니다.', NULL, 7, 2, NOW(), NOW());

-- 게시물 8번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('Kubernetes 배우고 싶었어요.', NULL, 8, 3, NOW(), NOW()),
('좋은 설명 감사합니다.', NULL, 8, 4, NOW(), NOW()),
('더 깊이 있는 내용 부탁드립니다.', NULL, 8, 5, NOW(), NOW());

-- 게시물 9번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('마이크로서비스에 관심이 있었어요.', NULL, 9, 1, NOW(), NOW()),
('장점과 단점이 명확하네요.', NULL, 9, 2, NOW(), NOW()),
('언제 적용하면 좋을까요?', NULL, 9, 3, NOW(), NOW());

-- 게시물 10번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('클린 코드의 중요성을 느낍니다.', NULL, 10, 4, NOW(), NOW()),
('실무에서 꼭 필요한 내용이네요.', NULL, 10, 5, NOW(), NOW()),
('좋은 가이드 감사합니다.', NULL, 10, 1, NOW(), NOW());

-- 게시물 11번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('TDD를 처음 배워봅니다.', NULL, 11, 2, NOW(), NOW()),
('실제로 어렵나요?', NULL, 11, 3, NOW(), NOW()),
('좋은 설명 감사합니다.', NULL, 11, 4, NOW(), NOW());

-- 게시물 12번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('디자인 패턴 공부가 필요했어요.', NULL, 12, 5, NOW(), NOW()),
('각 패턴의 예제가 도움됩니다.', NULL, 12, 1, NOW(), NOW()),
('더 많은 패턴을 다뤄주세요.', NULL, 12, 2, NOW(), NOW());

-- 게시물 13번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('성능 최적화 팁 감사합니다!', NULL, 13, 3, NOW(), NOW()),
('실제로 적용해봐야겠어요.', NULL, 13, 4, NOW(), NOW()),
('더 자세한 벤치마크가 있으면 좋겠어요.', NULL, 13, 5, NOW(), NOW());

-- 게시물 14번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('보안이 정말 중요하네요.', NULL, 14, 1, NOW(), NOW()),
('체크리스트 정리 감사합니다.', NULL, 14, 2, NOW(), NOW()),
('자주 참고하겠습니다.', NULL, 14, 3, NOW(), NOW());

-- 게시물 15번 댓글
INSERT INTO comment (comment_content, comment_parent_no, board_no, member_no, create_time, update_time)
VALUES
('면접 준비하는데 도움이 될 것 같아요.', NULL, 15, 4, NOW(), NOW()),
('더 많은 질문들을 다뤄주세요.', NULL, 15, 5, NOW(), NOW()),
('감사합니다!', NULL, 15, 1, NOW(), NOW());

-- ===============================================
-- 완료!
-- ===============================================
-- 확인 쿼리:
-- SELECT COUNT(*) FROM member;   -- 5명
-- SELECT COUNT(*) FROM board;    -- 15개
-- SELECT COUNT(*) FROM comment;  -- 45개
