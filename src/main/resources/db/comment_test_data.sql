-- Comment 더미 데이터 (게시글별로 다양한 댓글수)
-- 1번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Spring Boot 정말 편하네요.', 1, 5, NULL),
('기본 설정이 명확합니다.', 1, 12, NULL),
('프로젝트 구조도 이해하기 쉬워요.', 1, 8, 1),
('다음 글도 기대합니다!', 1, 23, NULL),
('버전 업그레이드는 쉬운가요?', 1, 7, NULL),
('보안 설정은 어떻게 하나요?', 1, 15, NULL),
('마이크로서비스로 갈 수 있나요?', 1, 20, NULL),
('정말 도움 많이 됐습니다!', 1, 3, 3);

-- 2번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('영속성 컨텍스트 개념이 명확해졌어요.', 2, 7, NULL),
('1차 캐시 부분 설명이 좋네요.', 2, 15, NULL),
('더티 체킹은 어떻게 동작하나요?', 2, 19, NULL);

-- 3번 게시글 (12개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('인덱스 때문에 고민 중이었는데 해결됐어요.', 3, 4, NULL),
('복합 인덱스 설명이 명확합니다.', 3, 11, NULL),
('왼쪽 우선 규칙 처음 알았네요!', 3, 18, NULL),
('EXPLAIN으로 확인하는 방법도 궁금해요.', 3, 22, 12),
('실제 프로젝트에서 어떻게 적용하나요?', 3, 25, NULL),
('쿼리 튜닝 팁도 알려주세요.', 3, 28, NULL),
('인덱스 크기 최적화는?', 3, 2, NULL),
('통계 갱신 주기는?', 3, 6, NULL),
('파티셔닝과 함께 쓸 수 있나요?', 3, 9, NULL),
('성능 개선 효과가 크군요!', 3, 14, NULL),
('다른 DB도 비슷한가요?', 3, 17, NULL),
('정말 유용한 글입니다!', 3, 21, NULL);

-- 4번 게시글 (2개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('REST API 설계 시 항상 참고합니다.', 4, 6, NULL),
('URI 네이밍 규칙 부분이 유용했어요.', 4, 14, NULL);

-- 5번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Hook 사용하면서 헷갈렸던 부분이 정리됐어요.', 5, 9, NULL),
('useEffect 의존성 배열 설명 좋습니다!', 5, 16, NULL),
('커스텀 Hook 만드는 방법도 알려주세요.', 5, 21, NULL),
('useState는 어떻게 동작하나요?', 5, 3, NULL),
('렌더링 최적화 방법은?', 5, 10, NULL),
('Suspense와 함께 쓸 수 있나요?', 5, 13, NULL),
('useCallback vs useMemo는?', 5, 18, NULL),
('Context API는?', 5, 24, NULL),
('정말 도움 됐습니다!', 5, 27, NULL),
('더 많은 예제 기대합니다!', 5, 30, NULL);

-- 6번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Docker 입문자에게 최고의 글이네요.', 6, 10, NULL),
('컨테이너 개념이 이해됐습니다.', 6, 17, NULL),
('Dockerfile 작성법도 다뤄주세요!', 6, 24, NULL),
('좋은 제안 감사합니다.', 6, 3, NULL),
('이미지 최적화는?', 6, 11, NULL);

-- 7번 게시글 (15개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Git Flow 복잡하다고 느꼈는데 이해했어요.', 7, 2, NULL),
('우리 팀은 Github Flow 쓰는데 참고됩니다.', 7, 13, NULL),
('브랜치 전략 선택 기준이 명확하네요.', 7, 19, NULL),
('릴리즈 브랜치는 어떻게 관리하나요?', 7, 25, NULL),
('핫픽스 브랜치도 있나요?', 7, 5, NULL),
('롤백은 어떻게 하나요?', 7, 8, NULL),
('태그 관리는 어떻게?', 7, 12, NULL),
('머지 충돌은 어떻게 해결?', 7, 15, NULL),
('리베이스와 머지 차이는?', 7, 18, NULL),
('스쿼시 머지는?', 7, 22, NULL),
('깃 훅은 어떻게?', 7, 26, NULL),
('CI/CD 연동은?', 7, 29, NULL),
('깃 운영 정말 중요하네요!', 7, 3, NULL),
('더 많은 팁 부탁합니다!', 7, 7, NULL),
('감사합니다!', 7, 9, NULL);

-- 8번 게시글 (1개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('EC2 배포 성공했습니다! 감사합니다.', 8, 5, NULL);

-- 9번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('JWT 구현하면서 막혔던 부분이 해결됐어요.', 9, 1, NULL),
('RefreshToken 관리 방법이 명확하네요.', 9, 15, NULL),
('토큰 만료 시간은 보통 얼마로 설정하나요?', 9, 20, NULL),
('AccessToken 1시간, RefreshToken 7일 정도 권장합니다.', 9, 5, 39),
('토큰 탈취는 어떻게 방지?', 9, 10, NULL),
('HTTPS는 필수인가요?', 9, 16, NULL),
('정말 유용합니다!', 9, 21, NULL);

-- 10번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('테스트 코드의 중요성을 다시 느낍니다.', 10, 3, NULL),
('Mockito 사용법이 잘 설명되어 있어요.', 10, 14, NULL),
('@Mock과 @InjectMocks 차이가 궁금해요.', 10, 22, NULL),
('@Mock은 모의 객체, @InjectMocks는 주입 대상입니다.', 10, 5, 43),
('이해됐습니다. 감사합니다!', 10, 22, 44),
('통합 테스트는?', 10, 8, NULL),
('E2E 테스트는?', 10, 12, NULL),
('테스트 커버리지는?', 10, 18, NULL),
('CI에서 자동 테스트는?', 10, 24, NULL),
('정말 필수 지식이네요!', 10, 27, NULL),
('감사합니다!', 10, 30, NULL);

-- 11번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('논리적 삭제가 정말 유용하네요.', 11, 8, NULL),
('데이터 복구도 쉽고 좋습니다.', 11, 16, NULL),
('성능 영향은 없나요?', 11, 24, NULL),
('인덱스를 잘 설계하면 괜찮습니다.', 11, 5, 48);

-- 12번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('CORS 에러로 몇 시간을 날렸는데 해결됐어요.', 12, 7, NULL),
('Spring에서 설정하는 방법이 명확합니다.', 12, 13, NULL),
('프리플라이트 요청도 잘 설명되어 있네요.', 12, 19, NULL),
('질문이 있는데 답변 가능할까요?', 12, 6, 52),
('네, 댓글로 남겨주세요!', 12, 5, 53),
('OPTIONS 메서드는?', 12, 11, NULL),
('Credentials는?', 12, 15, NULL),
('커스텀 헤더는?', 12, 20, NULL),
('정말 도움됩니다!', 12, 25, NULL);

-- 13번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Redis 캐싱으로 성능이 확 올라갔어요.', 13, 2, NULL),
('캐시 무효화 전략이 유용합니다.', 13, 17, NULL),
('TTL 설정 기준도 알려주세요.', 13, 21, NULL),
('데이터 특성에 맞게 설정하면 됩니다.', 13, 5, 57),
('메모리 관리는?', 13, 10, NULL),
('감사합니다!', 13, 14, NULL);

-- 14번 게시글 (13개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('SQL과 NoSQL 차이 정확히 이해했어요.', 14, 4, NULL),
('각각의 장단점이 명확하네요.', 14, 10, NULL),
('어떤 상황에 어떤 것을 써야 하나요?', 14, 18, NULL),
('프로젝트 요구사항에 따라 결정하면 됩니다.', 14, 5, NULL),
('좋은 가이드 감사합니다!', 14, 18, 62),
('확장성은?', 14, 7, NULL),
('비용은?', 14, 12, NULL),
('운영 난이도는?', 14, 17, NULL),
('스키마는?', 14, 21, NULL),
('쿼리 성능은?', 14, 23, NULL),
('라이센스는?', 14, 26, NULL),
('커뮤니티는?', 14, 28, NULL),
('정말 좋은 비교입니다!', 14, 31, NULL);

-- 15번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Github Actions로 CI/CD 구축했어요.', 15, 6, NULL),
('자동화 배포가 정말 편하네요.', 15, 12, NULL),
('테스트 자동화도 가능한가요?', 15, 23, NULL);

-- 16번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Nginx 로드밸런싱 설정이 명확합니다.', 16, 9, NULL),
('리버스 프록시 개념이 이해됐어요.', 16, 14, NULL),
('무중단 배포도 가능한가요?', 16, 20, NULL),
('헬스 체크와 함께 사용하면 가능합니다.', 16, 5, NULL),
('정말 유용한 팁이 많네요!', 16, 20, 71),
('스트림 캐싱은?', 16, 8, NULL),
('gzip 압축은?', 16, 13, NULL),
('감사합니다!', 16, 18, NULL);

-- 17번 게시글 (14개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('TypeScript로 마이그레이션하는 중입니다.', 17, 1, NULL),
('타입 안전성이 정말 좋네요.', 17, 11, NULL),
('초보자도 배우기 쉬운가요?', 17, 19, NULL),
('JavaScript 경험이 있으면 금방 배웁니다!', 17, 5, 75),
('Generics는?', 17, 7, NULL),
('Interfaces는?', 17, 9, NULL),
('Decorators는?', 17, 12, NULL),
('Type Guards는?', 17, 15, NULL),
('Union Types는?', 17, 17, NULL),
('Intersection Types는?', 17, 20, NULL),
('Enums는?', 17, 22, NULL),
('Modules는?', 17, 24, NULL),
('정말 강력한 언어네요!', 17, 26, NULL),
('감사합니다!', 17, 28, NULL);

-- 18번 게시글 (2개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('SSR의 이점이 명확하네요.', 18, 5, NULL),
('SEO 최적화도 가능하군요.', 18, 15, NULL);

-- 19번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('BeautifulSoup으로 크롤링 시작했어요.', 19, 3, NULL),
('웹 데이터 수집이 이렇게 쉽다니!', 19, 16, NULL),
('법적 문제는 없나요?', 19, 21, NULL),
('robots.txt와 이용약관을 반드시 확인하세요.', 19, 5, 84),
('Selenium은?', 19, 8, NULL),
('Puppeteer는?', 19, 11, NULL),
('데이터 파싱은?', 19, 14, NULL),
('에러 처리는?', 19, 17, NULL),
('속도 최적화는?', 19, 20, NULL),
('감사합니다!', 19, 23, NULL);

-- 20번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Pandas로 데이터 전처리하는 중입니다.', 20, 7, NULL),
('DataFrame 조작이 정말 직관적이네요.', 20, 13, NULL),
('대용량 데이터 처리는 어떻게 하나요?', 20, 25, NULL),
('Dask나 Spark을 고려해보세요.', 20, 5, NULL),
('유용한 팁 감사합니다!', 20, 25, 89),
('GroupBy는?', 20, 9, NULL),
('Merge는?', 20, 12, NULL);

-- 21번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('NoSQL 스키마 설계가 명확해졌어요.', 21, 2, NULL),
('임베딩 vs 참조 개념이 좋습니다.', 21, 17, NULL),
('정규화가 항상 답은 아니네요.', 21, 23, NULL),
('맞습니다! 유연한 설계가 중요합니다.', 21, 5, 93),
('인덱싱은?', 21, 6, NULL),
('샤딩은?', 21, 10, NULL),
('복제는?', 21, 14, NULL),
('트랜잭션은?', 21, 19, NULL),
('일관성은?', 21, 22, NULL),
('성능은?', 21, 26, NULL),
('감사합니다!', 21, 29, NULL);

-- 22번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('GraphQL 장점이 정말 많네요.', 22, 4, NULL),
('쿼리 최적화가 쉬워졌어요.', 22, 12, NULL),
('REST API에서 마이그레이션 어렵지 않나요?', 22, 18, NULL),
('단계적으로 도입할 수 있습니다.', 22, 5, NULL);

-- 23번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Kotlin 코루틴 정말 멋있네요.', 23, 8, NULL),
('async/await 문법이 직관적입니다.', 23, 14, NULL),
('Java와의 상호운용성도 좋고요.', 23, 19, NULL),
('Kotlin은 정말 좋은 언어입니다!', 23, 5, 102),
('Extension Functions는?', 23, 7, NULL),
('Data Classes는?', 23, 11, NULL),
('Sealed Classes는?', 23, 15, NULL),
('Delegation은?', 23, 18, NULL),
('감사합니다!', 23, 21, NULL);

-- 24번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Jetpack Compose 배우는 중입니다.', 24, 6, NULL),
('선언형 UI가 훨씬 간단하네요.', 24, 11, NULL),
('Material Design3 지원도 좋아요.', 24, 20, NULL),
('상태 관리가 직관적입니다!', 24, 5, NULL),
('레이아웃 코드가 너무 깔끔해졌어요.', 24, 20, 107);

-- 25번 게시글 (12개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Vue.js 정말 배우기 좋네요.', 25, 9, NULL),
('문법이 간단하고 우아합니다.', 25, 19, NULL),
('React보다 낮은 진입장벽이 좋아요.', 25, 25, NULL),
('Vue는 처음 배우기 정말 좋습니다!', 25, 5, 111),
('v-model은?', 25, 7, NULL),
('computed는?', 25, 10, NULL),
('watch는?', 25, 13, NULL),
('lifecycle hooks는?', 25, 16, NULL),
('component는?', 25, 18, NULL),
('props는?', 25, 21, NULL),
('emit은?', 25, 23, NULL),
('감사합니다!', 25, 26, NULL);

-- 26번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Vuex로 상태 관리 정말 편하네요.', 26, 10, NULL),
('State, Mutation, Action 구분이 명확합니다.', 26, 20, NULL),
('Pinia는 어떤가요?', 26, 26, NULL),
('Pinia도 좋지만 Vuex도 충분합니다.', 26, 5, 115),
('모두 시도해봐야겠어요!', 26, 26, 116),
('감사합니다!', 26, 28, NULL);

-- 27번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Flutter 위젯 종류가 이렇게 많네요.', 27, 11, NULL),
('StatefulWidget과 StatelessWidget 차이 이해했어요.', 27, 21, NULL),
('기본 위젯들이 잘 정리되어 있습니다.', 27, 1, NULL),
('Flutter 개발할 때 참고하겠습니다!', 27, 5, 120),
('Material Design은?', 27, 8, NULL),
('Cupertino는?', 27, 12, NULL),
('Custom Widgets는?', 27, 15, NULL),
('감사합니다!', 27, 18, NULL);

-- 28번 게시글 (15개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Dart 언어가 생각보다 강력하네요.', 28, 12, NULL),
('null safety 기능이 정말 좋습니다.', 28, 22, NULL),
('타입 추론도 잘 동작해요.', 28, 2, NULL),
('비동기 처리도 간단하고요.', 28, 5, NULL),
('Dart로 Flutter 개발하기 정말 좋아요!', 28, 22, 125),
('Variables는?', 28, 7, NULL),
('Functions는?', 28, 10, NULL),
('Classes는?', 28, 13, NULL),
('Mixins는?', 28, 16, NULL),
('Extensions는?', 28, 19, NULL),
('Generics는?', 28, 21, NULL),
('Type inference는?', 28, 24, NULL),
('Pattern matching은?', 28, 26, NULL),
('Records는?', 28, 28, NULL),
('정말 좋은 언어입니다!', 28, 31, NULL);

-- 29번 게시글 (3개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Elasticsearch 검색 속도가 놀라워요.', 29, 13, NULL),
('인덱싱 방식이 효율적입니다.', 29, 23, NULL),
('전문 검색 기능이 정말 강력합니다.', 29, 3, NULL);

-- 30번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Kafka 처리량이 정말 크네요.', 30, 14, NULL),
('파티션 구조가 확장성 좋습니다.', 30, 24, NULL),
('컨슈머 그룹 관리도 유연하고요.', 30, 4, NULL),
('분산 시스템에 최적화되어 있어요.', 30, 5, NULL),
('고가용성 시스템 구축할 때 추천합니다!', 30, 24, 134),
('Offset은?', 30, 8, NULL),
('Replication은?', 30, 11, NULL),
('Retention은?', 30, 15, NULL),
('Throughput은?', 30, 18, NULL),
('감사합니다!', 30, 21, NULL);

-- 31번 게시글 (7개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Linux 명령어 모음이 정말 유용해요.', 31, 1, NULL),
('모든 필수 명령어가 다 있네요.', 31, 11, NULL),
('예제도 실용적이고 좋습니다.', 31, 21, NULL),
('리눅스 초보자 강추합니다!', 31, 5, 138),
('권한 관리는?', 31, 7, NULL),
('파일 시스템은?', 31, 10, NULL),
('감사합니다!', 31, 14, NULL);

-- 32번 게시글 (11개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Shell Script로 자동화하니 정말 편하네요.', 32, 2, NULL),
('배치 작업 작성이 간단해졌어요.', 32, 12, NULL),
('스크립트 최적화 팁도 좋아요.', 32, 22, NULL),
('크론 작업과 함께 사용하면 최고예요.', 32, 5, NULL),
('시스템 관리자 필독서네요!', 32, 22, 143),
('Variables는?', 32, 6, NULL),
('Functions는?', 32, 9, NULL),
('Loops는?', 32, 13, NULL),
('Conditionals는?', 32, 16, NULL),
('Error Handling은?', 32, 19, NULL),
('감사합니다!', 32, 23, NULL);

-- 33번 게시글 (4개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('PostgreSQL이 더 강력하네요.', 33, 3, NULL),
('JSON 지원이 정말 유용합니다.', 33, 13, NULL),
('Advanced 기능들이 많고요.', 33, 23, NULL),
('엔터프라이즈 프로젝트에 좋아요!', 33, 5, 147);

-- 34번 게시글 (9개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('인덱스 설계가 성능에 큰 영향이네요.', 34, 4, NULL),
('B-tree 원리를 이해하니 설계가 쉬워요.', 34, 14, NULL),
('복합 인덱스 활용법이 실무적이네요.', 34, 24, NULL),
('EXPLAIN으로 쿼리 분석이 가능하군요.', 34, 9, NULL),
('DB 성능 최적화 필수 지식이에요!', 34, 5, 152),
('Hash Index는?', 34, 7, NULL),
('Full Text Index는?', 34, 11, NULL),
('Partial Index는?', 34, 16, NULL),
('감사합니다!', 34, 20, NULL);

-- 35번 게시글 (5개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('gRPC 성능이 정말 빠르네요.', 35, 5, NULL),
('Protocol Buffer가 효율적입니다.', 35, 15, NULL),
('REST보다 대역폭 사용이 적어요.', 35, 25, NULL),
('마이크로서비스 아키텍처 필수군요!', 35, 5, 156),
('감사합니다!', 35, 28, NULL);

-- 36번 게시글 (13개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('WebSocket으로 실시간 통신이 정말 좋아요.', 36, 6, NULL),
('채팅 기능 구현이 간단하네요.', 36, 16, NULL),
('Heartbeat 설정도 중요하군요.', 36, 26, NULL),
('재연결 로직은 어떻게 구현하나요?', 36, 2, NULL),
('클라이언트 측에서 처리하면 됩니다!', 36, 5, 161),
('메시지 포맷은?', 36, 8, NULL),
('보안은?', 36, 11, NULL),
('성능은?', 36, 14, NULL),
('확장성은?', 36, 17, NULL),
('모니터링은?', 36, 20, NULL),
('에러 처리는?', 36, 23, NULL),
('테스트는?', 36, 25, NULL),
('감사합니다!', 36, 28, NULL);

-- 37번 게시글 (2개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('RabbitMQ 신뢰성이 정말 높네요.', 37, 7, NULL),
('메시지 라우팅이 유연합니다.', 37, 17, NULL);

-- 38번 게시글 (8개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('MSA 설계 방법이 명확하네요.', 38, 8, NULL),
('서비스 분할 기준이 좋습니다.', 38, 18, NULL),
('API 게이트웨이의 역할이 중요하군요.', 38, 28, NULL),
('서비스 간 통신 복잡도는 어떻게 처리해요?', 38, 3, NULL),
('메시지 큐와 이벤트 기반 아키텍처를 추천합니다!', 38, 5, 170),
('Saga 패턴은?', 38, 10, NULL),
('서킷 브레이커는?', 38, 15, NULL),
('감사합니다!', 38, 22, NULL);

-- 39번 게시글 (10개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('OAuth 2.0 플로우 정말 명확해요.', 39, 9, NULL),
('소셜 로그인 구현이 간단합니다.', 39, 19, NULL),
('보안이 잘 고려되어 있네요.', 39, 29, NULL),
('현대 웹 애플리케이션 필수예요!', 39, 5, 174),
('Authorization Code는?', 39, 6, NULL),
('Implicit Flow는?', 39, 12, NULL),
('Client Credentials는?', 39, 16, NULL),
('Refresh Token은?', 39, 21, NULL),
('PKCE는?', 39, 25, NULL),
('감사합니다!', 39, 27, NULL);

-- 40번 게시글 (6개 댓글)
INSERT INTO comment (content, board_id, member_id, parent_id) VALUES
('Spring Security 설정이 강력하네요.', 40, 10, NULL),
('CSRF, CORS 보호가 자동으로 되고요.', 40, 20, NULL),
('커스텀 인증도 쉽게 구현할 수 있어요.', 40, 30, NULL),
('JWT와 조합하면 정말 좋습니다.', 40, 5, NULL),
('보안 강화 필수 프레임워크예요!', 40, 20, 179),
('감사합니다!', 40, 35, NULL);
